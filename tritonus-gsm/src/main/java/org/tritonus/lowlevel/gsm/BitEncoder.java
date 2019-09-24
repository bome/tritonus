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
     * <p>
     * Note: this constructor writes 0 values into the passed byte array.
     * </p>
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
        Arrays.fill(codedBytes, (byte) 0);
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
            int x;
            switch (allocationMode)
            {
            case LSBitFirst:
                x = (((value) & Gsm_Def.BITMASKS[bits]) << (8 - remainingBitsInCurrentByte));
                codedBytes[byteIndex] |= x;
                value >>>= bits;
                break;
            case MSBitFirst:
                x = (((value >>> nextNumBits) & Gsm_Def.BITMASKS[bits]) << nextRemainingBits);
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
