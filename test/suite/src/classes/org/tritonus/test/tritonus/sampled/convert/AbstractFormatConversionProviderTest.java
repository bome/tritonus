package org.tritonus.test.tritonus.sampled.convert;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.spi.FormatConversionProvider;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

/**
 * Base class for test cases of {@link FormatConversionProvider}.
 * 
 * @author Matthias Pfisterer
 * 
 */
public abstract class AbstractFormatConversionProviderTest
{
    /**
     * Empty list of {@link Encoding}.
     */
    protected static final List<Encoding> EMPTY_ENCODING_LIST = Collections
            .unmodifiableList(new ArrayList<Encoding>());
    /**
     * Empty list of {@link AudioFormat}.
     */
    protected static final List<AudioFormat> EMPTY_AUDIOFORMAT_LIST = Collections
            .unmodifiableList(new ArrayList<AudioFormat>());

    protected static final int ALL = AudioSystem.NOT_SPECIFIED;

    /**
     * Obtains the {@link FormatConversionProvider} to be tested.
     * 
     * <p>
     * This method is used by all test methods in this class to obtain the
     * tested provider. It should also be used by subclasses so there is a
     * unique way to access the tested provider.
     * </p>
     * 
     * @return an instance of {@link FormatConversionProvider}
     */
    protected abstract FormatConversionProvider getFormatConversionProvider();

    /**
     * Tests <code>getSourceEncodings()</code> or
     * <code>getTargetEncodings()</code>.
     * 
     * <p>
     * This method does:
     * </p>
     * <ol>
     * <li>Calls the method to test</li>
     * <li>Checks if the resulted array contains any of the Encodings that are
     * expected to be supported</li>
     * <li>Checks if the resulted array contains none of the Encodings that are
     * expected to be unsupported</li>
     * </ol>
     * 
     * @param testTarget
     *            if <code>true</code>
     *            {@link FormatConversionProvider#getTargetEncodings()} is
     *            tested, if <code>false</code>
     *            {@link FormatConversionProvider#getSourceEncodings()} is
     *            tested
     * @param expectedSupportedEncodings
     *            list of {@link Encoding} instances that are expected to be
     *            supported
     * @param expectedUnsupportedEncodings
     *            list of {@link Encoding} instances that are expected to be
     *            unsupported
     * @throws AssertionFailedError
     *             if an expected supported encoding is not in the list of
     *             actual encodings or if an expected unsupported encoding is in
     *             the list of actual encodings
     * 
     * @see FormatConversionProvider#getSourceEncodings()
     * @see FormatConversionProvider#getTargetEncodings()
     * @see #checkSupportedEncodingsList(String, List, List, Encoding[])
     */
    protected void testGetEncodings(boolean testTarget,
            List<Encoding> expectedSupportedEncodings,
            List<Encoding> expectedUnsupportedEncodings)
    {
        Encoding[] actualSupportedEncodings = testTarget ? getFormatConversionProvider()
                .getTargetEncodings()
                : getFormatConversionProvider().getSourceEncodings();
        checkSupportedEncodingsList(testTarget ? "getTargetEncodings(): "
                : "getSourceEncodings(): ", expectedSupportedEncodings,
                expectedUnsupportedEncodings, actualSupportedEncodings);
    }

    /**
     * Checks a list of actual supported encodings against expected supported
     * and unsupported encodings.
     * 
     * @param messagePrefix
     *            prefix for error messages when an AssertionFailedError is
     *            thrown
     * @param expectedSupportedEncodings
     *            list of {@link Encoding} instances that are expected to be
     *            supported
     * @param expectedUnsupportedEncodings
     *            list of {@link Encoding} instances that are expected to be
     *            unsupported
     * @param actualSupportedEncodings
     *            array of actual supported {@link Encoding} instances
     * @throws AssertionFailedError
     *             if an expected supported encoding is not in the list of
     *             actual encodings or if an expected unsupported encoding is in
     *             the list of actual encodings
     */
    protected void checkSupportedEncodingsList(String messagePrefix,
            List<Encoding> expectedSupportedEncodings,
            List<Encoding> expectedUnsupportedEncodings,
            Encoding[] actualSupportedEncodings)
    {
        Collection<Encoding> actualSupportedEncodingsCollection = Arrays
                .asList(actualSupportedEncodings);
        for (Encoding expectedSupportedEncoding : expectedSupportedEncodings)
        {
            assertEquals(messagePrefix + expectedSupportedEncoding, true,
                    actualSupportedEncodingsCollection
                            .contains(expectedSupportedEncoding));
        }
        for (Encoding expectedUnsupportedEncoding : expectedUnsupportedEncodings)
        {
            assertEquals(messagePrefix + expectedUnsupportedEncoding, false,
                    actualSupportedEncodingsCollection
                            .contains(expectedUnsupportedEncoding));
        }
    }

