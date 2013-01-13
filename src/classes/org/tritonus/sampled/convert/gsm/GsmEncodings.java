package org.tritonus.sampled.convert.gsm;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;

import org.tritonus.lowlevel.gsm.GsmFrameFormat;

/**
 * Constants for {@link Encoding} used for GSM 06.10.
 * 
 * @author Matthias Pfisterer
 * 
 */
public interface GsmEncodings
{
    /**
     * Encoding for the "toast" frame format. Corresponds to
     * {@link GsmFrameFormat#TOAST}.
     */
    public static final Encoding TOAST_GSM_ENCODING = new AudioFormat.Encoding(
            "GSM0610");

    /**
     * Encoding for the Microsoft frame format. Corresponds to
     * {@link GsmFrameFormat#MICROSOFT}.
     */
    public static final Encoding MS_GSM_ENCODING = new AudioFormat.Encoding(
            "MS GSM0610");
}
