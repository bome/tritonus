package org.tritonus.dsp.processor;

import org.tritonus.dsp.interfaces.FloatSampleProcessor;
import org.tritonus.lowlevel.dsp.FIR;
import org.tritonus.lowlevel.dsp.FIRDirectFormFilterDescription;
import org.tritonus.share.sampled.FloatSampleBuffer;

public class FIRFilterProcessor implements FloatSampleProcessor {

	private FIR[] filters;

	public FIRFilterProcessor(int nChannels,
			FIRDirectFormFilterDescription filterDescription) {
		filters = new FIR[nChannels];
		for (int channel = 0; channel < nChannels; channel++) {
			filters[channel] = new FIR(filterDescription);
		}
	}

	public void setFilterDescription(
			FIRDirectFormFilterDescription filterDescription) {
		for (FIR filter : filters) {
			filter.setFilterDescription(filterDescription);
		}
	}

	private int getChannelCount() {
		return filters.length;
	}

	/** {@inheritDoc} */
	@Override
	public void process(FloatSampleBuffer buffer) {
		if (getChannelCount() != buffer.getChannelCount()) {
			throw new IllegalArgumentException(
					"number of channels of FloatSampleBuffer not equal to number of channels of this filter processor");
		}
		for (int nChannel = 0; nChannel < buffer.getChannelCount(); nChannel++) {
			float[] afBuffer = buffer.getChannel(nChannel);
			for (int nSample = 0; nSample < buffer.getSampleCount(); nSample++) {
				afBuffer[nSample] = filters[nChannel]
						.process(afBuffer[nSample]);
			}
		}
	}
}
