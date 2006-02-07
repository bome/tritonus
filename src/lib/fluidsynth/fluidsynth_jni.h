#ifndef _FLUID_JNI_H
#define _FLUID_JNI_H

#include <fluidsynth.h>
#include <jni.h>

int fluid_jni_new_synth(void);
void fluid_jni_delete_synth();
int fluid_jni_sfload(const char*  filename);
void fluid_jni_process_midi(int command, int channel, int data1, int data2);
void fluid_jni_set_bank_offset(int sfontID, int offset);
/* $$mp: original version:
int fluid_jni_get_preset_count(int sfontID);
*/
jobjectArray fluid_jni_get_presets(JNIEnv *env, jobject obj, jint sfontID);
/* $$mp: commented out because fluid_synth_set_reverb_preset() is not
         present in fluidsynth 1.0.6
int fluid_jni_set_reverb_preset(int reverbPreset);
*/
void fluid_jni_set_gain(float gain);
int fluid_jni_get_polyphony();
#endif /* _FLUID_JNI_H */
