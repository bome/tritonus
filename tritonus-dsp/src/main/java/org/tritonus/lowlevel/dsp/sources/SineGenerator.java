package org.tritonus.lowlevel.dsp.sources;

/**
 * Generator for sine waveform.
 * 
 * @author Matthias Pfisterer
 * 
 * @see SawtoothGenerator
 * @see SquareGenerator
 * @see TriangleGenerator
 */
public class SineGenerator extends AbstractPeriodicGenerator {

	public SineGenerator(float sampleRate, int channelCount) {
		super(sampleRate, channelCount);
	}

	/** {@inheritDoc} */
	@Override
	protected float generateSample(float fPeriodPosition) {
		// TODO: * Math.PI ??
		return (float) Math.sin(2.0 * fPeriodPosition);
	}
}
