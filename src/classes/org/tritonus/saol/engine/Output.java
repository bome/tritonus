/*
 *	Output.java
 */

import	java.io.IOException;



/**	Output method for the SA engine.
	This interface abstracts the way calculated samples are
	output from the engine. The engine only calls this interface,
	while implementations of this interface write the samples to a
	file, a line, a network socket or whatever else.

	@author Matthias Pfisterer
 */
public interface Output
{
	/**	Initiate the cumulation of a sample value.
		This method must be called in an a-cycle before
		any instrument's a-cycle code is executed.
	*/
	public void init();


	/**	Add the sample value of one instrument.
		This method can be called by instrument's a-cycle
		code to output the sample value the instrument has
		calculated for this a-cycle.
		The current hacky version allows only for mono samples.
	*/
	public void output(float fSample);


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



/*** Output.java ***/
