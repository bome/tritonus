/*
 * Encoder is the base class for all GSM encoding.
 * Copyright (C) 1999  Christopher Edwards
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package org.tritonus.lowlevel.gsm;

import org.tritonus.lowlevel.gsm.BitEncoder.AllocationMode;

/**
 * Encoder for GSM 06.10.
 * 
 * @author Christopher Edwards
 * @author Matthias Pfisterer
 */
public class Encoder
{ /* Every Encoder has a state through completion */
    private Gsm_State g_s = new Gsm_State();
    private Long_term lg_term_Obj = new Long_term();
    private Lpc lpc_Obj = new Lpc();
    private Rpe rpe_Obj = new Rpe();
    private Short_term sh_term_Obj = new Short_term();

    /* [0..7] LAR coefficients OUT */
    private short LARc[] = new short[8];
    /* [0..3] LTP lag OUT */
    private short Nc[] = new short[4];
    /* [0..3] coded LTP gain OUT */
    private short Mc[] = new short[4];
    /* [0..3] RPE grid selection OUT */
    private short bc[] = new short[4];
    /* [0..3] Coded maximum amplitude OUT */
    private short xmaxc[] = new short[4];
    /* [13*4] normalized RPE samples OUT */
    private short xmc[] = new short[13 * 4];

    /* Reads 160 bytes */
    private int[] input_signal = new int[160]; /* [0..159] OUT */

    private GsmFrameFormat gsmFrameFormat;

    /**
     * Constructor.
     * 
     * @param gsmFrameFormat
     *            the format of the GSM frames to produce
     */
    public Encoder(GsmFrameFormat gsmFrameFormat)
    {
        this.gsmFrameFormat = gsmFrameFormat;
    }

    /**
     * Encodes a block of data.
     * 
     * @param asBuffer
     *            an 160-element (for "toast" frame format) or 320-element array
     *            (for Microsoft frame format) with the data to encode in PCM
     *            signed 16 bit format
     * @param abFrame
     *            the encoded GSM frame (33 or 65 bytes, depending on the frame
     *            format). Note that the contents of this array is overwritten
     *            by this method.
     */
    public void encode(short[] asBuffer, byte[] abFrame)
    {
        BitEncoder bitEncoder;
        switch (gsmFrameFormat)
        {
        case TOAST:
            bitEncoder = new BitEncoder(abFrame, AllocationMode.MSBitFirst);
            bitEncoder.addBits(0xD, 4);
            break;
        case MICROSOFT:
            bitEncoder = new BitEncoder(abFrame, AllocationMode.LSBitFirst);
            break;
        default:
            throw new RuntimeException("Unhandled GSM frame format");
        }
        for (int i = 0; i < 160; i++)
        {
            input_signal[i] = asBuffer[i];
        }
        Gsm_Coder_java();
        implodeFrameGeneric(bitEncoder);

        if (gsmFrameFormat == GsmFrameFormat.MICROSOFT)
        {
            for (int i = 0; i < 160; i++)
            {
                input_signal[i] = asBuffer[i + 160];
            }
            Gsm_Coder_java();
            implodeFrameGeneric(bitEncoder);
        }
    }

