/*
 *	org_tritonus_lowlevel_cdda_cooked_ioctl_CookedIoctl.c
 */

#include	<errno.h>
#include	<fcntl.h>
#include	<linux/cdrom.h>
#include	<unistd.h>
#include	<stdio.h>
#include	<sys/ioctl.h>

#include	"common.h"
#include	"org_tritonus_lowlevel_cdda_cooked_0005fioctl_CookedIoctl.h"



static int	DEBUG = 0;
static FILE*	debug_file = NULL;

static jfieldID
getNativeHandleFieldID(JNIEnv *env)
{
	static jfieldID	nativeHandleFieldID = NULL;

	if (nativeHandleFieldID == NULL)
	{
		// TODO: use a passed object rather than the name of the class
		jclass	cls = (*env)->FindClass(env, "org/tritonus/lowlevel/cdda/cooked_ioctl/CookedIoctl");
		if (cls == NULL)
		{
			throwRuntimeException(env, "cannot get class object for org.tritonus.lowlevel.cdda.cooked_ioctl.CookedIoctl");
		}
		nativeHandleFieldID = (*env)->GetFieldID(env, cls, "m_lNativeHandle", "J");
		if (nativeHandleFieldID == NULL)
		{
			throwRuntimeException(env, "cannot get field ID for m_lNativeHandle");
		}
	}
	return nativeHandleFieldID;
}



static int
getNativeHandle(JNIEnv *env, jobject obj)
{
	jfieldID	fieldID = getNativeHandleFieldID(env);
	return (int) (*env)->GetLongField(env, obj, fieldID);
}



static void
setNativeHandle(JNIEnv *env, jobject obj, int handle)
{
	jfieldID	fieldID = getNativeHandleFieldID(env);
	(*env)->SetLongField(env, obj, fieldID, (jlong) handle);
}



/*
 * Class:     org_tritonus_lowlevel_cdda_cooked_0005fioctl_CookedIoctl
 * Method:    open
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_open
(JNIEnv *env, jobject obj, jstring strDevice)
{
	int	cdrom_fd;
	char*	cd_dev = "/dev/cdrom";

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_open(): begin\n"); }
	cdrom_fd = open(cd_dev, O_RDONLY | O_NONBLOCK);
	if (cdrom_fd == -1)
	{
		return -errno;
	}
	setNativeHandle(env, obj, cdrom_fd);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_open(): end\n"); }
	return 0;
}



/*
 * Class:     org_tritonus_lowlevel_cdda_cooked_0005fioctl_CookedIoctl
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_close
(JNIEnv *env, jobject obj)
{
	int	fd;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_close(): begin\n"); }
	fd = getNativeHandle(env, obj);
	close(fd);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_close(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_cdda_cooked_0005fioctl_CookedIoctl
 * Method:    readTOC
 * Signature: ([I[I[I[I[Z[Z[I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_readTOC
(JNIEnv *env, jobject obj, jintArray anValues, jintArray anStartFrame, jintArray anLength, jintArray anType, jbooleanArray abCopy, jbooleanArray abPre, jintArray anChannels)
{
	int		cdrom_fd;
	int		nReturn;
	struct cdrom_tochdr	toc_hdr;
	struct cdrom_tocentry	toc_entry;
	int		nFirstTrack;
	int		nLastTrack;
	jint*		pnValues;
	jint*		pnStartFrame;
	jint*		pnType;
	int		i;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_readTOC(): begin\n"); }
	cdrom_fd = getNativeHandle(env, obj);
	nReturn = ioctl(cdrom_fd, CDROMREADTOCHDR, &toc_hdr);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "CookedIoctl: read TOC header ioctl failed");
	}
	checkArrayLength(env, anValues, 2);
	pnValues = (*env)->GetIntArrayElements(env, anValues, NULL);
	if (pnValues == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	pnValues[0] = toc_hdr.cdth_trk0;
	pnValues[1] = toc_hdr.cdth_trk1;
	nFirstTrack = toc_hdr.cdth_trk0;
	nLastTrack = toc_hdr.cdth_trk1;
	(*env)->ReleaseIntArrayElements(env, anValues, pnValues, 0);

	checkArrayLength(env, anStartFrame, 100);
	pnStartFrame = (*env)->GetIntArrayElements(env, anStartFrame, NULL);
	if (pnStartFrame == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	checkArrayLength(env, anType, 100);
	pnType = (*env)->GetIntArrayElements(env, anType, NULL);
	if (pnType == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	// TODO: i -> nTrack
	for (i = nFirstTrack; i <= nLastTrack; i++)
	{
		toc_entry.cdte_track = i;
		toc_entry.cdte_format = CDROM_LBA;
		nReturn = ioctl(cdrom_fd, CDROMREADTOCENTRY, &toc_entry);
		if (nReturn < 0)
		{
			throwRuntimeException(env, "CookedIoctl: read TOC entry ioctl failed");
		}
		pnStartFrame[i - nFirstTrack] = toc_entry.cdte_addr.lba;
		pnType[i - nFirstTrack] = toc_entry.cdte_ctrl & CDROM_DATA_TRACK;
	}
	i = CDROM_LEADOUT;
	toc_entry.cdte_track = i;
	toc_entry.cdte_format = CDROM_LBA;
	nReturn = ioctl(cdrom_fd, CDROMREADTOCENTRY, &toc_entry);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "CookedIoctl: read TOC entry ioctl failed");
	}
	pnStartFrame[nLastTrack - nFirstTrack + 1] = toc_entry.cdte_addr.lba;
	pnType[nLastTrack - nFirstTrack + 1] = toc_entry.cdte_ctrl&CDROM_DATA_TRACK;

	(*env)->ReleaseIntArrayElements(env, anStartFrame, pnStartFrame, 0);
	(*env)->ReleaseIntArrayElements(env, anType, pnType, 0);

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_readTOC(): end\n"); }
	return 0;
}



/*
 * Class:     org_tritonus_lowlevel_cdda_cooked_0005fioctl_CookedIoctl
 * Method:    readFrame
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_readFrame
(JNIEnv *env, jobject obj, jint nStartFrame, jint nCount, jbyteArray abData)
{
	int	cdrom_fd;
	int	nReturn;
	struct cdrom_read_audio ra;
	jbyte*		pbData;
	
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_readFrame(): begin\n"); }
	cdrom_fd = getNativeHandle(env, obj);
	checkArrayLength(env, abData, CD_FRAMESIZE_RAW * nCount);
	pbData = (*env)->GetByteArrayElements(env, abData, NULL);
	if (pbData == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	ra.addr.lba = nStartFrame;
	ra.addr_format = CDROM_LBA;
	ra.nframes = nCount;
	ra.buf = pbData;
	nReturn = ioctl(cdrom_fd, CDROMREADAUDIO, &ra);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "CookedIoctl: read raw ioctl failed");
	}
	(*env)->ReleaseByteArrayElements(env, abData, pbData, 0);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_readFrame(): end\n"); }
	return 0;
}



/*
 * Class:     org_tritonus_lowlevel_cdda_cooked_0005fioctl_CookedIoctl
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_cdda_cooked_1ioctl_CookedIoctl_setTrace
(JNIEnv *env, jclass cls, jboolean bTrace)
{
	DEBUG = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_cdda_cooked_ioctl_CookedIoctl.c ***/