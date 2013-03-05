package org.tritonus.dsp.ais;

import javax.sound.sampled.AudioFormat;

import org.tritonus.lowlevel.dsp.sources.AbstractGenerator;
import org.tritonus.share.sampled.FloatSampleBuffer;

public class GeneratorAudioInputStream extends FloatAudioInputStream {

	private final AbstractGenerator generator;
	
	public GeneratorAudioInputStream(AbstractGenerator generator,
			AudioFormat targetFormat) {
		super(null, targetFormat);
		this.generator = generator;
	}

	@Override
	protected void convert(FloatSampleBuffer buffer) {
		// TODO Auto-generated method stub
	}
}
