package org.tritonus.lowlevel.gsm;

public class BitDecoder
{
    public static enum AllocationMode
    {
        MSBitFirst, LSBitFirst;
    }

    private AllocationMode allocationMode;

    private byte[] m_codedFrame;
    private int m_codedFrameByteIndex;
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

    public void setCodedFrame(byte[] c, final int bufferStartIndex)
    {
        m_codedFrame = c;
        m_codedFrameByteIndex = bufferStartIndex;
    }

    private final void addNextCodedByteValue()
    {
        m_sr |= getNextCodedByteValue() << m_currentBits;
        m_currentBits += 8;
    }

    private final int getNextCodedByteValue()
    {
        int value = m_codedFrame[m_codedFrameByteIndex];
        m_codedFrameByteIndex++;
        return value & 0xFF;
    }

    public final int getNextBits(int bits)
    {
        switch (allocationMode)
        {
        case LSBitFirst:
            while (m_currentBits < bits)
            {
                addNextCodedByteValue();
            }
            int value = m_sr & Gsm_Def.BITMASKS[bits];
            m_sr >>>= bits;
            m_currentBits -= bits;
            return value;
        case MSBitFirst:
        default:
            throw new RuntimeException("not supported");
        }
    }
}