    /**
     * Checks a list of actual supported AudioFormat instances against expected
     * supported and unsupported AudioFormat instances.
     * 
     * @param messagePrefix
     *            prefix for error messages when an AssertionFailedError is
     *            thrown
     * @param expectedSupportedAudioFormats
     *            list of {@link Encoding} instances that are expected to be
     *            supported
     * @param expectedUnsupportedAudioFormats
     *            list of AudioFormat instances that are expected to be
     *            unsupported
     * @param actualSupportedAudioFormats
     *            array of actual supported AudioFormat instances
     * @throws AssertionFailedError
     *             if an expected supported AudioFormat is not in the list of
     *             actual AudioFormat instances or if an expected unsupported
     *             AudioFormat is in the list of actual AudioFormat instances
     */
    protected void checkSupportedAudioFormatsList(String messagePrefix,
            List<AudioFormat> expectedSupportedAudioFormats,
            List<AudioFormat> expectedUnsupportedAudioFormats,
            AudioFormat[] actualSupportedAudioFormats)
    {
        Collection<AudioFormat> actualSupportedEncodingsCollection = Arrays
                .asList(actualSupportedAudioFormats);
        for (AudioFormat expectedSupportedAudioFormat : expectedSupportedAudioFormats)
        {
            assertEquals(messagePrefix + expectedSupportedAudioFormat, true,
                    contains(actualSupportedEncodingsCollection,
                            expectedSupportedAudioFormat));
        }
        for (AudioFormat expectedUnsupportedAudioFormat : expectedUnsupportedAudioFormats)
        {
            assertEquals(messagePrefix + expectedUnsupportedAudioFormat, false,
                    contains(actualSupportedEncodingsCollection,
                            expectedUnsupportedAudioFormat));
        }
    }

    /**
     * Tests <code>isSourceEncodingSupported()</code> or
     * <code>isTargetEncodingSupported()</code>.
     * 
     * <p>
     * This method does:
     * </p>
     * <ol>
     * <li>For any of the encodings that are expected to be supported calls the
     * tested method expecting it to return <code>true</code></li>
     * <li>For any of the encodings that are expected to be unsupported calls
     * the tested method expecting it to return <code>false</code></li>
     * </ol>
     * 
     * @param testTarget
     *            if <code>true</code>
     *            {@link FormatConversionProvider#isTargetEncodingSupported(Encoding)}
     *            is tested, if <code>false</code>
     *            {@link FormatConversionProvider#isSourceEncodingSupported(Encoding)}
     *            is tested
     * @param expectedSupportedEncodings
     *            list of {@link Encoding} instances that are expected to be
     *            supported
     * @param expectedUnsupportedEncodings
     *            list of {@link Encoding} instances that are expected to be
     *            unsupported
     * @throws AssertionFailedError
     *             if an encoding expected to be supported is actually
     *             unsupported or if an encoding expected to be unsupported is
     *             actually supported
     * 
     * @see FormatConversionProvider#isSourceEncodingSupported(Encoding)
     * @see FormatConversionProvider#isTargetEncodingSupported(Encoding)
     * @see #testIsEncodingSupportedEncoding(boolean, boolean, Encoding)
     */
    protected void testIsEncodingSupportedEncoding(boolean testTarget,
            List<Encoding> expectedSupportedEncodings,
            List<Encoding> expectedUnsupportedEncodings)
    {
        for (Encoding encoding : expectedSupportedEncodings)
        {
            testIsEncodingSupportedEncoding(testTarget, true, encoding);
        }
        for (Encoding encoding : expectedUnsupportedEncodings)
        {
            testIsEncodingSupportedEncoding(testTarget, false, encoding);
        }
    }

