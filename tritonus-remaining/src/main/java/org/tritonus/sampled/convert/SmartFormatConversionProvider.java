/*
 *	SmartFormatConversionProvider.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 - 2004 by Matthias Pfisterer
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
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.sampled.convert;

import java.util.HashSet;
import java.util.Set;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.tritonus.share.sampled.convert.TFormatConversionProvider;


// Name suggested by Florian: MetaFormatConversionProvider
/* Additinal explanation:
> Ich hab mal kurz in den SmartConverter reingeguckt, warum machst Du das mit den
> Threads ? In Rekursion wird doch nicht ein neuer Thread benutzt ? Und sonst
> koennte man das doch mit synchronized bzw. einem echten lock machen ?

Bei der Rekursion bezu"glich der selben Konvertersuche befindet man sich
im gleichen Thread; diese Eigenschaft nutze ich ja gerade aus. Es kann
aber das Anwendungsprogramm von mehreren Threads aus gleichzeitig einen
Konverter anfordern. Diese Aufrufe gehen alle in das gleiche SmartF.C.P.
Objekt (es gibt nur eins). Die Methoden des Konverters (das gilt fu"r
alle) mu"ssen also reentrant sein. Die Alternative wa"re ein globaler
Lock. Das halte ich aber fu"r nicht akzeptabel. Bei meiner Soundmachine
zum Beispiel wu"rde das zu Problemen fu"hren: da ist es no"tig, da? beim
Abspielen mehrere Kana"le on the fly konvertiert wird. Ein globaler Lock
wu"rde zu Verzo"gerungen im Abspielen fu"hren. Die "einfache" Variante der
Rekursionserkennung (ohne Beru"cksichtigung von Threads) braucht nur ein
einfaches Flag, das gesetzt wird, wenn keine Rekursion mehr stattfinden
soll. Dieses Flag wird bei meiner Implementierung mit der Hashtabelle
realisiert; sie simuliert ein thread-lokales Verhalten dieses Flags.
Alles klar? Ich seh' ein, man braucht zwei Knoten im Hirn, um das zu
verstehen...
*/

/**	"Smart" formatConversionProvider.
 *	This FormatConversionProvider tries to find combinations of other
 *	FormatConversionProviders so that the chain of these providers fulfill the request for a
 *	format conversion given to this provider.
 *
 * @author Matthias Pfisterer
 */

