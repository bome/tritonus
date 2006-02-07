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

/* $$mp: original version:
#include "fluidsynth_Synth.h"
*/
#include "org_tritonus_midi_device_fluidsynth_FluidSynthesizer.h"
#include "org_tritonus_midi_sb_fluidsynth_FluidSoundbank.h"

#include "fluidsynth_jni.h"

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
	fluid_jni_process_midi(command, channel, data1, data2);
}


/*
 * Class:     org_tritonus_midi_device_fluidsynth_FluidSynthesizer
 * Method:    loadSoundFont
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_midi_device_fluidsynth_FluidSynthesizer_loadSoundFont
(JNIEnv *env, jobject obj, jstring filename)
{
/* $$mp: original version:
	const char *cfilename = env->GetStringUTFChars(filename, 0);
	int sfont_id = fluid_jni_sfload(cfilename);
	env->ReleaseStringUTFChars(filename, cfilename);
*/
	const char *cfilename = (*env)->GetStringUTFChars(env, filename, 0);
	int sfont_id = fluid_jni_sfload(cfilename);
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
	fluid_jni_set_bank_offset(sfontID, offset);
}

/*
 * Class:     org_tritonus_midi_sb_fluidsynth_FluidSoundbank
 * Method:    nGetInstruments
 * Signature: (I)[Lorg/tritonus/midi/sb/fluidsynth/FluidSoundbank/FluidInstrument;
 */
JNIEXPORT jobjectArray JNICALL Java_org_tritonus_midi_sb_fluidsynth_FluidSoundbank_nGetInstruments
(JNIEnv *env, jobject obj, jint sfontID)
{
	jobjectArray arr;
	//printf("3a\n");
	arr = fluid_jni_get_presets(env, obj, sfontID);
	//printf("3b\n");
	return arr;
}

/* $$mp: original version:
JNIEXPORT void JNICALL Java_fluidsynth_Synth_setGain(JNIEnv *, jobject, jfloat gain)
{
	fluid_jni_set_gain((float) gain);
}
*/

/*
 * Class:     org_tritonus_midi_device_fluidsynth_FluidSynthesizer
 * Method:    setGain
 * Signature: (F)V
 */
JNIEXPORT void JNICALL Java_org_tritonus_midi_device_fluidsynth_FluidSynthesizer_setGain
(JNIEnv *env, jobject obj, jfloat gain)
{
	fluid_jni_set_gain((float) gain);
}

/* $$mp: original version:
JNIEXPORT int JNICALL Java_fluidsynth_Synth_setReverbPreset(JNIEnv *, jobject, jint reverbPreset)
{
	return fluid_jni_set_reverb_preset((int) reverbPreset);
}
*/

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
	// fluid_jni_set_reverb_preset((int) reverbPreset);
}


/*
 * Class:     org_tritonus_midi_device_fluidsynth_FluidSynthesizer
 * Method:    getMaxPolyphony
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_midi_device_fluidsynth_FluidSynthesizer_getMaxPolyphony
(JNIEnv *env, jobject obj)
{
	return fluid_jni_get_polyphony();
}

/* org_tritonus_midi_device_fluidsynth_FluidSynthesizer.c */