    /**
     * Tests <code>isSourceEncodingSupported()</code> or
     * <code>isTargetEncodingSupported()</code>.
     * 
     * <p>
     * This method calls the tested method for the passed encoding and checks
     * the return value.
     * </p>
     * 
     * @param testTarget
     *            if <code>true</code>
     *            {@link FormatConversionProvider#isTargetEncodingSupported(Encoding)}
     *            is tested, if <code>false</code>
     *            {@link FormatConversionProvider#isSourceEncodingSupported(Encoding)}
     *            is tested
     * @param expectedSupported
     *            <code>true</code> if the encoding is expected to be supported,
     *            <code>false</code> if the encoding is expected to be
     *            unsupported
     * @param encoding
     *            the {@link Encoding} to be tested
     * @throws AssertionFailedError
     *             if the return value of the tested method does not match the
     *             expected one
     * 
     * @see FormatConversionProvider#isSourceEncodingSupported(Encoding)
     * @see FormatConversionProvider#isTargetEncodingSupported(Encoding)
     */
    protected void testIsEncodingSupportedEncoding(boolean testTarget,
            boolean expectedSupported, Encoding encoding)
    {
        boolean actualSupported = testTarget ? getFormatConversionProvider()
                .isTargetEncodingSupported(encoding)
                : getFormatConversionProvider().isSourceEncodingSupported(
                        encoding);
        assertEquals(testTarget ? "isTargetEncodingSupported(): "
                : "isSourceEncodingSupported(): " + encoding,
                expectedSupported, actualSupported);
    }

    /**
     * Tests <code>getTargetEncodings(AudioFormat)</code>.
     * 
     * <p>
     * This method does:
     * </p>
     * <ol>
     * <li>Calls the method to test</li>
     * <li>Checks if the resulted array contains any of the Encodings that are
     * expected to be supported</li>
     * <li>Checks if the resulted array contains none of the Encodings that are
     * expected to be unsupported</li>
     * </ol>
     * 
     * @param sourceAudioFormat
     *            the source AudioFormat to pass to the tested method
     * @param expectedSupportedEncodings
     *            list of {@link Encoding} instances that are expected to be
     *            supported
     * @param expectedUnsupportedEncodings
     *            list of {@link Encoding} instances that are expected to be
     *            unsupported
     * @throws AssertionFailedError
     *             if an expected supported encoding is not in the list of
     *             actual encodings or if an expected unsupported encoding is in
     *             the list of actual encodings
     * 
     * @see FormatConversionProvider#getTargetEncodings(AudioFormat)
     * @see #checkSupportedEncodingsList(String, List, List, Encoding[])
     */
    protected void testGetTargetEncodingsAudioFormat(
            AudioFormat sourceAudioFormat,
            List<Encoding> expectedSupportedEncodings,
            List<Encoding> expectedUnsupportedEncodings)
    {
        Encoding[] actualSupportedEncodings = getFormatConversionProvider()
                .getTargetEncodings(sourceAudioFormat);
        checkSupportedEncodingsList("getTargetEncodings(AudioFormat): ",
                expectedSupportedEncodings, expectedUnsupportedEncodings,
                actualSupportedEncodings);
    }