public class SmartFormatConversionProvider
extends TFormatConversionProvider
{
	/**	Stores the threads currently blocked.
	 *	To avoid recursion, this class stores which threads have already "passed"
	 *	methods of this class once. On entry of a method prone to recursion, it is
	 *	checked if the current thread is in the set. If so, this indicates a recursion
	 *	and the method will return immediately. If not, the current thread is entered
	 *	this data structure, so that further invocations can detect a recursion. On
	 *	exit of this method, it is removed from this data structure to indicate it is
	 *	"free".
	 */
	private Set<Thread>	m_blockedThreads;



	public SmartFormatConversionProvider()
	{
		m_blockedThreads = new HashSet<Thread>();
	}



	// TODO: can use AudioSystem to return all source encodings? (don't forget to block!)
	public AudioFormat.Encoding[] getSourceEncodings()
	{
		return EMPTY_ENCODING_ARRAY;
	}



	// TODO: can use AudioSystem to return all target encodings? (don't forget to block!)
	public AudioFormat.Encoding[] getTargetEncodings()
	{
		return EMPTY_ENCODING_ARRAY;
	}



	public AudioFormat.Encoding[] getTargetEncodings(
		AudioFormat sourceFormat)
	{
		// TODO:
		return null;
	}



	public boolean isConversionSupported(
		AudioFormat.Encoding targetEncoding,
		AudioFormat sourceFormat)
	{
		return false;
	}



	public AudioFormat[] getTargetFormats(
		AudioFormat.Encoding targetEncoding,
		AudioFormat sourceFormat)
	{
		return null;
	}



	public boolean isConversionSupported(
		AudioFormat targetFormat,
		AudioFormat sourceFormat)
	{
		if (isCurrentThreadBlocked())
		{
			return false;
		}
		AudioFormat[]	aIntermediateFormats = getIntermediateFormats(sourceFormat, targetFormat);
		return aIntermediateFormats != null;
	}



	public AudioInputStream getAudioInputStream(
		AudioFormat.Encoding targetEncoding,
		AudioInputStream audioInputStream)
	{
		return null;
	}



	public AudioInputStream getAudioInputStream(
		AudioFormat targetFormat,
		AudioInputStream audioInputStream)
	{
		return null;
	}


	/*
	 *	Search for converter chain.
	 */

	/*
	 *
	 *	@return	an array of intermediate formats (possibly of length 0 if it's possible
	 *		to do the conversion in one step) or null if the conversion is not
	 *		possible.
	 */
	private AudioFormat[] getIntermediateFormats(AudioFormat sourceFormat, AudioFormat targetFormat)
	{
		AudioFormat.Encoding	sourceEncoding = sourceFormat.getEncoding();
		AudioFormat.Encoding	targetEncoding = targetFormat.getEncoding();
		blockCurrentThread();
		boolean	bDirectConversionPossible = AudioSystem.isConversionSupported(targetFormat, sourceFormat);
		unblockCurrentThread();
		if (bDirectConversionPossible)
		{
			return EMPTY_FORMAT_ARRAY;
		}
		else if (isPCM(sourceEncoding) && isPCM(targetEncoding))
		{
			/*
			 *	The SR converter is not yet implemented. The PCM2PCM converter
			 *	should handle all other cases.
			 */
			return null;
			/* eventually block */
		}
		else if (!isPCM(sourceEncoding))
		{
			AudioFormat	intermediateFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				sourceFormat.getSampleRate(),
				sourceFormat.getSampleSizeInBits(),
				sourceFormat.getChannels(),
				AudioSystem.NOT_SPECIFIED,
				sourceFormat.getSampleRate(),
				true);
			blockCurrentThread();
			AudioFormat[]	aPreIntermediateFormats = getIntermediateFormats(sourceFormat, intermediateFormat);
			unblockCurrentThread();
			AudioFormat[]	aPostIntermediateFormats = getIntermediateFormats(intermediateFormat, targetFormat);
			if (aPreIntermediateFormats != null && aPostIntermediateFormats != null)
			{
				AudioFormat[]	aIntermediateFormats = new AudioFormat[aPreIntermediateFormats.length + 1 + aPostIntermediateFormats.length];
				System.arraycopy(aPreIntermediateFormats, 0, aIntermediateFormats, 0, aPreIntermediateFormats.length);
				aIntermediateFormats[aPreIntermediateFormats.length] = intermediateFormat;
				System.arraycopy(aPostIntermediateFormats, 0, aIntermediateFormats, aPreIntermediateFormats.length, aPostIntermediateFormats.length);
				return aIntermediateFormats;
			}
			else
			{
				return null;
			}
		}
		else if (!isPCM(targetEncoding))
		{
			AudioFormat	intermediateFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				targetFormat.getSampleRate(),
				targetFormat.getSampleSizeInBits(),
				targetFormat.getChannels(),
				AudioSystem.NOT_SPECIFIED,
				targetFormat.getSampleRate(),
				true);
			AudioFormat[]	aPreIntermediateFormats = getIntermediateFormats(sourceFormat, intermediateFormat);
			blockCurrentThread();
			AudioFormat[]	aPostIntermediateFormats = getIntermediateFormats(intermediateFormat, targetFormat);
			unblockCurrentThread();
			if (aPreIntermediateFormats != null && aPostIntermediateFormats != null)
			{
				AudioFormat[]	aIntermediateFormats = new AudioFormat[aPreIntermediateFormats.length + 1 + aPostIntermediateFormats.length];
				System.arraycopy(aPreIntermediateFormats, 0, aIntermediateFormats, 0, aPreIntermediateFormats.length);
				aIntermediateFormats[aPreIntermediateFormats.length] = intermediateFormat;
				System.arraycopy(aPostIntermediateFormats, 0, aIntermediateFormats, aPreIntermediateFormats.length, aPostIntermediateFormats.length);
				return aIntermediateFormats;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}



	/*
	 *	General helper methods.
	 */


	private static boolean isPCM(AudioFormat.Encoding encoding)
	{
		return encoding.equals(AudioFormat.Encoding.PCM_SIGNED)
			|| encoding.equals(AudioFormat.Encoding.PCM_SIGNED);
	}



	protected static boolean isSignedPCM(AudioFormat.Encoding encoding)
	{
		return encoding.equals(AudioFormat.Encoding.PCM_SIGNED);
	}




	/*
	 *	Methods for recursion detection/blocking.
	 */

	private boolean isCurrentThreadBlocked()
	{
		return m_blockedThreads.contains(Thread.currentThread());
	}



	private void blockCurrentThread()
	{
		m_blockedThreads.add(Thread.currentThread());
	}



	private void unblockCurrentThread()
	{
		m_blockedThreads.remove(Thread.currentThread());
	}
}



/*** SmartFormatConversionProvider.java ***/
