/*
 *	org_tritonus_lowlevel_cdda_CDDA.c
 */

#include	<errno.h>
#include	<fcntl.h>
#include	<unistd.h>
#include	<stdio.h>
#include	<stdlib.h>
#include	<sys/ioctl.h>
#include	<endian.h>
#include	<cdda_interface.h>
#include	<cdda_paranoia.h>

#include	"common.h"
#include	"org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia.h"


// default value taken over from cdparanoia main.c
static const int	MAX_RETRIES = 20;


typedef struct
{
	cdrom_drive*	drive;
	cdrom_paranoia*	paranoia;
} handle_t;




static int
getParanoiaMode()
{
	return PARANOIA_MODE_FULL ^ PARANOIA_MODE_NEVERSKIP;
}



static int	DEBUG = 0;
static FILE*	debug_file = NULL;

static jfieldID
getNativeHandleFieldID(JNIEnv *env)
{
	static jfieldID	nativeHandleFieldID = NULL;

	if (nativeHandleFieldID == NULL)
	{
		// TODO: use a passed object rather than the name of the class
		jclass	cls = (*env)->FindClass(env, "org/tritonus/lowlevel/cdda/cdparanoia/Cdparanoia");
		if (cls == NULL)
		{
			throwRuntimeException(env, "cannot get class object for org.tritonus.lowlevel.cdda.cdparanoia.CdParanoia");
		}
		nativeHandleFieldID = (*env)->GetFieldID(env, cls, "m_lNativeHandle", "J");
		if (nativeHandleFieldID == NULL)
		{
			throwRuntimeException(env, "cannot get field ID for m_lNativeHandle");
		}
	}
	return nativeHandleFieldID;
}



static handle_t*
getNativeHandle(JNIEnv *env, jobject obj)
{
	jfieldID	fieldID = getNativeHandleFieldID(env);
	return (handle_t*) (*env)->GetLongField(env, obj, fieldID);
}



static void
setNativeHandle(JNIEnv *env, jobject obj, handle_t* handle)
{
	jfieldID	fieldID = getNativeHandleFieldID(env);
	(*env)->SetLongField(env, obj, fieldID, (jlong) handle);
}



