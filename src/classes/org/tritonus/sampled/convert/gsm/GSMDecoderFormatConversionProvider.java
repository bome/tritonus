/*
 *	GSMFormatConversionProvider.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer
 *  Copyright (c) 2001 by Florian Bomers <http://www.bomers.de>
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/*
 |<---            this code is formatted to fit into 80 columns             --->|
 */

package org.tritonus.sampled.convert.gsm;

import static org.tritonus.sampled.convert.gsm.GsmEncodings.MS_GSM_ENCODING;
import static org.tritonus.sampled.convert.gsm.GsmEncodings.TOAST_GSM_ENCODING;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.tritonus.lowlevel.gsm.GSMDecoder;
import org.tritonus.lowlevel.gsm.GsmConstants;
import org.tritonus.lowlevel.gsm.GsmFrameFormat;
import org.tritonus.lowlevel.gsm.InvalidGSMFrameException;
import org.tritonus.share.TDebug;
import org.tritonus.share.sampled.AudioFormats;
import org.tritonus.share.sampled.convert.TAsynchronousFilteredAudioInputStream;
import org.tritonus.share.sampled.convert.TSimpleFormatConversionProvider;

/**
 * FormatConversionProvider for GSM 06.10.
 * 
 * @author Matthias Pfisterer
 */
public class GSMDecoderFormatConversionProvider extends
        TSimpleFormatConversionProvider
