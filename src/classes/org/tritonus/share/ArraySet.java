/*
 *	ArraySet.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 -2004 by Matthias Pfisterer
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;



public class ArraySet<E>
extends ArrayList<E>
implements Set<E>
{
	private static final long serialVersionUID = 1;

	public ArraySet()
	{
		super();
	}



	public ArraySet(Collection<E> c)
	{
		this();
		addAll(c);
	}



	public boolean add(E element)
	{
		if (!contains(element))
		{
			super.add(element);
			return true;
		}
		else
		{
			return false;
		}
	}



	public void add(int index, E element)
	{
		throw new UnsupportedOperationException("ArraySet.add(int index, Object element) unsupported");
	}

	public E set(int index, E element)
	{
		throw new UnsupportedOperationException("ArraySet.set(int index, Object element) unsupported");
	}

}



/*** ArraySet.java ***/
