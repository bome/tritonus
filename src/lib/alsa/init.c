/*
 *	init.c
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

#include <stdio.h>
#include <dlfcn.h>
#include "../common/debug.h"


static void*	asound_dl_handle = NULL;


/*
  These methods are needed to circumvent a problem with the dynamic linker:
  The VM calls dlopen() to load this library, which depends on libasound.so.
  libasound.so, in turn, uses dlopen() to load plugins. It is this chained
  calling of dlopen() which results in the second call not resolving symbols
  properly. Making the symbols of libasound.so global here (RTLD_GLOBAL)
  solves the problem.
 */
void _init(void)
{
	if (debug_flag) { (void) fprintf(debug_file, "_init(): begin\n"); }
	asound_dl_handle = dlopen("libasound.so", RTLD_LAZY | RTLD_GLOBAL);
	if (debug_flag) { (void) fprintf(debug_file, "_init(): result: %p\n", asound_dl_handle); }
	if (debug_flag) { (void) fprintf(debug_file, "_init(): end\n"); }
}



void _fini(void)
{
	if (debug_flag) { (void) fprintf(debug_file, "_fini(): begin\n"); }
	if (asound_dl_handle != NULL)
	{
		if (debug_flag) { (void) fprintf(debug_file, "_fini(): closeing handle\n"); }
		(void) dlclose(asound_dl_handle);
	}
	if (debug_flag) { (void) fprintf(debug_file, "_fini(): end\n"); }
}



/*** init.c ***/