/*
 * Class:     org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia
 * Method:    open
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_open
(JNIEnv *env, jobject obj, jstring strDevice)
{
	int	nReturn;
	// TODO: fetch from strDevice
	char*	cd_dev = "/dev/cdrom";
	cdrom_drive*	cdrom = NULL;
	handle_t*	pHandle;
	int		nParanoiaMode;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_open(): begin\n"); }
	nParanoiaMode = getParanoiaMode();
	cdrom = cdda_identify(cd_dev, 0, NULL);
	if (cdrom == NULL)
	{
		return -1;
	}
	nReturn = cdda_open(cdrom);
	if (nReturn < 0)
	{
		return -1;
	}

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_open(): drive endianess: %d\n", cdrom->bigendianp); }

	pHandle = (handle_t*) malloc(sizeof(handle_t));
	if (pHandle == NULL)
	{
		return -1;
	}
	pHandle->drive = cdrom;
	pHandle->paranoia = paranoia_init(pHandle->drive);
	if (pHandle->paranoia == NULL)
	{
		cdda_close(pHandle->drive);
		free(pHandle);
		return -1;
	}
	paranoia_modeset(pHandle->paranoia, nParanoiaMode);

	setNativeHandle(env, obj, pHandle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_open(): end\n"); }
	return 0;
}



/*
 * Class:     org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_close
(JNIEnv *env, jobject obj)
{
	handle_t*	handle;
	cdrom_drive*	cdrom;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_close(): begin\n"); }
	// TODO: close paranoia pointer?
	handle = getNativeHandle(env, obj);
	if (handle != NULL)
	{
		cdrom = handle->drive;
		if (cdrom != NULL)
		{
			cdda_close(cdrom);
		}
	}
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_close(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia
 * Method:    readTOC
 * Signature: ([I[I[I[I[Z[Z[I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_readTOC
(JNIEnv* env, jobject obj,
 jintArray anValues,
 jintArray anStartFrame,
 jintArray anLength,
 jintArray anType,
 jbooleanArray abCopy,
 jbooleanArray abPre,
 jintArray anChannels)
{
	handle_t*	handle;
	cdrom_drive*	cdrom;
	int		nFirstTrack;
	int		nLastTrack;
	jint*		pnValues;
	jint*		pnStartFrame;
	jint*		pnLength;
	jint*		pnType;
	jboolean*	pbCopy;
	jboolean*	pbPre;
	jint*		pnChannels;
	int		nTrack;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_readTOC(): begin\n"); }
	handle = getNativeHandle(env, obj);
	cdrom = handle->drive;
	checkArrayLength(env, anValues, 2);
	pnValues = (*env)->GetIntArrayElements(env, anValues, NULL);
	if (pnValues == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	// TODO: check if first track is guaranteed to be 1
	pnValues[0] = 1;
	pnValues[1] = cdda_tracks(cdrom);
	nFirstTrack = 1;
	nLastTrack = cdda_tracks(cdrom);
	(*env)->ReleaseIntArrayElements(env, anValues, pnValues, 0);

	checkArrayLength(env, anStartFrame, 100);
	pnStartFrame = (*env)->GetIntArrayElements(env, anStartFrame, NULL);
	if (pnStartFrame == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	checkArrayLength(env, anLength, 100);
	pnLength = (*env)->GetIntArrayElements(env, anLength, NULL);
	if (pnLength == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	checkArrayLength(env, anType, 100);
	pnType = (*env)->GetIntArrayElements(env, anType, NULL);
	if (pnType == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	checkArrayLength(env, abCopy, 100);
	pbCopy = (*env)->GetBooleanArrayElements(env, abCopy, NULL);
	if (pbCopy == NULL)
	{
		throwRuntimeException(env, "GetBooleanArrayElements failed");
	}
	checkArrayLength(env, abPre, 100);
	pbPre = (*env)->GetBooleanArrayElements(env, abPre, NULL);
	if (pbPre == NULL)
	{
		throwRuntimeException(env, "GetBooleanArrayElements failed");
	}
	checkArrayLength(env, anChannels, 100);
	pnChannels = (*env)->GetIntArrayElements(env, anChannels, NULL);
	if (pnChannels == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	for (nTrack = nFirstTrack; nTrack <= nLastTrack; nTrack++)
	{
		pnStartFrame[nTrack - nFirstTrack] = cdda_track_firstsector(cdrom, nTrack);
		pnLength[nTrack - nFirstTrack] = cdda_track_lastsector(cdrom, nTrack) - cdda_track_firstsector(cdrom, nTrack) + 1;
		pnType[nTrack - nFirstTrack] = 0;	// TODO: toc_entry.cdte_ctrl & CDROM_DATA_TRACK;
		pbCopy[nTrack - nFirstTrack] = cdda_track_copyp(cdrom, nTrack);
		pbPre[nTrack - nFirstTrack] = cdda_track_preemp(cdrom, nTrack);
		pnChannels[nTrack - nFirstTrack] = cdda_track_channels(cdrom, nTrack);
		if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_readTOC(): %d: %d %d %d\n", nTrack - nFirstTrack, nTrack, pnStartFrame[nTrack - nFirstTrack], pnLength[nTrack - nFirstTrack]); }
	}

	(*env)->ReleaseIntArrayElements(env, anStartFrame, pnStartFrame, 0);
	(*env)->ReleaseIntArrayElements(env, anLength, pnLength, 0);
	(*env)->ReleaseIntArrayElements(env, anType, pnType, 0);
	(*env)->ReleaseBooleanArrayElements(env, abCopy, pbCopy, 0);
	(*env)->ReleaseBooleanArrayElements(env, abPre, pbPre, 0);
	(*env)->ReleaseIntArrayElements(env, anChannels, pnChannels, 0);

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_readTOC(): end\n"); }
	return 0;
}



/*
 * Class:     org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia
 * Method:    prepareTrack
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_prepareTrack
(JNIEnv* env, jobject obj, jint nTrack)
{
	handle_t*	handle;
	cdrom_drive*	cdrom;
	cdrom_paranoia*	paranoia;
	int		nFirstSector;
	
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_prepareTrack(): begin\n"); }
	handle = getNativeHandle(env, obj);
	cdrom = handle->drive;
	paranoia = handle->paranoia;

	nFirstSector = cdda_track_firstsector(cdrom, nTrack);
	// TODO: check return value
	paranoia_seek(paranoia, nFirstSector, SEEK_SET);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_prepareTrack(): end\n"); }
	return 0;
}



/*
 * Class:     org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia
 * Method:    readNextFrame
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_readNextFrame
(JNIEnv *env, jobject obj, jint nCount, jbyteArray abData)
{
	handle_t*	handle;
	cdrom_drive*	cdrom;
	cdrom_paranoia*	paranoia;
	int16_t*	psBuffer;
	jbyte*		pbData;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_readNextFrame(): begin\n"); }
	handle = getNativeHandle(env, obj);
	cdrom = handle->drive;
	paranoia = handle->paranoia;

	checkArrayLength(env, abData, CD_FRAMESIZE_RAW * nCount);
	pbData = (*env)->GetByteArrayElements(env, abData, NULL);
	if (pbData == NULL)
	{
		throwRuntimeException(env, "GetByteArrayElements failed");
	}
	// TODO: verify that NULL is allowed for callback; common values for maxretries
	// TODO: repeat for multiple sectors
	psBuffer = paranoia_read_limited(paranoia, NULL, MAX_RETRIES);
	if (psBuffer == NULL)
	{
		throwRuntimeException(env, "cdparanoia_Cdparanoia: read failed");
	}
#if __BYTE_ORDER == __LITTLE_ENDIAN
	(void) memcpy(pbData, psBuffer, CD_FRAMESIZE_RAW);
#else
	swab(psBuffer, pbData, CD_FRAMESIZE_RAW);
#endif
	(*env)->ReleaseByteArrayElements(env, abData, pbData, 0);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_readNextFrame(): end\n"); }
	return 0;
}



/*
 * Class:     org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia_setTrace
(JNIEnv *env, jclass cls, jboolean bTrace)
{
	DEBUG = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_cdda_cdparanoia_Cdparanoia.c ***/