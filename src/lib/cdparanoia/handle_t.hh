/*
 *	handle_t.hh
 */

extern "C"
{
#include	<cdda_interface.h>
#include	<cdda_paranoia.h>
}


typedef struct
{
	cdrom_drive*	drive;
	cdrom_paranoia*	paranoia;
} handle_t;



/*** handle_t.hh ***/
