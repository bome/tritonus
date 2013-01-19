package org.tritonus.test.tritonus.sampled.convert.gsm;

import static javax.sound.sampled.AudioSystem.NOT_SPECIFIED;

import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.spi.FormatConversionProvider;

import org.junit.Before;
import org.junit.Test;
import org.tritonus.sampled.convert.gsm.GSMDecoderFormatConversionProvider;
import org.tritonus.share.sampled.Encodings;

public class GSMDecoderFormatConversionProviderTest extends
        AbstractGsmFormatConversionProviderTest
{
    @Before
    public void setUp() throws Exception
    {
        setFormatConversionProvider(new GSMDecoderFormatConversionProvider());
    }

    /**
     * Test for {@link FormatConversionProvider#getSourceEncodings()}.
     */
    @Test
    public void testGetSourceEncodings()
    {
        List<Encoding> expectedSupportedEncodings = Arrays.asList(
                TOAST_GSM_ENCODING, MS_GSM_ENCODING);
        List<Encoding> expectedUnsupportedEncodings = Arrays.asList(
                Encoding.PCM_SIGNED, Encoding.PCM_UNSIGNED, Encoding.ALAW,
                Encoding.ULAW);
        testGetEncodings(false, expectedSupportedEncodings,
                expectedUnsupportedEncodings);
    }

    /**
     * Test for {@link FormatConversionProvider#getTargetEncodings()}.
     */
    @Test
    public void testGetTargetEncodings()
    {
        List<Encoding> expectedSupportedEncodings = Arrays
                .asList(Encoding.PCM_SIGNED);
        List<Encoding> expectedUnsupportedEncodings = Arrays.asList(
                Encoding.PCM_UNSIGNED, TOAST_GSM_ENCODING, MS_GSM_ENCODING,
                Encoding.ALAW, Encoding.ULAW);
        testGetEncodings(true, expectedSupportedEncodings,
                expectedUnsupportedEncodings);
    }

    /**
     * Test for
     * {@link FormatConversionProvider#isSourceEncodingSupported(Encoding)}.
     */
    @Test
    public void testIsSourceEncodingSupportedEncoding()
    {
        List<Encoding> expectedSupportedEncodings = Arrays.asList(
                TOAST_GSM_ENCODING, MS_GSM_ENCODING);
        List<Encoding> expectedUnsupportedEncodings = Arrays.asList(
                Encoding.PCM_SIGNED, Encoding.PCM_UNSIGNED, Encoding.ALAW,
                Encoding.ULAW);

        testIsEncodingSupportedEncoding(false, expectedSupportedEncodings,
                expectedUnsupportedEncodings);
    }

    /**
     * Test for
     * {@link FormatConversionProvider#isSourceEncodingSupported(Encoding)}.
     */
    @Test
    public void testIsTargetEncodingSupportedEncoding()
    {
        List<Encoding> expectedSupportedEncodings = Arrays
                .asList(Encoding.PCM_SIGNED);
        List<Encoding> expectedUnsupportedEncodings = Arrays.asList(
                TOAST_GSM_ENCODING, MS_GSM_ENCODING, Encoding.PCM_UNSIGNED,
                Encoding.ALAW, Encoding.ULAW);

        testIsEncodingSupportedEncoding(true, expectedSupportedEncodings,
                expectedUnsupportedEncodings);
    }

    /**
     * Test for {@link FormatConversionProvider#getTargetEncodings(AudioFormat)}
     * .
     */
    @Test
    public void testGetTargetEncodingsAudioFormat()
    {
        testGetTargetEncodingsAudioFormat(//
                new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, ALL, 1, ALL,
                        50.0F, true), //
                Arrays.asList(Encoding.PCM_SIGNED), //
                Arrays.asList(Encoding.PCM_UNSIGNED, Encoding.ALAW,
                        Encoding.ULAW, TOAST_GSM_ENCODING, MS_GSM_ENCODING));
        testGetTargetEncodingsAudioFormat(//
                new AudioFormat(MS_GSM_ENCODING, 8000.0F, ALL, 1, ALL, 25.0F,
                        true), //
                Arrays.asList(Encoding.PCM_SIGNED), //
                Arrays.asList(Encoding.PCM_UNSIGNED, Encoding.ALAW,
                        Encoding.ULAW, TOAST_GSM_ENCODING, MS_GSM_ENCODING));
    }

    /**
     * Test for
     * {@link FormatConversionProvider#isConversionSupported(Encoding, AudioFormat)}
     * .
     */
    @Test
    public void testIsConversionSupportedEncodingAudioFormat()
    {
        testIsConversionSupportedEncodingAudioFormat(//
                new AudioFormat(Encodings.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, true), //
                EMPTY_ENCODING_LIST, //
                Arrays.asList(TOAST_GSM_ENCODING, MS_GSM_ENCODING,
                        Encoding.PCM_SIGNED, Encoding.PCM_UNSIGNED,
                        Encoding.ALAW, Encoding.ULAW));
        testIsConversionSupportedEncodingAudioFormat(//
                new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, 16, 1, 33, 50.0F,
                        true), //
                Arrays.asList(Encoding.PCM_SIGNED), //
                Arrays.asList(TOAST_GSM_ENCODING, MS_GSM_ENCODING,
                        Encoding.PCM_UNSIGNED, Encoding.ALAW, Encoding.ULAW));
        testIsConversionSupportedEncodingAudioFormat(//
                new AudioFormat(MS_GSM_ENCODING, 8000.0F, 16, 1, 65, 25.0F,
                        true), //
                Arrays.asList(Encoding.PCM_SIGNED), //
                Arrays.asList(TOAST_GSM_ENCODING, MS_GSM_ENCODING,
                        Encoding.PCM_UNSIGNED, Encoding.ALAW, Encoding.ULAW));
    }

    /**
     * Test for
     * {@link FormatConversionProvider#getTargetFormats(Encoding, AudioFormat)}.
     */
    @Test
    public void testGetTargetFormatsEncodingAudioFormat()
    {
        testGetTargetFormatsEncodingAudioFormat(Encoding.PCM_SIGNED, //
                new AudioFormat(TOAST_GSM_ENCODING, 8000.0F,
                        AudioSystem.NOT_SPECIFIED, 1, 33, 50.0F, true), //
                Arrays.asList(new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16,
                        1, 2, 8000.0F, true),//
                        new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                                8000.0F, true)//
                        ), //
                Arrays
                        .asList(//
                                new AudioFormat(TOAST_GSM_ENCODING, 8000.0F,
                                        AudioSystem.NOT_SPECIFIED, 1, 33,
                                        50.0F, false),//
                                new AudioFormat(TOAST_GSM_ENCODING, 8000.0F,
                                        AudioSystem.NOT_SPECIFIED, 1, 33,
                                        50.0F, true),//
                                new AudioFormat(MS_GSM_ENCODING, 8000.0F,
                                        AudioSystem.NOT_SPECIFIED, 1, ALL, ALL,
                                        false),//
                                new AudioFormat(MS_GSM_ENCODING, 8000.0F,
                                        AudioSystem.NOT_SPECIFIED, 1, ALL, ALL,
                                        true)//
                        )//
        );
        testGetTargetFormatsEncodingAudioFormat(Encoding.PCM_SIGNED, //
                new AudioFormat(MS_GSM_ENCODING, 8000.0F,
                        AudioSystem.NOT_SPECIFIED, 1, 65, 25.0F, true), //
                Arrays.asList(new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16,
                        1, 2, 8000.0F, true),//
                        new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                                8000.0F, true)//
                        ), //
                Arrays
                        .asList(//
                                new AudioFormat(TOAST_GSM_ENCODING, 8000.0F,
                                        AudioSystem.NOT_SPECIFIED, 1, 33,
                                        50.0F, false),//
                                new AudioFormat(TOAST_GSM_ENCODING, 8000.0F,
                                        AudioSystem.NOT_SPECIFIED, 1, 33,
                                        50.0F, true),//
                                new AudioFormat(MS_GSM_ENCODING, 8000.0F,
                                        AudioSystem.NOT_SPECIFIED, 1, ALL, ALL,
                                        false),//
                                new AudioFormat(MS_GSM_ENCODING, 8000.0F,
                                        AudioSystem.NOT_SPECIFIED, 1, ALL, ALL,
                                        true)//
                        )//
        );
    }

    /**
     * Test for
     * {@link FormatConversionProvider#isConversionSupported(AudioFormat, AudioFormat)}
     * .
     */
    @Test
    public void testIsConversionSupportedAudioFormatAudioFormat()
    {
        testIsConversionSupportedAudioFormatAudioFormat(//
                new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, 16, 1, 33, 50.0F,
                        true), //
                Arrays.asList(//
                        new AudioFormat(Encoding.PCM_SIGNED, _8KHZ, 16, 1, 2,
                                _8KHZ, true), //
                        new AudioFormat(Encoding.PCM_SIGNED, _8KHZ, 16, 1, 2,
                                _8KHZ, false) //
                        ), //
                Arrays.asList(//
                        new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, ALL, 1,
                                33, 50.0F, true), //
                        new AudioFormat(MS_GSM_ENCODING, 8000.0F, ALL, 1, 33,
                                50.0F, true),//
                        new AudioFormat(Encoding.PCM_UNSIGNED, _8KHZ, 16, 1, 2,
                                _8KHZ, true), //
                        new AudioFormat(Encoding.ALAW, _8KHZ, 16, 1, 2, _8KHZ,
                                true), //
                        new AudioFormat(Encoding.ULAW, _8KHZ, 16, 1, 2, _8KHZ,
                                true)//
                        )//
        );
        testIsConversionSupportedAudioFormatAudioFormat(//
                new AudioFormat(MS_GSM_ENCODING, 8000.0F, 16, 1, 65, 25.0F,
                        true), //
                Arrays.asList(//
                        new AudioFormat(Encoding.PCM_SIGNED, _8KHZ, 16, 1, 2,
                                _8KHZ, true), //
                        new AudioFormat(Encoding.PCM_SIGNED, _8KHZ, 16, 1, 2,
                                _8KHZ, false) //
                        ), //
                Arrays.asList(//
                        new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, ALL, 1,
                                33, 50.0F, true), //
                        new AudioFormat(MS_GSM_ENCODING, 8000.0F, ALL, 1, 33,
                                50.0F, true),//
                        new AudioFormat(Encoding.PCM_UNSIGNED, _8KHZ, 16, 1, 2,
                                _8KHZ, true), //
                        new AudioFormat(Encoding.ALAW, _8KHZ, 16, 1, 2, _8KHZ,
                                true), //
                        new AudioFormat(Encoding.ULAW, _8KHZ, 16, 1, 2, _8KHZ,
                                true)//
                        )//
        );
        testIsConversionSupportedAudioFormatAudioFormat(//
                new AudioFormat(Encodings.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, true), //
                EMPTY_AUDIOFORMAT_LIST, //
                Arrays.asList(//
                        new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, ALL, 1,
                                33, 50.0F, true), //
                        new AudioFormat(MS_GSM_ENCODING, 8000.0F, ALL, 1, 33,
                                50.0F, true),//
                        new AudioFormat(Encoding.PCM_SIGNED, _8KHZ, 16, 1, 2,
                                _8KHZ, true), //
                        new AudioFormat(Encoding.PCM_UNSIGNED, _8KHZ, 16, 1, 2,
                                _8KHZ, true), //
                        new AudioFormat(Encoding.ALAW, _8KHZ, 16, 1, 2, _8KHZ,
                                true), //
                        new AudioFormat(Encoding.ULAW, _8KHZ, 16, 1, 2, _8KHZ,
                                true)//
                        )//
        );
    }

    /**
     * Test for
     * {@link FormatConversionProvider#getAudioInputStream(Encoding, AudioInputStream)}
     * .
     */
    @Test
    public void testGetAudioInputStreamEncodingAudioInputStream()
    {
        testGetAudioInputStreamEncoding(Encoding.PCM_SIGNED, new AudioFormat(
                TOAST_GSM_ENCODING, 8000.0F, ALL, 1, 33, 50, true), //
                NOT_SPECIFIED,//
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false), //
                NOT_SPECIFIED);
        testGetAudioInputStreamEncoding(Encoding.PCM_SIGNED, new AudioFormat(
                TOAST_GSM_ENCODING, 8000.0F, ALL, 1, 33, 50, true), //
                134,//
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false), //
                160 * 134);
        testGetAudioInputStreamEncoding(Encoding.PCM_SIGNED, new AudioFormat(
                MS_GSM_ENCODING, 8000.0F, ALL, 1, 65, 25, true), //
                NOT_SPECIFIED,//
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false), //
                NOT_SPECIFIED);
        testGetAudioInputStreamEncoding(Encoding.PCM_SIGNED, new AudioFormat(
                MS_GSM_ENCODING, 8000.0F, ALL, 1, 65, 25, true), //
                134,//
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false), //
                320 * 134);
    }

    /**
     * Test for
     * {@link FormatConversionProvider#getAudioInputStream(AudioFormat, AudioInputStream)}
     * .
     */
    @Test
    public void testGetAudioInputStreamAudioFormatAudioInputStream()
    {
        testGetAudioInputStreamAudioFormat(//
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false), //
                new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, ALL, 1, 33, 50,
                        true), //
                NOT_SPECIFIED, //
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false), //
                NOT_SPECIFIED);
        testGetAudioInputStreamAudioFormat(//
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false), //
                new AudioFormat(TOAST_GSM_ENCODING, 8000.0F, ALL, 1, 33, 50,
                        true), //
                134, //
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false), //
                160 * 134);
        testGetAudioInputStreamAudioFormat(
                //
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false),//
                new AudioFormat(MS_GSM_ENCODING, 8000.0F, ALL, 1, 65, 25, true), //
                NOT_SPECIFIED,//
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false), //
                NOT_SPECIFIED);
        testGetAudioInputStreamAudioFormat(
                //
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false),//
                new AudioFormat(MS_GSM_ENCODING, 8000.0F, ALL, 1, 65, 25, true), //
                134,//
                new AudioFormat(Encoding.PCM_SIGNED, 8000.0F, 16, 1, 2,
                        8000.0F, false), //
                320 * 134);
    }
}
