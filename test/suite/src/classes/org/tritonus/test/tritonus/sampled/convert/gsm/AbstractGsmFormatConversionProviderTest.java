package org.tritonus.test.tritonus.sampled.convert.gsm;

import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.spi.FormatConversionProvider;

import org.tritonus.lowlevel.gsm.GsmFrameFormat;
import org.tritonus.test.tritonus.sampled.convert.AbstractFormatConversionProviderTest;

public abstract class AbstractGsmFormatConversionProviderTest extends
        AbstractFormatConversionProviderTest
{
    /**
     * Encoding for the "toast" frame format. Corresponds to
     * {@link GsmFrameFormat#TOAST}.
     */
    protected static final Encoding TOAST_GSM_ENCODING = new Encoding("GSM0610");

    /**
     * Encoding for the Microsoft frame format. Corresponds to
     * {@link GsmFrameFormat#MICROSOFT}.
     */
    protected static final Encoding MS_GSM_ENCODING = new Encoding("MS GSM0610");

    protected static final float _8KHZ = 8000.0F;

    private FormatConversionProvider formatConversionProvider;

    protected final FormatConversionProvider getFormatConversionProvider()
    {
        return formatConversionProvider;
    }

    protected final void setFormatConversionProvider(
            FormatConversionProvider formatConversionProvider)
    {
        this.formatConversionProvider = formatConversionProvider;
    }

   
}
