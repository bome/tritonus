/*
 * FluidSynthesizer.java
 *
 * This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 * Copyright (c) 2006 by Henri Manson
 * Copyright (c) 2006 by Matthias Pfisterer
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.midi.device.fluidsynth;

import javax.sound.midi.*;

import org.tritonus.share.TDebug;
import org.tritonus.share.midi.TMidiDevice;
import org.tritonus.midi.sb.fluidsynth.FluidSoundbank;

/*
TODO: implement openImpl() and closeImpl()
*/
public class FluidSynthesizer extends TMidiDevice implements Synthesizer
{
    private MidiChannel channels[];
    private FluidSoundbank defaultSoundbank;
    
	private int defaultbankSfontID;
    
    // native pointers *32 bit*!
	private int settingsPtr;
	private int synthPtr;
	private int audioDriverPtr;



	static
	{
		loadNativeLibrary();
	}


	/** Load the native library for fluidsynth.
	 */
	private static void loadNativeLibrary()
	{
		if (TDebug.TraceFluidNative) TDebug.out("FluidSynthesizer.loadNativeLibrary(): loading native library tritonusfluid");
		try
		{
			System.loadLibrary("tritonusfluid");
			// only reached if no exception occures
			if (TDebug.TraceFluidNative)
			{
				setTrace(true);
			}
		}
		catch (Error e)
		{
			if (TDebug.TraceFluidNative ||
			    TDebug.TraceAllExceptions)
			{
				TDebug.out(e);
			}
			// throw e;
		}
		if (TDebug.TraceFluidNative) TDebug.out("FluidSynthesizer.loadNativeLibrary(): loaded");
	}



	/**
	 * Constructor.
	 */
    public FluidSynthesizer(MidiDevice.Info info) throws Exception
    {
        super(info, false, true);
    }


	protected void openImpl() 
	throws MidiUnavailableException
	{
		if (newSynth() < 0)
        {
            throw new MidiUnavailableException("Low-level initialization of the synthesizer failed");
        }
        if (TDebug.TraceSynthesizer) TDebug.out("FluidSynthesizer: " + Integer.toHexString(synthPtr));

        Receiver r = getReceiver();
        channels = new FluidMidiChannel[16];
        for (int i = 0; i < 16; i++)
        	channels[i] = new FluidMidiChannel(r, i);

            String sfontFile =
			System.getProperty("tritonus.fluidsynth.defaultsoundbank");
		if (sfontFile != null && ! sfontFile.equals(""))
		{
			int sfontID = loadSoundFont(sfontFile);
			setDefaultSoundBank(sfontID);
		}
    }


    protected void closeImpl()
    {
        if (TDebug.TraceSynthesizer) TDebug.out("FluidSynthesizer.closeImpl(): "
        		+ Integer.toHexString(synthPtr));
        deleteSynth();
        super.closeImpl();
    }




    public void setDefaultSoundBank(int sfontID)
    {
	    defaultSoundbank = new FluidSoundbank(this, sfontID);
	    defaultbankSfontID = sfontID;
    }


    protected void finalize(){
        if (TDebug.TraceSynthesizer) TDebug.out("finalize: " + Integer.toHexString(synthPtr));
        close();
    }


    protected void receive(MidiMessage message, long lTimeStamp)
    {
//        System.out.print("FluidSynth.receive: ");
//        Juke.printHex(message.getMessage(), 0, message.getLength());
//        System.out.println();
        if (message instanceof ShortMessage)
        {
            ShortMessage sm = (ShortMessage) message;
            nReceive(sm.getCommand(), sm.getChannel(), sm.getData1(), sm.getData2());
        }
    }


    public native void nReceive(int command, int channel, int data1, int data2);
    public native int loadSoundFont(String filename);
    public native void setBankOffset(int sfontID, int offset);
    public native void setGain(float gain);

	/* $$mp: currently not functional because fluid_synth_set_reverb_preset()
	 * is not present in fluidsynth 1.0.6.
	 */
    public native void setReverbPreset(int reverbPreset);

    public native int getMaxPolyphony();

    protected native int newSynth();
    protected native void deleteSynth();

	/** Sets tracing in the native code.
	 * Note that this method can either be called directly or (recommended)
	 * the system property "tritonus.TraceFluidNative" can be set to true.
	 *
	 * @see org.tritonus.share.TDebug
	 */
	public static native void setTrace(boolean bTrace);

    public boolean isSoundbankSupported(Soundbank soundbank)
    {
        return (soundbank instanceof FluidSoundbank);
    }

    public boolean loadAllInstruments(Soundbank soundbank)
    {
        return true;
    }

    public void unloadAllInstruments(Soundbank soundbank)
    {
    }

    public void unloadInstruments(Soundbank soundbank, Patch[] patchList)
    {
    }

    public boolean loadInstruments(Soundbank soundbank, Patch[] patchList)
    {
        return true;
    }

    public void unloadInstrument(Instrument instrument)
    {
    }

    public boolean loadInstrument(Instrument instrument)
    {
        return true;
    }

    public Instrument[] getAvailableInstruments()
    {
        return null;
    }

    public MidiChannel[] getChannels()
    {
        return channels;
    }

    public Soundbank getDefaultSoundbank()
    {
       return defaultSoundbank;
    }

    public long getLatency()
    {
        return 0L;
    }

    public Instrument[] getLoadedInstruments()
    {
        return null;
    }

    public VoiceStatus[] getVoiceStatus()
    {
        return null;
    }

    public boolean remapInstrument(Instrument from, Instrument to)
    {
        return true;
    }
}

/* FluidSynthesizer.java */
