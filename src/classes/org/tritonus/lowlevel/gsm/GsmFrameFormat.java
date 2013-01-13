package org.tritonus.lowlevel.gsm;

/**
 * Format of the coded GSM frame.
 * 
 * <p>
 * There are two formats incompatible with each other:
 * </p>
 * <ol>
 * 
 * <li>The format defined by Jutta Degner and Carsten Bormann at Technische
 * Universitaet Berlin ("toast" format). It has a leading 4 bit magic number, so
 * that the frame has 33 bytes. The frame rate is 50 frames per second. Bits
 * vectors for coefficients inside bytes are allocated MSB first.</li>
 * 
 * </li>The format defined by Microsoft as "ms-gsm". Universitaet Berlin. It
 * packs two native GSM frames into one frame, so that the frame has 65 bytes.
 * The frame rate is 25 frames per second. Bits vectors for coefficients inside
 * bytes are allocated LSB first.</li>
 * </ol>
 * 
 * @author Matthias Pfisterer
 * 
 */
public enum GsmFrameFormat
{
    /**
     * 33 byte frames, 50 frames per second. 4 bits leading magic number. Bits
     * vectors for coefficients inside bytes are allocated MSB first
     */
    TOAST(GsmConstants.TOAST_ENCODED_GSM_FRAME_SIZE,
            GsmConstants.TOAST_SAMPLES_PER_FRAME),
    /**
     * 65 byte frames, 25 frames per second. Bits vectors for coefficients
     * inside bytes are allocated LSB first.
     */
    MICROSOFT(GsmConstants.MICROSOFT_ENCODED_GSM_FRAME_SIZE,
            GsmConstants.MICROSOFT_SAMPLES_PER_FRAME);

    private final int frameSizeInBytes;
    private final int samplesPerFrame;

    private GsmFrameFormat(int frameSizeInBytes, int samplesPerFrame)
    {
        this.frameSizeInBytes = frameSizeInBytes;
        this.samplesPerFrame = samplesPerFrame;
    }

    /**
     * Obtains the size of an encoded frame.
     * 
     * @return the size of the encoded frame in bytes
     */
    public final int getFrameSize()
    {
        return frameSizeInBytes;
    }

    /**
     * Obtains the number of decoded samples per GSM frame.
     * 
     * @return the number of decoded samples
     */
    public final int getSamplesPerFrame()
    {
        return samplesPerFrame;
    }
}
