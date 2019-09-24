/*
 *	SystemOutput.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.saol.engine;

import java.io.IOException;



/**	Output method for the SA engine.
	This interface abstracts the way calculated samples are
	output from the engine. The engine only calls this interface,
	while implementations of this interface write the samples to a
	file, a line, a network socket or whatever else.

	@author Matthias Pfisterer
 */
public interface SystemOutput
extends Output
{
	/**	Writes the accumulated sample values to the output media.
		This method must be called by the engine after all
		instrument's a-cycle code for this cycle is executed.
		It is intended to actually write the resulting sample data
		to the desired location. The desired location may be a file,
		a line, or somthing else, depending on this interface'
		implementation.
	*/
	public void emit()
		throws IOException;


	/**	Closes the output destination.
		This method must be called by the engine after execution,
		i.e. when there are no further a-cycles.
		It is intended to close files, lines, or other output
		destinations.
	*/
	public void close()
		throws IOException;
}



/*** SystemOutput.java ***/
