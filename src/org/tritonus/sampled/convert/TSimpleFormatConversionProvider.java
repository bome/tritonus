/*
 *	TSimpleFormatConversionProvider.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */


package	org.tritonus.sampled.convert;


import	java.util.Collection;
import	java.util.Iterator;

import	javax.sound.sampled.AudioFormat;

import	org.tritonus.util.ArraySet;


/**
 *	This is a base class for FormatConversionProviders that can convert
 *	from each source encoding/format to each target encoding/format.
 *	If this is not the case, use TMatrixFormatConversionProvider.
 *	<p>Overriding classes must implement at least
 *	<code>AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream sourceStream)</code>
 *	and provide a constructor that calls the protected constructor of this class.
 *
 * @author Matthias Pfisterer
 */
public abstract class TSimpleFormatConversionProvider
	extends		TFormatConversionProvider
{
	private Collection	m_sourceEncodings;
	private Collection	m_targetEncodings;
	private Collection	m_sourceFormats;
	private Collection	m_targetFormats;




	protected TSimpleFormatConversionProvider(
		Collection sourceFormats,
		Collection targetFormats)
	{
		m_sourceEncodings = new ArraySet();
		m_targetEncodings = new ArraySet();
		m_sourceFormats = sourceFormats;
		m_targetFormats = targetFormats;
		collectEncodings(m_sourceFormats, m_sourceEncodings);
		collectEncodings(m_targetFormats, m_targetEncodings);
	}



	private static void collectEncodings(Collection formats,
				      Collection encodings)
	{
		Iterator	iterator = formats.iterator();
		while (iterator.hasNext())
		{
			AudioFormat	format = (AudioFormat) iterator.next();
			encodings.add(format.getEncoding());
		}
	}



	public AudioFormat.Encoding[] getSourceEncodings()
	{
		return (AudioFormat.Encoding[]) m_sourceEncodings.toArray(EMPTY_ENCODING_ARRAY);
	}



	public AudioFormat.Encoding[] getTargetEncodings()
	{
		return (AudioFormat.Encoding[]) m_targetEncodings.toArray(EMPTY_ENCODING_ARRAY);
	}



	// overwritten of FormatConversionProvider
	public boolean isSourceEncodingSupported(AudioFormat.Encoding sourceEncoding)
	{
		return m_sourceEncodings.contains(sourceEncoding);
	}



	// overwritten of FormatConversionProvider
	public boolean isTargetEncodingSupported(AudioFormat.Encoding targetEncoding)
	{
		return m_targetEncodings.contains(targetEncoding);
	}



	/**
	 *	This implementation assumes that the converter can convert
	 *	from each of its source encodings to each of its target
	 *	encodings. If this is not the case, the converter has to
	 *	override this method.
	 */
	public AudioFormat.Encoding[] getTargetEncodings(AudioFormat sourceFormat)
	{
		if (isAllowedSourceFormat(sourceFormat))
		{
			return getTargetEncodings();
		}
		else
		{
			return EMPTY_ENCODING_ARRAY;
		}
	}



	/**
	 *	This implementation assumes that the converter can convert
	 *	from each of its source formats to each of its target
	 *	formats. If this is not the case, the converter has to
	 *	override this method.
	 */
	public AudioFormat[] getTargetFormats(AudioFormat.Encoding targetEncoding, AudioFormat sourceFormat)
	{
		if (isConversionSupported(targetEncoding, sourceFormat))
		{
			return (AudioFormat[]) m_targetFormats.toArray(EMPTY_FORMAT_ARRAY);
		}
		else
		{
			return EMPTY_FORMAT_ARRAY;
		}
	}


	// TODO: check if necessary
	protected boolean isAllowedSourceEncoding(AudioFormat.Encoding sourceEncoding)
	{
		return m_sourceEncodings.contains(sourceEncoding);
	}



	protected boolean isAllowedTargetEncoding(AudioFormat.Encoding targetEncoding)
	{
		return m_targetEncodings.contains(targetEncoding);
	}



	protected boolean isAllowedSourceFormat(AudioFormat sourceFormat)
	{
		Iterator	iterator = m_sourceFormats.iterator();
		while (iterator.hasNext())
		{
			AudioFormat	format = (AudioFormat) iterator.next();
			if (format.matches(sourceFormat))
			{
				return true;
			}
		}
		return false;
	}



	protected boolean isAllowedTargetFormat(AudioFormat targetFormat)
	{
		Iterator	iterator = m_targetFormats.iterator();
		while (iterator.hasNext())
		{
			AudioFormat	format = (AudioFormat) iterator.next();
			if (format.matches(targetFormat))
			{
				return true;
			}
		}
		return false;
	}
	
	// $$fb 2000-04-02 added some convenience methods for overriding classes
	protected Collection getCollectionSourceEncodings() {
		return m_sourceEncodings;
	}
	
	protected Collection getCollectionTargetEncodings() {
		return m_targetEncodings;
	}
	
	protected Collection getCollectionSourceFormats() {
		return m_sourceFormats;
	}
	
	protected Collection getCollectionTargetFormats() {
		return m_targetFormats;
	}
	
}

/*** TSimpleFormatConversionProvider.java ***/
