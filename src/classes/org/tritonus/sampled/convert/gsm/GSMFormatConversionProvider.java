/*
 *	GSMFormatConversionProvider.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer
 *  Copyright (c) 2001 by Florian Bomers <http://www.bomers.de>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
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
import javax.sound.sampled.AudioFormat.Encoding;

import org.tritonus.lowlevel.gsm.Encoder;
import org.tritonus.lowlevel.gsm.GSMDecoder;
import org.tritonus.lowlevel.gsm.GsmConstants;
import org.tritonus.lowlevel.gsm.GsmFrameFormat;
import org.tritonus.lowlevel.gsm.InvalidGSMFrameException;
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
public class GSMFormatConversionProvider extends
        TSimpleFormatConversionProvider
// extends TEncodingFormatConversionProvider
        implements GsmConstants
{

    /**
     * Debugging (profiling) hack.
     */
    private static final boolean MEASURE_DECODING_TIME = false;

    private static final AudioFormat[] FORMATS1 = {
            new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, -1, 1, 33, 50.0F,
                    false),
            new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, -1, 1, 33, 50.0F, true),
            new AudioFormat(MS_GSM_ENCODING, 8000.0F, -1, 1, 65, 25.0F, false),
            new AudioFormat(MS_GSM_ENCODING, 8000.0F, -1, 1, 65, 25.0F, true),
            // temporary only
            new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                    8000.0F, false),
            new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                    8000.0F, true), };

    /*
     * private static final AudioFormat[] FORMATS2 = { new AudioFormat(8000.0F,
     * 16, 1, true, false), new AudioFormat(8000.0F, 16, 1, true, true), };
     */
    private static final AudioFormat[] FORMATS2 = FORMATS1;

    /**
     * Size of the circular buffer for toast frame format. This value is in
     * bytes. It is chosen so that one (decoded) GSM frame fits into the buffer.
     * GSM frames contain 160 samples. The value is 160 samples * 2 bytes per
     * sample.
     */
    private static final int SINGLE_FRAME_OUTPUT_BUFFER_SIZE = TOAST_SAMPLES_PER_FRAME * 2;
    /**
     * Size of the circular buffer for Microsoft frame format. This value is in
     * bytes. It is chosen so that one (decoded) GSM frame fits into the buffer.
     * GSM frames contain 160 samples. The value is 160 samples * 2 bytes per
     * sample * 2 frames.
     */
    private static final int DOUBLE_FRAME_OUTPUT_BUFFER_SIZE = MICROSOFT_SAMPLES_PER_FRAME * 2;

    /**
     * Constructor.
     */
    public GSMFormatConversionProvider()
    {
        super(Arrays.asList(FORMATS1), Arrays.asList(FORMATS2));
        if (TDebug.TraceAudioConverter)
        {
            TDebug.out("GSMFormatConversionProvider.<init>(): begin");
        }
        if (TDebug.TraceAudioConverter)
        {
            TDebug.out("GSMFormatConversionProvider.<init>(): end");
        }
    }

    /*
     * $$MP: TODO: hack to prevent the converter announcing to convert from PCM
     * little-endian to PCM big-endian! Should be solved on a general basis!!
     */
    public boolean isConversionSupported(AudioFormat targetFormat,
            AudioFormat sourceFormat)
    {
        if (targetFormat.getEncoding().equals(Encoding.PCM_SIGNED)
                && sourceFormat.getEncoding().equals(Encoding.PCM_SIGNED))
        {
            return false;
        } else
        {
            return super.isConversionSupported(targetFormat, sourceFormat);
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
            if (targetFormat.getEncoding().equals(
                    AudioFormat.Encoding.PCM_SIGNED))
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
            } else
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
        } else
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
     * AudioInputStream returned on decoding of GSM. An instance of this class
     * is returned if you call AudioSystem.getAudioInputStream(AudioFormat,
     * AudioInputStream) to decode a GSM stream. This class contains the logic
     * of maintaining buffers and calling the decoder.
     */
    public static class DecodedGSMAudioInputStream extends
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
            } catch (IOException e)
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
            } catch (InvalidGSMFrameException e)
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
            } else if (audioFormat.getEncoding().equals(TOAST_GSM_ENCODING))
            {
                return GsmFrameFormat.TOAST;
            } else
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

    /**
     * AudioInputStream returned on encoding of GSM. An instance of this class
     * is returned if you call AudioSystem.getAudioInputStream(AudioFormat,
     * AudioInputStream) to encode data to GSM. This class contains the logic of
     * maintaining buffers and calling the encoder.
     */
    public static class EncodedGSMAudioInputStream extends
            TAsynchronousFilteredAudioInputStream
    {
        private AudioInputStream m_decodedStream;
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

        public EncodedGSMAudioInputStream(AudioFormat outputFormat,
                AudioInputStream inputStream)
        {
            super(outputFormat,
            // $$fb 2001-04-16: FrameLength gives the number of 33-byte blocks !
                    // inputStream.getFrameLength() == AudioSystem.NOT_SPECIFIED
                    // ? AudioSystem.NOT_SPECIFIED :
                    // inputStream.getFrameLength() / 160 * 33);
                    inputStream.getFrameLength() == AudioSystem.NOT_SPECIFIED ? AudioSystem.NOT_SPECIFIED
                            : inputStream.getFrameLength() / 160);
            if (TDebug.TraceAudioConverter)
            {
                TDebug.out("EncodedGSMAudioInputStream.<init>(): begin");
            }
            m_decodedStream = inputStream;
            m_encoder = new Encoder();
            m_abBuffer = new byte[SINGLE_FRAME_OUTPUT_BUFFER_SIZE];
            m_asBuffer = new short[160];
            // m_abFrameBuffer = new byte[ENCODED_GSM_FRAME_SIZE];
            m_abFrameBuffer = new byte[outputFormat.getFrameSize()];
            if (TDebug.TraceAudioConverter)
            {
                TDebug.out("EncodedGSMAudioInputStream.<init>(): end");
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
            } catch (IOException e)
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
            for (int i = 0; i < 160; i++)
            {
                m_asBuffer[i] = TConversionTool.bytesToShort16(m_abBuffer,
                        i * 2, isBigEndian());
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