    /**
     * Tests <code>isConversionSupported(Encoding, AudioFormat)</code>.
     * 
     * <p>
     * This method does:
     * </p>
     * <ol>
     * <li>For any of the encodings that are expected to be supported calls the
     * tested method expecting it to return <code>true</code></li>
     * <li>For any of the encodings that are expected to be unsupported calls
     * the tested method expecting it to return <code>false</code></li>
     * </ol>
     * 
     * @param sourceAudioFormat
     *            the source AudioFormat to pass to the tested method
     * @param expectedSupportedEncodings
     *            list of {@link Encoding} instances that are expected to be
     *            supported
     * @param expectedUnsupportedEncodings
     *            list of {@link Encoding} instances that are expected to be
     *            unsupported
     * @throws AssertionFailedError
     *             if an encoding expected to be supported is actually
     *             unsupported or if an encoding expected to be unsupported is
     *             actually supported
     * 
     * @see FormatConversionProvider#isConversionSupported(Encoding,
     *      AudioFormat)
     * @see #testIsConversionSupportedEncodingAudioFormat(AudioFormat, boolean,
     *      Encoding)
     */
    protected void testIsConversionSupportedEncodingAudioFormat(
            AudioFormat sourceAudioFormat,
            List<Encoding> expectedSupportedEncodings,
            List<Encoding> expectedUnsupportedEncodings)
    {
        for (Encoding encoding : expectedSupportedEncodings)
        {
            testIsConversionSupportedEncodingAudioFormat(sourceAudioFormat,
                    true, encoding);
        }
        for (Encoding encoding : expectedUnsupportedEncodings)
        {
            testIsConversionSupportedEncodingAudioFormat(sourceAudioFormat,
                    false, encoding);
        }
    }

    /**
     * Tests <code>isConversionSupported(Encoding, AudioFormat)</code>.
     * 
     * <p>
     * This method calls the tested method for the passed encoding and checks
     * the return value.
     * </p>
     * 
     * @param sourceAudioFormat
     *            the source AudioFormat to pass to the tested method
     * @param expectedSupported
     *            <code>true</code> if the encoding is expected to be supported,
     *            <code>false</code> if the encoding is expected to be
     *            unsupported
     * @param encoding
     *            the {@link Encoding} to be tested
     * @throws AssertionFailedError
     *             if the return value of the tested method does not match the
     *             expected one
     * 
     * @see FormatConversionProvider#isConversionSupported(Encoding,
     *      AudioFormat)
     */
    protected void testIsConversionSupportedEncodingAudioFormat(
            AudioFormat sourceAudioFormat, boolean expectedSupported,
            Encoding encoding)
    {
        boolean actualSupported = getFormatConversionProvider()
                .isConversionSupported(encoding, sourceAudioFormat);
        assertEquals("isConversionSupported(Encoding, AudioFormat): "
                + encoding, expectedSupported, actualSupported);
    }

    /**
     * Tests <code>getTargetFormats(Encoding, AudioFormat)</code>.
     * 
     * <p>
     * This method does:
     * </p>
     * <ol>
     * <li>Calls the method to test</li>
     * <li>Checks if the resulted array contains any of the Encodings that are
     * expected to be supported</li>
     * <li>Checks if the resulted array contains none of the Encodings that are
     * expected to be unsupported</li>
     * </ol>
     * 
     * @param sourceAudioFormat
     *            the source AudioFormat to pass to the tested method
     * @param expectedSupportedAudioFormats
     *            list of {@link Encoding} instances that are expected to be
     *            supported
     * @param expectedUnsupportedAudioFormats
     *            list of {@link Encoding} instances that are expected to be
     *            unsupported
     * @throws AssertionFailedError
     *             if an expected supported encoding is not in the list of
     *             actual encodings or if an expected unsupported encoding is in
     *             the list of actual encodings
     * 
     * @see FormatConversionProvider#getTargetFormats(Encoding, AudioFormat)
     * @see #checkSupportedEncodingsList(String, List, List, Encoding[])
     */
    protected void testGetTargetFormatsEncodingAudioFormat(
            Encoding targetEncoding, AudioFormat sourceAudioFormat,
            List<AudioFormat> expectedSupportedAudioFormats,
            List<AudioFormat> expectedUnsupportedAudioFormats)
    {
        AudioFormat[] actualSupportedAudioFormats = getFormatConversionProvider()
                .getTargetFormats(targetEncoding, sourceAudioFormat);
        checkSupportedAudioFormatsList(
                "getTargetFormats(Encoding, AudioFormat): ",
                expectedSupportedAudioFormats, expectedUnsupportedAudioFormats,
                actualSupportedAudioFormats);
    }

