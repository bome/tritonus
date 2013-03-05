package org.tritonus.lowlevel.dsp.sources;

/**
 * Generator for triangle waveform.
 * 
 * @author Matthias Pfisterer
 * 
 * @see SawtoothGenerator
 * @see SineGenerator
 * @see SquareGenerator
 */
public class TriangleGenerator extends AbstractPeriodicGenerator {

	public TriangleGenerator(float sampleRate, int channelCount) {
		super(sampleRate, channelCount);
	}

	/** {@inheritDoc} */
	@Override
	protected float generateSample(float fPeriodPosition) {
		float fValue;
		if (fPeriodPosition < 0.25F) {
			fValue = 4.0F * fPeriodPosition;
		} else if (fPeriodPosition < 0.75F) {
			fValue = -4.0F * (fPeriodPosition - 0.5F);
		} else {
			fValue = 4.0F * (fPeriodPosition - 1.0F);
		}
		return fValue;
	}
}
