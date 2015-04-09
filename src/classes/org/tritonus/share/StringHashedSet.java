/*
 *	StringHashedSet.java
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

package org.tritonus.share;

import java.util.Collection;
import java.util.Iterator;

import org.tritonus.share.ArraySet;


/**
 * A set where the elements are uniquely referenced by their
 * string representation as given by the objects toString()
 * method. No 2 objects with the same toString() can
 * be in the set.
 * <p>
 * The <code>contains(Object elem)</code> and <code>get(Object elem)</code>
 * methods can be called with Strings as <code>elem</code> parameter.
 * For <code>get(Object elem)</code>, the object that has been added
 * is returned, and not its String representation.
 * <p>
 * Though it's possible to store
 * Strings as objects in this class, it doesn't make sense
 * as you could use ArraySet for that equally well.
 * <p>
 * You shouldn't use the ArrayList specific functions
 * like those that take index parameters.
 * <p>
 * It is not possible to add <code>null</code> elements.
 */

public class StringHashedSet<E> extends ArraySet<E>
{
	private static final long serialVersionUID = 1;

	public StringHashedSet()
	{
		super();
	}

	public StringHashedSet(Collection<E> c)
	{
		super(c);
	}

	public boolean add(E elem)
	{
		if (elem==null) {
			return false;
		}
		return super.add(elem);
	}

	public boolean contains(Object elem)
	{
		if (elem==null) {
			return false;
		}
		String comp=elem.toString();
		Iterator<E> it=iterator();
		while (it.hasNext()) {
			if (comp.equals(it.next().toString())) {
				return true;
			}
		}
		return false;
	}

	public E get(Object elem) {
		if (elem==null) {
			return null;
		}
		String comp=elem.toString();
		Iterator<E> it=iterator();
		while (it.hasNext()) {
			E thisElem=it.next();
			if (comp.equals(thisElem.toString())) {
				return thisElem;
			}
		}
		return null;
	}

}

/*** StringHashedSet.java ***/
