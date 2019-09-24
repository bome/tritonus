package org.tritonus.lowlevel.dsp.sources;

/**
 * Generator for square waveform.
 * 
 * @author Matthias Pfisterer
 * 
 * @see SawtoothGenerator
 * @see SineGenerator
 * @see TriangleGenerator
 */
public class SquareGenerator extends AbstractPeriodicGenerator {

	public SquareGenerator(float sampleRate, int channelCount) {
		super(sampleRate, channelCount);
	}

	/** {@inheritDoc} */
	@Override
	protected float generateSample(float fPeriodPosition) {
		return (fPeriodPosition < 0.5F) ? 1.0F : -1.0F;
	}
}
