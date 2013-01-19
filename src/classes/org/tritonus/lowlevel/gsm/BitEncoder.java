package org.tritonus.lowlevel.gsm;

import java.util.Arrays;

public class BitEncoder
{
    public static enum AllocationMode
    {
        MSBitFirst, LSBitFirst;
    }

    private int byteIndex;
    private int remainingBitsInCurrentByte;
    private byte[] codedBytes;
    private AllocationMode allocationMode;

    /**
     * Constructor.
     * 
     * @param codedBytes
     * @param allocationMode
     */
    public BitEncoder(byte[] codedBytes, AllocationMode allocationMode)
    {
        super();
        byteIndex = 0;
        remainingBitsInCurrentByte = 8;
        this.codedBytes = codedBytes;
        this.allocationMode = allocationMode;
    }

    /**
     * Resets the array pointers and clears the array.
     */
    public void reset()
    {
        byteIndex = 0;
        remainingBitsInCurrentByte = 8;
        Arrays.fill(codedBytes, (byte) 0);
    }

    public final void addBits(int value, int numBits)
    {
        while (numBits > 0)
        {
            if (byteIndex >= codedBytes.length)
            {
                throw new RuntimeException("No more bytes in coded bytes array");
            }
            int bits = Math.min(numBits, remainingBitsInCurrentByte);
            int nextRemainingBits = remainingBitsInCurrentByte - bits;
            int nextNumBits = numBits - bits;
            switch (allocationMode)
            {
            case LSBitFirst:
                break;
            case MSBitFirst:
                int x = (((value >>> nextNumBits) & Gsm_Def.BITMASKS[bits]) << nextRemainingBits);
                codedBytes[byteIndex] |= x;
                break;
            }
            remainingBitsInCurrentByte = nextRemainingBits;
            if (remainingBitsInCurrentByte == 0)
            {
                byteIndex++;
                remainingBitsInCurrentByte = 8;
            }
            numBits = nextNumBits;
        }
    }
}