    /**
     * Tests <code>isConversionSupported(AudioFormat, AudioFormat)</code>.
     * 
     * <p>
     * This method does:
     * </p>
     * <ol>
     * <li>For any of the AudioFormat instances that are expected to be
     * supported calls the tested method expecting it to return
     * <code>true</code></li>
     * <li>For any of the AudioFormats that are expected to be unsupported calls
     * the tested method expecting it to return <code>false</code></li>
     * </ol>
     * 
     * @param sourceAudioFormat
     *            the source AudioFormat to pass to the tested method
     * @param expectedSupportedAudioFormats
     *            list of {@link AudioFormat} instances that are expected to be
     *            supported
     * @param expectedUnsupportedAudioFormats
     *            list of {@link AudioFormat} instances that are expected to be
     *            unsupported
     * @throws AssertionFailedError
     *             if an AudioFormat expected to be supported is actually
     *             unsupported or if an AudioFormat expected to be unsupported
     *             is actually supported
     * 
     * @see FormatConversionProvider#isConversionSupported(AudioFormat,
     *      AudioFormat)
     * @see #testIsConversionSupportedAudioFormatAudioFormat(AudioFormat,
     *      boolean, AudioFormat)
     */
    protected void testIsConversionSupportedAudioFormatAudioFormat(
            AudioFormat sourceAudioFormat,
            List<AudioFormat> expectedSupportedAudioFormats,
            List<AudioFormat> expectedUnsupportedAudioFormats)
    {
        for (AudioFormat audioFormat : expectedSupportedAudioFormats)
        {
            testIsConversionSupportedAudioFormatAudioFormat(sourceAudioFormat,
                    true, audioFormat);
        }
        for (AudioFormat audioFormat : expectedUnsupportedAudioFormats)
        {
            testIsConversionSupportedAudioFormatAudioFormat(sourceAudioFormat,
                    false, audioFormat);
        }
    }

    /**
     * Tests <code>isConversionSupported(AudioFormat, AudioFormat)</code>.
     * 
     * <p>
     * This method calls the tested method for the passed encoding and checks
     * the return value.
     * </p>
     * 
     * @param sourceAudioFormat
     *            the source AudioFormat to pass to the tested method
     * @param expectedSupported
     *            <code>true</code> if the AudioFormat is expected to be
     *            supported, <code>false</code> if the AudioFormat is expected
     *            to be unsupported
     * @param targetAudioFormat
     *            the {@link AudioFormat} to be tested
     * @throws AssertionFailedError
     *             if the return value of the tested method does not match the
     *             expected one
     * 
     * @see FormatConversionProvider#isConversionSupported(AudioFormat,
     *      AudioFormat)
     */
    private void testIsConversionSupportedAudioFormatAudioFormat(
            AudioFormat sourceAudioFormat, boolean expectedSupported,
            AudioFormat targetAudioFormat)
    {
        boolean actualSupported = getFormatConversionProvider()
                .isConversionSupported(targetAudioFormat, sourceAudioFormat);
        assertEquals("isConversionSupported(AudioFormat, AudioFormat): "
                + targetAudioFormat, expectedSupported, actualSupported);
    }

