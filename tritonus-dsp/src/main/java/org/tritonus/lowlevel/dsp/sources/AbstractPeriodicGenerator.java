package org.tritonus.lowlevel.dsp.sources;

import org.tritonus.share.sampled.FloatSampleBuffer;

public abstract class AbstractPeriodicGenerator extends AbstractGenerator {

	public AbstractPeriodicGenerator(float sampleRate, int channelCount) {
		super(sampleRate, channelCount);
	}

	@Override
	protected FloatSampleBuffer generate() {
		/*
		 * This is the target delay when changing frequency or amplitude. The
		 * buffer is filled with a number of periods of the waveform that fit
		 * into this amount of time. However, always at least one full period is
		 * generated. So for very low frequencies the delay raises (this is the
		 * case if a period length is greater than the target delay, i. e. with
		 * 10 ms delay for frequencies f < 100 Hz).
		 */
		final float DELAY_IN_SECONDS = 0.01F;
		float periodDurationInSeconds = 1 / getFrequency();
		final int periodCount = Math.max(1,
				(int) (DELAY_IN_SECONDS / periodDurationInSeconds));

		int sampleCount = Math.round((getSampleRate() * periodCount)
				/ getFrequency());
		FloatSampleBuffer floatSampleBuffer = new FloatSampleBuffer(1,
				sampleCount, getSampleRate());
		float amplitude = getAmplitude();
		float normalizedFrequency = getFrequency() / getSampleRate();
		float[] channelBuffer = floatSampleBuffer.getChannel(0);
		for (int i = 0; i < sampleCount; i++) {
			float sampleValue = (float) (amplitude * generateSample(normalizedFrequency
					* i));
			channelBuffer[i] = sampleValue;
		}
		floatSampleBuffer.expandChannel(getChannelCount());
		return floatSampleBuffer;
	}

	/**
	 * Generates a single sample of the waveform.
	 * 
	 * @param fPeriodPosition
	 *            the relative position inside a single period of the waveform.
	 *            The value passed is in the range [0.0, 1.0]
	 * @return the sample value, scaled to [-1.0, +1.0]
	 */
	protected abstract float generateSample(float fPeriodPosition);
}
