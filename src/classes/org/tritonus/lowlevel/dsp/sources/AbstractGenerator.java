package org.tritonus.lowlevel.dsp.sources;

import org.tritonus.share.sampled.FloatSampleBuffer;

/**
 * Base class for waveform generators.
 * 
 * <p>
 * Note: amplitude and frequency can be changes on the fly cleanly. Sample rate
 * and number of channels are fixed after creation of the class.
 * </p>
 * 
 * @author Matthias Pfisterer
 * 
 */
public abstract class AbstractGenerator {
	private final int channelCount;
	private final float sampleRate;
	private float frequency;
	private float amplitude;
	private FloatSampleBuffer currentFloatSampleBuffer;
	private FloatSampleBuffer nextFloatSampleBuffer;
	private boolean useNextBuffer;

	public AbstractGenerator(float sampleRate, int channelCount) {
		super();
		this.channelCount = channelCount;
		this.sampleRate = sampleRate;
		setAmplitude(1.0F);
		setFrequency(1000.0F);
	}

	public float getSampleRate() {
		return sampleRate;
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
		regenerate();
	}

	public float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
		regenerate();
	}

	public int getChannelCount() {
		return channelCount;
	}

	public FloatSampleBuffer getSampleBuffer() {
		synchronized (this) {
			if (useNextBuffer) {
				currentFloatSampleBuffer = nextFloatSampleBuffer;
				nextFloatSampleBuffer = null;
				useNextBuffer = false;
			}
		}
		return currentFloatSampleBuffer;
	}

	private void regenerate() {
		nextFloatSampleBuffer = generate();
		useNextBuffer = true;
	}

	protected abstract FloatSampleBuffer generate();
}