    private void implodeFrameGeneric(BitEncoder bitEncoder)
    {
        bitEncoder.addBits(LARc[0], 6);
        bitEncoder.addBits(LARc[1], 6);
        bitEncoder.addBits(LARc[2], 5);
        bitEncoder.addBits(LARc[3], 5);
        bitEncoder.addBits(LARc[4], 4);
        bitEncoder.addBits(LARc[5], 4);
        bitEncoder.addBits(LARc[6], 3);
        bitEncoder.addBits(LARc[7], 3);

        bitEncoder.addBits(Nc[0], 7);
        bitEncoder.addBits(bc[0], 2);
        bitEncoder.addBits(Mc[0], 2);
        bitEncoder.addBits(xmaxc[0], 6);
        bitEncoder.addBits(xmc[0], 3);
        bitEncoder.addBits(xmc[1], 3);
        bitEncoder.addBits(xmc[2], 3);
        bitEncoder.addBits(xmc[3], 3);
        bitEncoder.addBits(xmc[4], 3);
        bitEncoder.addBits(xmc[5], 3);
        bitEncoder.addBits(xmc[6], 3);
        bitEncoder.addBits(xmc[7], 3);
        bitEncoder.addBits(xmc[8], 3);
        bitEncoder.addBits(xmc[9], 3);
        bitEncoder.addBits(xmc[10], 3);
        bitEncoder.addBits(xmc[11], 3);
        bitEncoder.addBits(xmc[12], 3);

        bitEncoder.addBits(Nc[1], 7);
        bitEncoder.addBits(bc[1], 2);
        bitEncoder.addBits(Mc[1], 2);
        bitEncoder.addBits(xmaxc[1], 6);
        bitEncoder.addBits(xmc[13], 3);
        bitEncoder.addBits(xmc[14], 3);
        bitEncoder.addBits(xmc[15], 3);
        bitEncoder.addBits(xmc[16], 3);
        bitEncoder.addBits(xmc[17], 3);
        bitEncoder.addBits(xmc[18], 3);
        bitEncoder.addBits(xmc[19], 3);
        bitEncoder.addBits(xmc[20], 3);
        bitEncoder.addBits(xmc[21], 3);
        bitEncoder.addBits(xmc[22], 3);
        bitEncoder.addBits(xmc[23], 3);
        bitEncoder.addBits(xmc[24], 3);
        bitEncoder.addBits(xmc[25], 3);

        bitEncoder.addBits(Nc[2], 7);
        bitEncoder.addBits(bc[2], 2);
        bitEncoder.addBits(Mc[2], 2);
        bitEncoder.addBits(xmaxc[2], 6);
        bitEncoder.addBits(xmc[26], 3);
        bitEncoder.addBits(xmc[27], 3);
        bitEncoder.addBits(xmc[28], 3);
        bitEncoder.addBits(xmc[29], 3);
        bitEncoder.addBits(xmc[30], 3);
        bitEncoder.addBits(xmc[31], 3);
        bitEncoder.addBits(xmc[32], 3);
        bitEncoder.addBits(xmc[33], 3);
        bitEncoder.addBits(xmc[34], 3);
        bitEncoder.addBits(xmc[35], 3);
        bitEncoder.addBits(xmc[36], 3);
        bitEncoder.addBits(xmc[37], 3);
        bitEncoder.addBits(xmc[38], 3);

        bitEncoder.addBits(Nc[3], 7);
        bitEncoder.addBits(bc[3], 2);
        bitEncoder.addBits(Mc[3], 2);
        bitEncoder.addBits(xmaxc[3], 6);
        bitEncoder.addBits(xmc[39], 3);
        bitEncoder.addBits(xmc[40], 3);
        bitEncoder.addBits(xmc[41], 3);
        bitEncoder.addBits(xmc[42], 3);
        bitEncoder.addBits(xmc[43], 3);
        bitEncoder.addBits(xmc[44], 3);
        bitEncoder.addBits(xmc[45], 3);
        bitEncoder.addBits(xmc[46], 3);
        bitEncoder.addBits(xmc[47], 3);
        bitEncoder.addBits(xmc[48], 3);
        bitEncoder.addBits(xmc[49], 3);
        bitEncoder.addBits(xmc[50], 3);
        bitEncoder.addBits(xmc[51], 3);
    }

