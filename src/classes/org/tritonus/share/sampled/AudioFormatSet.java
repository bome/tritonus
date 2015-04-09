/*
 *	AudioFormatSet.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 by Florian Bomers <http://www.bomers.de>
 *
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

package org.tritonus.share.sampled;

import java.util.Collection;
import java.util.Iterator;

import javax.sound.sampled.AudioFormat;

import org.tritonus.share.ArraySet;
import org.tritonus.share.sampled.AudioFormats;


/**
 * A set where the elements are uniquely referenced by
 * AudioFormats.equals rather than their object reference.
 * No 2 equal AudioFormats can exist in the set.
 * <p>
 * This class provide convenience methods like
 * <code>getAudioFormat(AudioFormat)</code> and
 * <code>matches(AudioFormat)</code>.
 * <p>
 * The <code>contains(Object elem)</code> and <code>get(Object elem)</code>
 * fail, if elem is not an instance of AudioFormat.
 * <p>
 * You shouldn't use the ArrayList specific functions
 * like those that take index parameters.
 * <p>
 * It is not possible to add <code>null</code> elements.
 * <p>
 * Currently, the methods equals(.,.) and matches(.,.) of
 * class AudioFormats are used. Let's hope that they will
 * be integrated into AudioFormat.
 */

public class AudioFormatSet extends ArraySet<AudioFormat>
{
	private static final long serialVersionUID = 1;
	
	protected static final AudioFormat[]		EMPTY_FORMAT_ARRAY = new AudioFormat[0];

	public AudioFormatSet() {
		super();
	}

	public AudioFormatSet(Collection<AudioFormat> c) {
		super(c);
	}

	@Override
	public boolean add(AudioFormat elem) {
		if (elem==null) {
			return false;
		}
		return super.add(elem);
	}

	public boolean contains(AudioFormat elem) {
		if (elem==null) {
			return false;
		}
		AudioFormat comp= elem;
		Iterator<AudioFormat> it=iterator();
		while (it.hasNext()) {
			if (AudioFormats.equals(comp, it.next())) {
				return true;
			}
		}
		return false;
	}

	public AudioFormat get(AudioFormat elem) {
		if (elem==null) {
			return null;
		}
		AudioFormat comp= elem;
		Iterator<AudioFormat> it=iterator();
		while (it.hasNext()) {
			AudioFormat thisElem=it.next();
			if (AudioFormats.equals(comp, thisElem)) {
				return thisElem;
			}
		}
		return null;
	}

	public AudioFormat getAudioFormat(AudioFormat elem) {
		return get(elem);
	}

	/**
	 * Checks whether this Set contains an AudioFormat
	 * that matches <code>elem</code>.
	 * The first matching format is returned. If no element
	 * matches <code>elem</code>, <code>null</code> is returned.
	 * <p>
	 * @see AudioFormats#matches(AudioFormat, AudioFormat)
	 */
	public AudioFormat matches(AudioFormat elem) {
		if (elem==null) {
			return null;
		}
		Iterator<AudioFormat> it=iterator();
		while (it.hasNext()) {
			AudioFormat thisElem=it.next();
			if (AudioFormats.matches(elem, thisElem)) {
				return thisElem;
			}
		}
		return null;
	}


	// $$mp: TODO: remove; should be obsolete
	public AudioFormat[] toAudioFormatArray() {
		return toArray(EMPTY_FORMAT_ARRAY);
	}


	@Override
	public void add(int index, AudioFormat element) {
		throw new UnsupportedOperationException("unsupported");
	}

	@Override
	public AudioFormat set(int index, AudioFormat element) {
		throw new UnsupportedOperationException("unsupported");
	}
}

/*** AudioFormatSet.java ***/
