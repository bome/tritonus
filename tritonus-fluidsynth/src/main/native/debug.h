/*
 *	debug.h
 */

/*
 *  Copyright (c) 1999 - 2006 by Matthias Pfisterer
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

#ifndef _DEBUG_H
#define _DEBUG_H

#include <stdio.h>


#define FALSE 0
#define TRUE 1

#ifndef _MSC_VER
#define VARIADIC_MACROS
#elif _MSC_VER >= 1400
#define VARIADIC_MACROS
#endif

#ifdef VARIADIC_MACROS
#define out(...) if (debug_flag) { fprintf(debug_file, __VA_ARGS__); \
									fflush(debug_file); }
#endif
static int	debug_flag = FALSE;
static FILE*	debug_file = NULL;


#endif /* _DEBUG_H */

/*** debug.h ***/
