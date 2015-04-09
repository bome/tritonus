/*
 *	TInit.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer
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

package org.tritonus.core;

import java.util.Iterator;

import org.tritonus.core.Service;
import org.tritonus.share.TDebug;



/** Helper methods for provider registration.
 */
public class TInit
{
	/** Constructor to prevent instantiation.
	 */
	private TInit()
	{
	}



	/** Register all service providers of a certain type.
	    This method retrieves instances of all service providers of
	    the type given as providerClass. It registers them by
	    calling action with the provider instance as actual parameter.

	    @param providerClass Type of the service providers that should
	    be registered. For instance, this could be the class object for
	    javax.sound.sampled.spi.MixerProvider. However, the mechanism
	    is not restricted to the Java Sound types of service providers.

	    @param action A ProviderRegistrationAction that should to be
	    called to register the service providers. Typically, this is
	    something like adding the provider to a collection, but in
	    theorie, could be anything.
	*/
	public static void registerClasses(Class providerClass,
					    ProviderRegistrationAction action)
	{
		if (TDebug.TraceInit) { TDebug.out("TInit.registerClasses(): registering for: " + providerClass); }
		Iterator	providers = Service.providers(providerClass);
		if (providers != null)
		{
			while (providers.hasNext())
			{
				Object	provider = providers.next();
				try
				{
					action.register(provider);
				}
				catch (Throwable e)
				{
					if (TDebug.TraceInit || TDebug.TraceAllExceptions) { TDebug.out(e); }
				}
			}
		}
	}



	/** Action to be taken on registration of a provider.
	    Strategy objects of this type has to be passed to
	    {@link #registerClasses registerClasses}. The implementation
	    is called for each provider that has to be registered.
	 */
	public static interface ProviderRegistrationAction
	{
		public void register(Object provider)
			throws Exception;
	}
}



/*** TInit.java ***/