    /**
     * Main part of encoding.
     * 
     * <p>Uses array input_signal as input (160 samples are expected there).</p>
     * 
     * <p>Output is in the arrays xmc, LARc, etc.</p>
     */
    private void Gsm_Coder_java()
    {
        int xmc_point = 0;
        int Nc_bc_index = 0;
        int xmaxc_Mc_index = 0;
        int dp_dpp_point_dp0 = 120;

        // short[] ep = new short[40];
        short[] e = new short[50];
        short[] so = new short[160];

        Gsm_Preprocess(so);
        lpc_Obj.Gsm_LPC_Analysis(so, LARc);
        sh_term_Obj.Gsm_Short_Term_Analysis_Filter(g_s, LARc, so);

        short[] dp = g_s.getDp0();
        short[] dpp = dp;

        for (int k = 0; k <= 3; k++, xmc_point += 13)
        {
            lg_term_Obj.Gsm_Long_Term_Predictor(so, /* d [0..39] IN */
            k * 40, /* so entry point */
            e, /* e+5 [0..39] OUT */
            dp, /* Referance to Gsm_State dp0 */
            dpp, /* Referance to Gsm_State dp0 */
            dp_dpp_point_dp0, /* Where to start the dp0 ref */
            Nc, /* [0..3] coded LTP gain OUT */
            bc, /* [0..3] RPE grid selection OUT */
            Nc_bc_index++ /* The current referance point for Nc & bc */
            );

            rpe_Obj.Gsm_RPE_Encoding(e, /* e + 5 ][0..39][ IN/OUT */
            xmaxc, /* [0..3] Coded maximum amplitude OUT */
            Mc, /* [0..3] coded LTP gain OUT */
            xmaxc_Mc_index++, /* The current referance point */
            xmc, /* [13*4] normalized RPE samples OUT */
            xmc_point /* The current referance point for xmc */);

            for (int i = 0; i <= 39; i++)
            {
                dp[i + dp_dpp_point_dp0] = Add.GSM_ADD(e[5 + i], dpp[i
                        + dp_dpp_point_dp0]);
            }

            g_s.setDp0(dp);
            dp_dpp_point_dp0 += 40;
        }

        for (int i = 0; i < 120; i++)
        {
            g_s.setDp0Indexed(i, g_s.getDp0Indexed((160 + i)));
        }
    }

    private void Gsm_Preprocess(short[] so) /* [0..159] IN/OUT */
    throws IllegalArgumentException
    {
        int index = 0, so_index = 0;

        short z1 = g_s.getZ1();
        int L_z2 = g_s.getL_z2();
        int mp = g_s.getMp();

        short s1 = 0, msp = 0, lsp = 0, SO = 0;
        int L_s2 = 0, L_temp = 0;
        int k = 160;

        while (k != 0)
        {
            k--;

            /*
             * 4.2.1 Downscaling of the input signal
             */
            SO = (short) (Add.SASR((short) input_signal[index++], (short) 3) << 2);

            if (!(SO >= -0x4000))
            { /* downscaled by */
                throw new IllegalArgumentException("Gsm_Preprocess: SO = " + SO
                        + " is out of range. Sould be >= -0x4000 ");
            }

            if (!(SO <= 0x3FFC))
            { /* previous routine. */
                throw new IllegalArgumentException("Gsm_Preprocess: SO = " + SO
                        + " is out of range. Sould be <= 0x3FFC ");
            }

            /*
             * 4.2.2 Offset compensation
             * 
             * This part implements a high-pass filter and requires extended
             * arithmetic precision for the recursive part of this filter. The
             * input of this procedure is the array so[0...159] and the output
             * the array sof[ 0...159 ].
             */

            /*
             * Compute the non-recursive part
             */
            s1 = (short) (SO - z1); /* s1 = gsm_sub( *so, z1 ); */
            z1 = SO;

            if (s1 == Gsm_Def.MIN_WORD)
            {
                throw new IllegalArgumentException("Gsm_Preprocess: s1 = " + s1
                        + " is out of range. ");
            }

            /*
             * Compute the recursive part
             */
            L_s2 = s1;
            L_s2 <<= 15;

            /*
             * Execution of a 31 bv 16 bits multiplication
             */

            msp = Add.SASR(L_z2, 15);

            /* gsm_L_sub(L_z2,(msp<<15)); */
            lsp = (short) (L_z2 - ((int) (msp << 15)));

            L_s2 += Add.GSM_MULT_R(lsp, (short) 32735);
            L_temp = (int) msp * 32735; /* GSM_L_MULT(msp,32735) >> 1; */
            L_z2 = Add.GSM_L_ADD(L_temp, L_s2);

            /*
             * Compute sof[k] with rounding
             */
            L_temp = Add.GSM_L_ADD(L_z2, 16384);

            /*
             * 4.2.3 Preemphasis
             */
            msp = Add.GSM_MULT_R((short) mp, (short) -28180);
            mp = Add.SASR(L_temp, 15);
            so[so_index++] = Add.GSM_ADD((short) mp, msp);
        }
        g_s.setZ1(z1);
        g_s.setL_z2(L_z2);
        g_s.setMp(mp);
    }
}