// extends TEncodingFormatConversionProvider
        implements GsmConstants
{

    /**
     * Debugging (profiling) hack.
     */
    private static final boolean MEASURE_DECODING_TIME = false;

    private static final AudioFormat[] SOURCE_FORMATS = {//
            new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, -1, 1, 33, 50.0F,
                    false),//
            new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, -1, 1, 33, 50.0F, true),//
            new AudioFormat(MS_GSM_ENCODING, 8000.0F, -1, 1, 65, 25.0F, false),//
            new AudioFormat(MS_GSM_ENCODING, 8000.0F, -1, 1, 65, 25.0F, true),//
    };

    private static final AudioFormat[] TARGET_FORMATS = { //
            new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                    8000.0F, false),//
            new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                    8000.0F, true) //
    };

    /**
     * Constructor.
     */
    public GSMDecoderFormatConversionProvider()
    {
        super(Arrays.asList(SOURCE_FORMATS), Arrays.asList(TARGET_FORMATS));
        if (TDebug.TraceAudioConverter)
        {
            TDebug.out("GSMFormatConversionProvider.<init>(): begin");
        }
        if (TDebug.TraceAudioConverter)
        {
            TDebug.out("GSMFormatConversionProvider.<init>(): end");
        }
    }

    public AudioInputStream getAudioInputStream(AudioFormat targetFormat,
            AudioInputStream audioInputStream)
    {
        if (TDebug.TraceAudioConverter)
        {
            TDebug
                    .out("GSMFormatConversionProvider.getAudioInputStream(): begin");
            TDebug.out("GSMFormatConversionProvider.getAudioInputStream():");
            TDebug.out("checking if conversion supported");
            TDebug.out("from: " + audioInputStream.getFormat());
            TDebug.out("to: " + targetFormat);
        }

        targetFormat = getDefaultTargetFormat(targetFormat, audioInputStream
                .getFormat());
        if (isConversionSupported(targetFormat, audioInputStream.getFormat()))
        {
            if (TDebug.TraceAudioConverter)
            {
                TDebug
                        .out("GSMFormatConversionProvider.getAudioInputStream():");
                TDebug
                        .out("conversion supported; trying to create DecodedGSMAudioInputStream");
            }
            return new DecodedGSMAudioInputStream(targetFormat,
                    audioInputStream);
        }
        else
        {
            if (TDebug.TraceAudioConverter)
            {
                TDebug
                        .out("GSMFormatConversionProvider.getAudioInputStream():");
                TDebug
                        .out("conversion not supported; throwing IllegalArgumentException");
            }
            throw new IllegalArgumentException("conversion not supported");
        }
    }

    protected AudioFormat getDefaultTargetFormat(AudioFormat targetFormat,
            AudioFormat sourceFormat)
    {
        // return first of the matching formats
        // pre-condition: the predefined target formats (FORMATS2) must be
        // well-defined !
        Iterator<AudioFormat> iterator = getCollectionTargetFormats()
                .iterator();
        while (iterator.hasNext())
        {
            AudioFormat format = iterator.next();
            if (AudioFormats.matches(targetFormat, format))
            {
                return format;
            }
        }
        throw new IllegalArgumentException("conversion not supported");
    }

    /**
     * AudioInputStream returned on decoding of GSM. An instance of this class
     * is returned if you call AudioSystem.getAudioInputStream(AudioFormat,
     * AudioInputStream) to decode a GSM stream. This class contains the logic
     * of maintaining buffers and calling the decoder.
     */
    private static class DecodedGSMAudioInputStream extends
            TAsynchronousFilteredAudioInputStream
    {
        /*
         * Seems like DataInputStream (opposite to InputStream) is only needed
         * for readFully(). readFully-behaviour should perhaps be implemented in
         * AudioInputStream anyway (so this construct may become obsolete).
         */
        private DataInputStream m_encodedStream;
        private GSMDecoder m_decoder;

        /*
         * Holds one encoded GSM frame.
         */
        private byte[] m_abFrameBuffer;
        private byte[] m_abSampleBuffer;

        public DecodedGSMAudioInputStream(AudioFormat outputFormat,
                AudioInputStream inputStream)
        {
            super(
                    outputFormat,
                    inputStream.getFrameLength() == AudioSystem.NOT_SPECIFIED ? AudioSystem.NOT_SPECIFIED
                            : inputStream.getFrameLength()
                                    * getSamplesPerFrame(inputStream
                                            .getFormat()));
            if (TDebug.TraceAudioConverter)
            {
                TDebug.out("DecodedGSMAudioInputStream.<init>(): begin");
            }
            m_encodedStream = new DataInputStream(inputStream);
            GsmFrameFormat gsmFrameFormat = getGsmFrameFormat(inputStream
                    .getFormat());
            int sampleBufferSize = gsmFrameFormat.getSamplesPerFrame() * 2;
            m_decoder = new GSMDecoder(gsmFrameFormat);
            m_abFrameBuffer = new byte[inputStream.getFormat().getFrameSize()];
            m_abSampleBuffer = new byte[sampleBufferSize];
            if (TDebug.TraceAudioConverter)
            {
                TDebug.out("DecodedGSMAudioInputStream.<init>(): end");
            }
        }

        public void execute()
        {
            if (TDebug.TraceAudioConverter)
            {
                TDebug.out("DecodedGSMAudioInputStream.execute(): begin");
            }
            try
            {
                m_encodedStream.readFully(m_abFrameBuffer);
            }
            catch (IOException e)
            {
                /*
                 * Not only errors, but also EOF is caught here.
                 */
                if (TDebug.TraceAllExceptions)
                {
                    TDebug.out(e);
                }
                getCircularBuffer().close();
                return;
            }

            try
            {
                long lTimestamp1;
                long lTimestamp2;
                if (MEASURE_DECODING_TIME)
                {
                    lTimestamp1 = System.currentTimeMillis();
                }
                m_decoder.decode(m_abFrameBuffer, 0, m_abSampleBuffer, 0,
                        isBigEndian());
                // testing test hack
                // m_abBuffer[0] = 0;
                if (MEASURE_DECODING_TIME)
                {
                    lTimestamp2 = System.currentTimeMillis();
                    System.out.println("GSM decode (ms): "
                            + (lTimestamp2 - lTimestamp1));
                }
            }
            catch (InvalidGSMFrameException e)
            {
                if (TDebug.TraceAllExceptions)
                {
                    TDebug.out(e);
                }
                getCircularBuffer().close();
                return;
            }

            getCircularBuffer().write(m_abSampleBuffer);
            if (TDebug.TraceAudioConverter)
            {
                TDebug
                        .out("DecodedGSMAudioInputStream.execute(): decoded GSM frame written");
            }
            if (TDebug.TraceAudioConverter)
            {
                TDebug.out("DecodedGSMAudioInputStream.execute(): end");
            }
        }

        private static int getSamplesPerFrame(AudioFormat audioFormat)
        {
            return getGsmFrameFormat(audioFormat).getSamplesPerFrame();
        }

        private static GsmFrameFormat getGsmFrameFormat(AudioFormat audioFormat)
        {
            if (audioFormat.getEncoding().equals(MS_GSM_ENCODING))
            {
                return GsmFrameFormat.MICROSOFT;
            }
            else if (audioFormat.getEncoding().equals(TOAST_GSM_ENCODING))
            {
                return GsmFrameFormat.TOAST;
            }
            else
            {
                throw new RuntimeException("Unknown GSM frame format");
            }
        }

        private boolean isBigEndian()
        {
            return getFormat().isBigEndian();
        }

        public void close() throws IOException
        {
            super.close();
            m_encodedStream.close();
        }
    }
}

/*** GSMFormatConversionProvider.java ***/
