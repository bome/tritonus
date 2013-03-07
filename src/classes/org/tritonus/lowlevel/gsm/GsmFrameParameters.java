package org.tritonus.lowlevel.gsm;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Contains the "exploded" parameters of a GSM frame.
 * 
 * <p>
 * This are the parameters after bit-decoding or before bit-encoding.
 * 
 * <table border="1">
 * <tr>
 * <th>Parameter name</th>
 * <th>Variable name</th>
 * <th>number of parameters</th>
 * </tr>
 * <tr>
 * <td>LAR</td>
 * <td>m_LARc</td>
 * <td>8</td>
 * </tr>
 * <tr>
 * <td>N</td>
 * <td>m_Nc</td>
 * <td>4</td>
 * </tr>
 * <tr>
 * <td>b</td>
 * <td>m_bc</td>
 * <td>4</td>
 * </tr>
 * <tr>
 * <td>M</td>
 * <td>m_Mc</td>
 * <td>4</td>
 * </tr>
 * <tr>
 * <td>Xmax</td>
 * <td>m_xmaxc</td>
 * <td>4</td>
 * </tr>
 * <tr>
 * <td>x</td>
 * <td>m_xmc</td>
 * <td>52 (13 * 4)</td>
 * </tr>
 * </table>
 * 
 * @author Matthias Pfisterer
 * 
 */
public class GsmFrameParameters
{
    public int[] m_LARc = new int[8];
    public int[] m_Nc = new int[4];
    public int[] m_Mc = new int[4];
    public int[] m_bc = new int[4];
    public int[] m_xmaxc = new int[4];
    public int[] m_xmc = new int[13 * 4];

    /**
     * Dumps the parameter values to an output stream.
     * 
     * <p>
     * Can be used in the following ways: <code>dump(System.out)</code> or
     * <code>dump(System.err)</code>.
     * </p>
     * 
     * @param printStream
     *            the stream to output the dump
     */
    public void dump(PrintStream printStream)
    {
        PrintWriter pw = new PrintWriter(printStream);
        pw.println("GSM frame:");
        for (int i = 0; i < 8; i++)
        {
            pw.println("m_LARc[" + i + "]" + m_LARc[i]);
        }
        for (int i = 0; i < 4; i++)
        {
            pw.println("m_Nc[" + i + "]" + m_Nc[i]);
            pw.println("m_bc[" + i + "]" + m_bc[i]);
            pw.println("m_Mc[" + i + "]" + m_Mc[i]);
            pw.println("m_xmaxc[" + i + "]" + m_xmaxc[i]);
            for (int j = 0; j < 13; j++)
            {
                pw.println("m_xmc[" + i * 13 + j + "]" + m_xmc[i * 13 + j]);
            }
        }
    }
}
