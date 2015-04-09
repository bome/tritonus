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

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.tritonus.lowlevel.gsm.Encoder;
import org.tritonus.lowlevel.gsm.GsmConstants;
import org.tritonus.lowlevel.gsm.GsmFrameFormat;
import org.tritonus.share.TDebug;
import org.tritonus.share.sampled.AudioFormats;
import org.tritonus.share.sampled.TConversionTool;
import org.tritonus.share.sampled.convert.TAsynchronousFilteredAudioInputStream;
import org.tritonus.share.sampled.convert.TSimpleFormatConversionProvider;

/**
 * FormatConversionProvider for GSM 06.10.
 * 
 * @author Matthias Pfisterer
 */
public class GSMEncoderFormatConversionProvider extends
        TSimpleFormatConversionProvider
// extends TEncodingFormatConversionProvider
        implements GsmConstants
{
    private static final AudioFormat[] SOURCE_FORMATS = {
            new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                    8000.0F, false),
            new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                    8000.0F, true), };

    private static final AudioFormat[] TARGET_FORMATS = {//
            new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, -1, 1, 33, 50.0F,
                    false),//
            new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, -1, 1, 33, 50.0F, true),//
            new AudioFormat(MS_GSM_ENCODING, 8000.0F, -1, 1, 65, 25.0F, false),//
            new AudioFormat(MS_GSM_ENCODING, 8000.0F, -1, 1, 65, 25.0F, true),//
    };

    private static final int DECODED_BYTES_PER_FRAME = 2;

    /**
     * Constructor.
     */
    public GSMEncoderFormatConversionProvider()
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
                        .out("conversion supported; trying to create EncodedGSMAudioInputStream");
            }
            return new EncodedGSMAudioInputStream(targetFormat,
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
        // TODO: this is unreachable
        // if (TDebug.TraceAudioConverter) {
        // TDebug.out("GSMFormatConversionProvider.getAudioInputStream(): end");
        // }
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
     * AudioInputStream returned on encoding of GSM. An instance of this class
     * is returned if you call AudioSystem.getAudioInputStream(AudioFormat,
     * AudioInputStream) to encode data to GSM. This class contains the logic of
     * maintaining buffers and calling the encoder.
     */
    private static class EncodedGSMAudioInputStream extends
            TAsynchronousFilteredAudioInputStream
    {
        private AudioInputStream m_decodedStream;
        private GsmFrameFormat gsmFrameFormat;
        private Encoder m_encoder;

        /*
         * Holds one block of decoded data.
         */
        private byte[] m_abBuffer;

        /*
         * Holds one block of decoded data.
         */
        private short[] m_asBuffer;

        /*
         * Holds one encoded GSM frame.
         */
        private byte[] m_abFrameBuffer;

        public EncodedGSMAudioInputStream(AudioFormat targetFormat,
                AudioInputStream inputStream)
        {
            super(targetFormat, getTargetFrameLength(inputStream
                    .getFrameLength(), targetFormat));
            if (TDebug.TraceAudioConverter)
            {
                TDebug.out("EncodedGSMAudioInputStream.<init>(): begin");
            }
            m_decodedStream = inputStream;
            gsmFrameFormat = getGsmFrameFormat(targetFormat);
            m_encoder = new Encoder(gsmFrameFormat);
            m_abBuffer = new byte[gsmFrameFormat.getSamplesPerFrame()
                    * DECODED_BYTES_PER_FRAME];
            m_asBuffer = new short[gsmFrameFormat.getSamplesPerFrame()];
            m_abFrameBuffer = new byte[targetFormat.getFrameSize()];
            if (TDebug.TraceAudioConverter)
            {
                TDebug.out("EncodedGSMAudioInputStream.<init>(): end");
            }
        }

        private static long getTargetFrameLength(long lSourceFrameLength,
                AudioFormat targetFormat)
        {
            // $$fb 2001-04-16: FrameLength gives the number of 33-byte blocks !
            // inputStream.getFrameLength() == AudioSystem.NOT_SPECIFIED
            // ? AudioSystem.NOT_SPECIFIED :
            // inputStream.getFrameLength() / 160 * 33);
            return lSourceFrameLength == AudioSystem.NOT_SPECIFIED ? AudioSystem.NOT_SPECIFIED
                    : lSourceFrameLength
                            / getGsmFrameFormat(targetFormat)
                                    .getSamplesPerFrame();
        }

        private static GsmFrameFormat getGsmFrameFormat(AudioFormat targetFormat)
        {
            if (targetFormat.getEncoding().equals(MS_GSM_ENCODING))
            {
                return GsmFrameFormat.MICROSOFT;
            }
            else
            {
                return GsmFrameFormat.TOAST;
            }
        }

        public void execute()
        {
            if (TDebug.TraceAudioConverter)
            {
                TDebug.out(">EncodedGSMAudioInputStream.execute(): begin");
            }
            try
            {
                int nRead = m_decodedStream.read(m_abBuffer);
                /*
                 * Currently, we take all kinds of errors as end of stream.
                 */
                if (nRead != m_abBuffer.length)
                {
                    if (TDebug.TraceAudioConverter)
                    {
                        TDebug
                                .out("<EncodedGSMAudioInputStream.execute(): not read whole 160 sample block ("
                                        + nRead + ")");
                    }
                    getCircularBuffer().close();
                    return;
                }
            }
            catch (IOException e)
            {
                if (TDebug.TraceAllExceptions)
                {
                    TDebug.out(e);
                }
                getCircularBuffer().close();
                if (TDebug.TraceAudioConverter)
                {
                    TDebug.out("<");
                }
                return;
            }
            for (int i = 0; i < gsmFrameFormat.getSamplesPerFrame(); i++)
            {
                m_asBuffer[i] = TConversionTool.bytesToShort16(m_abBuffer, i
                        * DECODED_BYTES_PER_FRAME, isBigEndian());
            }
            m_encoder.encode(m_asBuffer, m_abFrameBuffer);
            getCircularBuffer().write(m_abFrameBuffer);
            if (TDebug.TraceAudioConverter)
            {
                TDebug
                        .out("<EncodedGSMAudioInputStream.execute(): encoded GSM frame written");
            }
            if (TDebug.TraceAudioConverter)
            {
                TDebug.out(">EncodedGSMAudioInputStream.execute(): end");
            }
        }

        private boolean isBigEndian()
        {
            return m_decodedStream.getFormat().isBigEndian();
        }

        public void close() throws IOException
        {
            super.close();
            m_decodedStream.close();
        }
    }
}

/*** GSMFormatConversionProvider.java ***/
