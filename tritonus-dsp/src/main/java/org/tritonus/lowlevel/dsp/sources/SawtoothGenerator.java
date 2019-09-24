package org.tritonus.lowlevel.dsp.sources;

/**
 * Generator for sawtooth waveform.
 * 
 * @author Matthias Pfisterer
 * 
 * @see SineGenerator
 * @see SquareGenerator
 * @see TriangleGenerator
 */
public class SawtoothGenerator extends AbstractPeriodicGenerator {

	public SawtoothGenerator(float sampleRate, int channelCount) {
		super(sampleRate, channelCount);
	}

	/** {@inheritDoc} */
	@Override
	protected float generateSample(float fPeriodPosition) {
		float fValue;
		if (fPeriodPosition < 0.5F) {
			fValue = 2.0F * fPeriodPosition;
		} else {
			fValue = 2.0F * (fPeriodPosition - 1.0F);
		}
		return fValue;
	}
}
