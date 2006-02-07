/*
 * FluidMidiChannel.java
 *
 * This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 * Copyright (c) 1999 - 2006 by Matthias Pfisterer
 * Copyright (c) 2006 by Henri Manson
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import org.tritonus.share.TDebug;
import org.tritonus.share.midi.MidiUtils;



// idea: put things that can implemented with "pure MIDI" into a base class TMidiChannel
public class FluidMidiChannel implements MidiChannel
{
	private Receiver	m_receiver;
	private int		m_nChannel;

    public FluidMidiChannel(Receiver receiver, int nChannel)
    {
        m_receiver = receiver;
        m_nChannel = nChannel;
    }


    protected int getChannel()
    {
        return m_nChannel;
    }


    protected void sendMessage(MidiMessage message)
    {
        m_receiver.send(message, -1);
    }


    protected void sendMessage(int command, int data1, int data2)
    {
        ShortMessage	message = new ShortMessage();
        try
        {
            message.setMessage(command, getChannel(), data1, data2);
        }
        catch (InvalidMidiDataException e)
        {
            if (TDebug.TraceAlsaMidiChannel || TDebug.TraceAllExceptions)
            {
                TDebug.out(e);
            }
        }
        sendMessage(message);
    }


    public void noteOn(int nNoteNumber, int nVelocity)
    {
        sendMessage(ShortMessage.NOTE_ON, nNoteNumber, nVelocity);
    }


    public void noteOff(int nNoteNumber, int nVelocity)
    {
        sendMessage(ShortMessage.NOTE_OFF, nNoteNumber, nVelocity);
    }


    public void noteOff(int nNoteNumber)
    {
        sendMessage(ShortMessage.NOTE_OFF, nNoteNumber, 0);
    }


    public void setPolyPressure(int nNoteNumber, int nPressure)
    {
        sendMessage(ShortMessage.POLY_PRESSURE, nPressure, 0);
    }


    public int getPolyPressure(int nNoteNumber)
    {
        return -1;
    }


    public void setChannelPressure(int nPressure)
    {
        sendMessage(ShortMessage.CHANNEL_PRESSURE, nPressure, 0);
    }


    public int getChannelPressure()
    {
        return -1;
    }


    public void controlChange(int nController, int nValue)
    {
        sendMessage(ShortMessage.CONTROL_CHANGE, nController, nValue);
    }


    public int getController(int nController)
    {
        return -1;
    }


    public void programChange(int nProgram)
    {
        sendMessage(ShortMessage.PROGRAM_CHANGE, nProgram, 0);
    }


    public void programChange(int nBank, int nProgram)
    {
        sendMessage(ShortMessage.CONTROL_CHANGE, 32, nBank);
        sendMessage(ShortMessage.PROGRAM_CHANGE, nProgram, 0);
    }


    public int getProgram()
    {
        return -1;
    }


    public void setPitchBend(int nBend)
    {
        ShortMessage	message = new ShortMessage();
        try
        {
            message.setMessage(ShortMessage.PITCH_BEND, MidiUtils.get14bitLSB(nBend), MidiUtils.get14bitMSB(nBend));
        }
        catch (InvalidMidiDataException e)
        {
            if (TDebug.TraceAlsaMidiChannel || TDebug.TraceAllExceptions)
            {
                TDebug.out(e);
            }
        }
        sendMessage(message);
    }


    public int getPitchBend()
    {
        return -1;
    }


    public void resetAllControllers()
    {
    }


    public void allNotesOff()
    {
    }


    public void allSoundOff()
    {
    }


    public boolean localControl(boolean bOn)
    {
        return false;
    }


    public void setMono(boolean bMono)
    {
    }


    public boolean getMono()
    {
        return false;
    }


    public void setOmni(boolean bOmni)
    {
    }


    public boolean getOmni()
    {
        return false;
    }


    public void setMute(boolean bMute)
    {
    }


    public boolean getMute()
    {
        return false;
    }


    public void setSolo(boolean bSolo)
    {
    }


    public boolean getSolo()
    {
        return false;
    }
}


/* FluidMidiChannel.java */
