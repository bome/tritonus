/*
 * org_tritonus_midi_device_fluidsynth_FluidSynthesizer.c
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

#include <jni.h>
#include <fluidsynth.h>

#include "org_tritonus_midi_device_fluidsynth_FluidSynthesizer.h"
#include "org_tritonus_midi_sb_fluidsynth_FluidSoundbank.h"


//static int fluid_jni_get_preset_count(int sfontID);


static fluid_settings_t* _settings = 0;
static fluid_synth_t* _synth = 0;
static fluid_audio_driver_t* _adriver = 0;

static void fluid_jni_delete_synth();



static int fluid_jni_new_synth()
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
	}
	return 0;

error_recovery:
	fluid_jni_delete_synth();
	return -1;
}


static void fluid_jni_delete_synth()
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





/*
 * Class:     org_tritonus_midi_device_fluidsynth_FluidSynthesizer
 * Method:    newSynth
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_midi_device_fluidsynth_FluidSynthesizer_newSynth
(JNIEnv *env, jobject obj)
{
	return fluid_jni_new_synth();
}


/*
 * Class:     org_tritonus_midi_device_fluidsynth_FluidSynthesizer
 * Method:    deleteSynth
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_tritonus_midi_device_fluidsynth_FluidSynthesizer_deleteSynth
(JNIEnv *env, jobject obj)
{
	fluid_jni_delete_synth();
}


/*
 * Class:     org_tritonus_midi_device_fluidsynth_FluidSynthesizer
 * Method:    nReceive
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_org_tritonus_midi_device_fluidsynth_FluidSynthesizer_nReceive
(JNIEnv *env, jobject obj, jint command, jint channel, jint data1, jint data2)
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


/*
 * Class:     org_tritonus_midi_device_fluidsynth_FluidSynthesizer
 * Method:    loadSoundFont
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_midi_device_fluidsynth_FluidSynthesizer_loadSoundFont
(JNIEnv *env, jobject obj, jstring filename)
{
	const char *cfilename = (*env)->GetStringUTFChars(env, filename, 0);
	int sfont_id;
	if (_synth == 0)
	{
		sfont_id = -1;
	}
	else
	{
		sfont_id = fluid_synth_sfload(_synth, cfilename, 1);
	}
	(*env)->ReleaseStringUTFChars(env, filename, cfilename);

	return sfont_id;
}


/*
 * Class:     org_tritonus_midi_device_fluidsynth_FluidSynthesizer
 * Method:    setBankOffset
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_tritonus_midi_device_fluidsynth_FluidSynthesizer_setBankOffset
(JNIEnv *env, jobject obj, jint sfontID, jint offset)
{
	fluid_synth_set_bank_offset(_synth, sfontID, offset);
}

/*
 * Class:     org_tritonus_midi_sb_fluidsynth_FluidSoundbank
 * Method:    nGetInstruments
 * Signature: (I)[Lorg/tritonus/midi/sb/fluidsynth/FluidSoundbank/FluidInstrument;
 */
JNIEXPORT jobjectArray JNICALL Java_org_tritonus_midi_sb_fluidsynth_FluidSoundbank_nGetInstruments
(JNIEnv *env, jobject obj, jint sfontID)
{
	//printf("3a\n");
	//printf("4");
	jclass fluidinstrclass = (*env)->FindClass(env, "org/tritonus/midi/sb/fluidsynth/FluidSoundbank$FluidInstrument");
	if (!fluidinstrclass) printf("could not get class id");
	//printf("5");
	jmethodID initid = (*env)->GetMethodID(env, fluidinstrclass, "<init>", "(Lorg/tritonus/midi/sb/fluidsynth/FluidSoundbank;IILjava/lang/String;)V");
	if (!initid) printf("could not get method id");
	//printf("6");
	int count = 0;
	jobjectArray instruments;
	jstring instrname;
	jobject instrument;

	fluid_sfont_t* sfont;
	fluid_preset_t preset;
	int offset;

	sfont = fluid_synth_get_sfont_by_id(_synth, sfontID);
	
	if (sfont != NULL)
	{
		sfont->iteration_start(sfont);
		
		while (sfont->iteration_next(sfont, &preset))
		{
			count++;
		}
	}

	//printf("7");
	instruments = (*env)->NewObjectArray(env, count, fluidinstrclass, NULL);

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


/*
 * Class:     org_tritonus_midi_device_fluidsynth_FluidSynthesizer
 * Method:    setGain
 * Signature: (F)V
 */
JNIEXPORT void JNICALL Java_org_tritonus_midi_device_fluidsynth_FluidSynthesizer_setGain
(JNIEnv *env, jobject obj, jfloat gain)
{
	fluid_synth_set_gain(_synth, (float) gain);
}


/*
 * Class:     org_tritonus_midi_device_fluidsynth_FluidSynthesizer
 * Method:    setReverbPreset
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_tritonus_midi_device_fluidsynth_FluidSynthesizer_setReverbPreset
(JNIEnv *env, jobject obj, jint reverbPreset)
{
/* $$mp: currently not functional because fluid_synth_set_reverb_preset() is not
         present in fluidsynth 1.0.6
*/
	//fluid_synth_set_reverb_preset(_synth, (int) reverbPreset);
}


/*
 * Class:     org_tritonus_midi_device_fluidsynth_FluidSynthesizer
 * Method:    getMaxPolyphony
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_midi_device_fluidsynth_FluidSynthesizer_getMaxPolyphony
(JNIEnv *env, jobject obj)
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


/* org_tritonus_midi_device_fluidsynth_FluidSynthesizer.c */