    /**
     * Tests <code>getAudioInputStream(Encoding, AudioInputStream)</code>.
     * 
     * <p>
     * This method does:
     * </p>
     * <ol>
     * <li>create and AudioInputStream to be used as source stream with
     * <code>null</code> as data stream and the passed AudioFormat and frame
     * length</li>
     * <li>Calls the tested method passing the created AudioInputStream and the
     * target encoding</li>
     * <li>Checks the AudioFormat and the frame length of the resulting
     * AudioInputStream</li>
     * </ol>
     * 
     * @param targetEncoding
     *            the target Encoding to pass to the tested method
     * @param sourceFormat
     *            the AudioFormat of the source AudioInputStream
     * @param sourceFrameLength
     *            the frame length of the source AudioInputStream (can be
     *            <code>AudioSystem.NOT_SPECIFIED</code>)
     * @param expectedTargetAudioFormat
     *            the expected AudioFormat of the resulting AudioInputStream
     * @param expectedTargetFrameLength
     *            the expected frame length of the resulting AudioInputStream
     *            (can be <code>AudioSystem.NOT_SPECIFIED</code>)
     * @throws AssertionFailedError
     *             if the AudioFormat or the frame length of the resulting
     *             AudioInputStream do not match the expected values
     * 
     * @see FormatConversionProvider#getAudioInputStream(Encoding,
     *      AudioInputStream)
     */
    protected void testGetAudioInputStreamEncoding(Encoding targetEncoding,
            AudioFormat sourceFormat, int sourceFrameLength,
            AudioFormat expectedTargetAudioFormat, int expectedTargetFrameLength)
    {
        AudioInputStream sourceStream = new AudioInputStream(null,
                sourceFormat, sourceFrameLength);
        AudioInputStream actualAudioInputStream = getFormatConversionProvider()
                .getAudioInputStream(targetEncoding, sourceStream);
        checkAudioInputStream(actualAudioInputStream,
                expectedTargetAudioFormat, expectedTargetFrameLength);
    }

