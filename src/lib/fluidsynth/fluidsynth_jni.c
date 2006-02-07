/*
 * fluidsynth_jni.c
 *
 * This file is part of Tritonus: http://www.tritonus.org/
 */

/*
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

#include "fluidsynth_jni.h"
/* $$mp: original version:
#include "fluidsynth\synth.h"
*/
#include <fluidsynth.h>

/* $$mp: original version:
#include "..\src\fluid_sfont.h"
#include "..\src\fluid_midi.h"
*/

/* $$mp: moved here from fluidsynth_jni.h, added 'static' */
static int fluid_jni_get_preset_count(int sfontID);


static fluid_settings_t* _settings = 0;
static fluid_synth_t* _synth = 0;
static fluid_audio_driver_t* _adriver = 0;
/* $$mp: original version:
void fluid_jni_process_midi(int command, int channel, int data1, int data2)
{
	fluid_midi_event_t event;
	
	event.type = command;
	event.channel = channel;
	event.param1 = data1;
	event.param2 = data2; 
	fluid_synth_handle_midi_event(_synth, &event);
}
*/
void fluid_jni_process_midi(int command, int channel, int data1, int data2)
{
	fluid_midi_event_t* event;

	//printf("values: %d %d %d %d\n", command, channel, data1, data2);
	//printf("1"); fflush(stdout);
	event = new_fluid_midi_event();
	if (!event)
	{
		printf("failed to instantiate fluid_midi_event_t\n");
		return;
	}
	//printf("2"); fflush(stdout);
	fluid_midi_event_set_type(event, command);
	fluid_midi_event_set_channel(event, channel);
	fluid_midi_event_set_key(event, data1);
	fluid_midi_event_set_velocity(event, data2);
	//printf("3"); fflush(stdout);
	/*printf("values2: %d %d %d %d\n",
	fluid_midi_event_get_type(event),
	fluid_midi_event_get_channel(event),
	fluid_midi_event_get_key(event),
	fluid_midi_event_get_velocity(event));
	fflush(stdout);
*/
	fluid_synth_handle_midi_event(_synth, event);
	//printf("4"); fflush(stdout);
	delete_fluid_midi_event(event);
	//printf("5\n"); fflush(stdout);
}

int fluid_jni_new_synth()
{
	if (_synth == 0)
	{
		_settings = new_fluid_settings();
		if (_settings == 0) {
			goto error_recovery;
		}
		
		_synth = new_fluid_synth(_settings);
		if (_synth == 0) {
			goto error_recovery;
		}
		
		_adriver = new_fluid_audio_driver(_settings, _synth);
		if (_adriver == 0) {
			goto error_recovery;
		}
		
		/* Load a SoundFont*/
		//	int sfont_id = fluid_synth_sfload(_synth, "Q:\\download\\soundfont\\Titanic 200 GM-GS SoundFont 1.1.sf2", 0);
		
		/* Select bank 0 and preset 0 in the SoundFont we just loaded on
		channel 0 */
		//	fluid_synth_program_select(_synth, 0, sfont_id, 0, 0); 
	}
	return 0;
	
error_recovery:
	fluid_jni_delete_synth();
	return -1;
}


void fluid_jni_delete_synth()
{
	if (_adriver) {
		delete_fluid_audio_driver(_adriver);
		_adriver = 0;
	}
	if (_synth) {
		delete_fluid_synth(_synth);
		_synth = 0;
	}
	if (_settings) {
		delete_fluid_settings(_settings);
		_settings = 0;
	}
}

int fluid_jni_sfload(const char*  filename)
{
	if (_synth == 0) {
		return -1;
	}
	int sfont_id = fluid_synth_sfload(_synth, filename, 1);
	return sfont_id;
}

void fluid_jni_set_bank_offset(int sfontID, int offset)
{
	fluid_synth_set_bank_offset(_synth, sfontID, offset);
}

/* $$mp: original version:
static int fluid_jni_get_preset_count(int sfontID)
{
	int font;
	fluid_sfont_t* sfont;
	fluid_preset_t preset;
	int count = 0;
	
	sfont = fluid_synth_get_sfont_by_id(_synth, sfontID);
	
	if (sfont != NULL)
	{	
		fluid_sfont_iteration_start(sfont);
		
		while (fluid_sfont_iteration_next(sfont, &preset))
		{
			count++;
		}
	}
	return count;
}
*/

static int fluid_jni_get_preset_count(int sfontID)
{
	fluid_sfont_t* sfont;
	fluid_preset_t preset;
	int count = 0;
	
	sfont = fluid_synth_get_sfont_by_id(_synth, sfontID);
	
	if (sfont != NULL)
	{
		sfont->iteration_start(sfont);
		
		while (sfont->iteration_next(sfont, &preset))
		{
			count++;
		}
	}
	return count;
}


jobjectArray fluid_jni_get_presets(JNIEnv *env, jobject obj, jint sfontID)
{
	//printf("4");
	jclass fluidinstrclass = (*env)->FindClass(env, "org/tritonus/midi/sb/fluidsynth/FluidSoundbank$FluidInstrument");
	if (!fluidinstrclass) printf("could not get class id");
	//printf("5");
	jmethodID initid = (*env)->GetMethodID(env, fluidinstrclass, "<init>", "(Lorg/tritonus/midi/sb/fluidsynth/FluidSoundbank;IILjava/lang/String;)V");
	if (!initid) printf("could not get method id");
	//printf("6");
	int count = fluid_jni_get_preset_count(sfontID);
	//printf("7");
	jobjectArray instruments = (*env)->NewObjectArray(env, count, fluidinstrclass, NULL);
	//printf("8");
	jstring instrname;
	jobject instrument;

	fluid_sfont_t* sfont;
	fluid_preset_t preset;
	int offset;

	sfont = fluid_synth_get_sfont_by_id(_synth, sfontID);
	offset = fluid_synth_get_bank_offset(_synth, sfontID);

	if (sfont == NULL)
		return 0;

	sfont->iteration_start(sfont);

	int i = 0;
	while (sfont->iteration_next(sfont, &preset))
	{
		instrname = (*env)->NewStringUTF(env,
//									fluid_preset_get_name(&preset)
									preset.get_name(&preset)
										);
		instrument = (*env)->NewObject(env, fluidinstrclass, initid, obj,
//			(jint) fluid_preset_get_banknum(&preset) + offset,
			(jint) (preset.get_banknum(&preset) + offset),
//			(jint) fluid_preset_get_num(&preset),
			(jint) (preset.get_num(&preset)),
			(jobject) instrname);
		(*env)->SetObjectArrayElement(env, instruments, i++, instrument);
	}
	return instruments;
}


/* $$mp: commented out because fluid_synth_set_reverb_preset() is not
         present in fluidsynth 1.0.6
int fluid_jni_set_reverb_preset(int reverbPreset)
{
	return fluid_synth_set_reverb_preset(_synth, reverbPreset);
}
*/

void fluid_jni_set_gain(float gain)
{
	fluid_synth_set_gain(_synth, gain);
}

int fluid_jni_get_polyphony()
{
	if (_synth)
	{
		return fluid_synth_get_polyphony(_synth);
	}
	else
	{
		return -99;
	}
}
