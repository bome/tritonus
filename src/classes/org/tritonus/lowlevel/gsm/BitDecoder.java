package org.tritonus.lowlevel.gsm;

public class BitDecoder
{
    public static enum AllocationMode
    {
        MSBitFirst, LSBitFirst;
    }

    private AllocationMode allocationMode;
    /**
     * The following variables are for exploding Microsoft frames. See
     * {@link #explodeFrameMicrosoft(byte[], int, boolean)}.
     */

    // TODO: (GSM) should be reverted to private once transition is finished
    public byte[] m_codedFrame;
    public int m_codedFrameByteIndex;
    private int m_sr;
    private int m_currentBits;

    /**
     * Constructor.
     * 
     * @param codedBytes
     * @param allocationMode
     */
    public BitDecoder(byte[] codedBytes, int bufferStartIndex,
            AllocationMode allocationMode)
    {
        super();
        this.allocationMode = allocationMode;
        m_codedFrame = codedBytes;
        m_codedFrameByteIndex = bufferStartIndex;
        m_sr = 0;
        m_currentBits = 0;
    }

    public final void getNextCodedByteValue()
    {
        m_sr |= getNextCodedByteValue2() << m_currentBits;
        m_currentBits += 8;
    }

    private final int getNextCodedByteValue2()
    {
        int value = m_codedFrame[m_codedFrameByteIndex];
        m_codedFrameByteIndex++;
        return value & 0xFF;
    }

    public final int getNextBits(int bits)
    {
        int value = m_sr & Gsm_Def.BITMASKS[bits];
        m_sr >>>= bits;
        m_currentBits -= bits;
        return value;
    }

    public final void addBits(int value, int numBits)
    {
        // while (numBits > 0)
        // {
        // if (byteIndex >= codedBytes.length)
        // {
        // throw new RuntimeException("No more bytes in coded bytes array");
        // }
        // int bits = Math.min(numBits, remainingBitsInCurrentByte);
        // int nextRemainingBits = remainingBitsInCurrentByte - bits;
        // int nextNumBits = numBits - bits;
        // int x;
        // switch (allocationMode)
        // {
        // case LSBitFirst:
        // x = (((value) & Gsm_Def.BITMASKS[bits]) << (8 -
        // remainingBitsInCurrentByte));
        // codedBytes[byteIndex] |= x;
        // value >>>= bits;
        // break;
        // case MSBitFirst:
        // x = (((value >>> nextNumBits) & Gsm_Def.BITMASKS[bits]) <<
        // nextRemainingBits);
        // codedBytes[byteIndex] |= x;
        // break;
        // }
        // remainingBitsInCurrentByte = nextRemainingBits;
        // if (remainingBitsInCurrentByte == 0)
        // {
        // byteIndex++;
        // remainingBitsInCurrentByte = 8;
        // }
        // numBits = nextNumBits;
        // }
    }

    public int getSr()
    {
        return m_sr;
    }

    public void setSr(int sr)
    {
        m_sr = sr;
    }
}