    /**
     * Tests <code>getAudioInputStream(Encoding, AudioInputStream)</code>.
     * 
     * <p>
     * This method does:
     * </p>
     * <ol>
     * <li>create and AudioInputStream to be used as source stream with
     * <code>null</code> as data stream and the passed AudioFormat and frame
     * length</li>
     * <li>Calls the tested method passing the created AudioInputStream and the
     * target encoding</li>
     * <li>Checks the AudioFormat and the frame length of the resulting
     * AudioInputStream</li>
     * </ol>
     * 
     * @param targetEncoding
     *            the target Encoding to pass to the tested method
     * @param sourceFormat
     *            the AudioFormat of the source AudioInputStream
     * @param sourceFrameLength
     *            the frame length of the source AudioInputStream (can be
     *            <code>AudioSystem.NOT_SPECIFIED</code>)
     * @param expectedTargetAudioFormat
     *            the expected AudioFormat of the resulting AudioInputStream
     * @param expectedTargetFrameLength
     *            the expected frame length of the resulting AudioInputStream
     *            (can be <code>AudioSystem.NOT_SPECIFIED</code>)
     * @throws AssertionFailedError
     *             if the AudioFormat or the frame length of the resulting
     *             AudioInputStream do not match the expected values
     * 
     * @see FormatConversionProvider#getAudioInputStream(Encoding,
     *      AudioInputStream)
     */
    protected void testGetAudioInputStreamEncodingUnsupported(
            Encoding targetEncoding, AudioFormat sourceFormat,
            int sourceFrameLength)
    {
        AudioInputStream sourceStream = new AudioInputStream(null,
                sourceFormat, sourceFrameLength);
        try
        {
            getFormatConversionProvider().getAudioInputStream(targetEncoding,
                    sourceStream);
            Assert.fail("Expecting IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // DO NOTHING
        }
    }

    /**
     * Tests <code>getAudioInputStream(AudioFormat, AudioInputStream)</code>.
     * 
     * <p>
     * This method does:
     * </p>
     * <ol>
     * <li>create and AudioInputStream to be used as source stream with
     * <code>null</code> as data stream and the passed AudioFormat and frame
     * length</li>
     * <li>Calls the tested method passing the created AudioInputStream and the
     * target AudioFormat</li>
     * <li>Checks the AudioFormat and the frame length of the resulting
     * AudioInputStream</li>
     * </ol>
     * 
     * @param targetAudioFormat
     *            the target AudioFormat to pass to the tested method
     * @param sourceFormat
     *            the AudioFormat of the source AudioInputStream
     * @param sourceFrameLength
     *            the frame length of the source AudioInputStream (can be
     *            <code>AudioSystem.NOT_SPECIFIED</code>)
     * @param expectedTargetAudioFormat
     *            the expected AudioFormat of the resulting AudioInputStream
     * @param expectedTargetFrameLength
     *            the expected frame length of the resulting AudioInputStream
     *            (can be <code>AudioSystem.NOT_SPECIFIED</code>)
     * @throws AssertionFailedError
     *             if the AudioFormat or the frame length of the resulting
     *             AudioInputStream do not match the expected values
     * 
     * @see FormatConversionProvider#getAudioInputStream(AudioFormat,
     *      AudioInputStream)
     */
    protected void testGetAudioInputStreamAudioFormat(
            AudioFormat targetAudioFormat, AudioFormat sourceFormat,
            int sourceFrameLength, AudioFormat expectedTargetAudioFormat,
            int expectedTargetFrameLength)
    {
        AudioInputStream sourceStream = new AudioInputStream(null,
                sourceFormat, sourceFrameLength);
        AudioInputStream actualAudioInputStream = getFormatConversionProvider()
                .getAudioInputStream(targetAudioFormat, sourceStream);
        checkAudioInputStream(actualAudioInputStream,
                expectedTargetAudioFormat, expectedTargetFrameLength);
    }

    /**
     * Tests <code>getAudioInputStream(AudioFormat, AudioInputStream)</code>.
     * 
     * <p>
     * This method does:
     * </p>
     * <ol>
     * <li>create and AudioInputStream to be used as source stream with
     * <code>null</code> as data stream and the passed AudioFormat and frame
     * length</li>
     * <li>Calls the tested method passing the created AudioInputStream and the
     * target AudioFormat</li>
     * <li>Checks the AudioFormat and the frame length of the resulting
     * AudioInputStream</li>
     * </ol>
     * 
     * @param targetAudioFormat
     *            the target AudioFormat to pass to the tested method
     * @param sourceFormat
     *            the AudioFormat of the source AudioInputStream
     * @param sourceFrameLength
     *            the frame length of the source AudioInputStream (can be
     *            <code>AudioSystem.NOT_SPECIFIED</code>)
     * @param expectedTargetAudioFormat
     *            the expected AudioFormat of the resulting AudioInputStream
     * @param expectedTargetFrameLength
     *            the expected frame length of the resulting AudioInputStream
     *            (can be <code>AudioSystem.NOT_SPECIFIED</code>)
     * @throws AssertionFailedError
     *             if the AudioFormat or the frame length of the resulting
     *             AudioInputStream do not match the expected values
     * 
     * @see FormatConversionProvider#getAudioInputStream(AudioFormat,
     *      AudioInputStream)
     */
    protected void testGetAudioInputStreamAudioFormatUnsupported(
            AudioFormat targetAudioFormat, AudioFormat sourceFormat,
            int sourceFrameLength)
    {
        AudioInputStream sourceStream = new AudioInputStream(null,
                sourceFormat, sourceFrameLength);
        try
        {
            getFormatConversionProvider().getAudioInputStream(
                    targetAudioFormat, sourceStream);
            Assert.fail("Expecting IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // DO NOTHING
        }
    }

    /**
     * Checks format and frame length of an AudioInputStream.
     * 
     * @param actualAudioInputStream
     *            the AudioInputStream to check
     * @param expectedAudioFormat
     *            the expected AudioFormat
     * @param expectedFrameLength
     *            the expected frame length (can be
     *            <code>AudioSystem.NOT_SPECIFIED</code>)
     * @throws AssertionFailedError
     *             if the AudioFormat or the frame length of the
     *             AudioInputStream do not match the expected values
     */
    private void checkAudioInputStream(AudioInputStream actualAudioInputStream,
            AudioFormat expectedAudioFormat, long expectedFrameLength)
    {
        assertEquals("AudioInputStream.getFormat()", true, expectedAudioFormat
                .matches(actualAudioInputStream.getFormat()));
        assertEquals("AudioInputStream.getFrameLength()", expectedFrameLength,
                actualAudioInputStream.getFrameLength());
    }

    /**
     * Checks if an AudioFormat is contained in a list of AudioFormat instances.
     * 
     * 
     * @param audioFormats
     * @param audioFormat
     * @return
     */
    private static boolean contains(Collection<AudioFormat> audioFormats,
            AudioFormat audioFormat)
    {
        for (AudioFormat format : audioFormats)
        {
            if (format.matches(audioFormat))
            {
                return true;
            }
        }
        return false;
    }
}
