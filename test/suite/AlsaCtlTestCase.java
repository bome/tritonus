import	junit.framework.TestCase;

import	org.tritonus.lowlevel.alsa.AlsaCtl;



public class AlsaCtlTestCase
extends TestCase
{
	private static final boolean	DEBUG = false;
	private static final String	CARD_NAME_FOR_INDEX_TEST = "LIFE";



	public AlsaCtlTestCase(String strName)
	{
		super(strName);
	}



	public void testGetCards()
	{
		int[]	anCards = AlsaCtl.getCards();
		assert(anCards != null);
		assert(anCards.length == 1);
		assert(anCards[0] >= 0);
	}



	public void testLoadCards()
	{
		int[]	anCards = AlsaCtl.getCards();
		for (int i = 0; i < anCards.length; i++)
		{
			int	nError = AlsaCtl.loadCard(anCards[i]);
			assert(nError >= 0);
		}
	}



	public void testGetIndex()
	{
		int	nIndex = AlsaCtl.getCardIndex(CARD_NAME_FOR_INDEX_TEST);
		if (DEBUG)
		{
			System.out.println("card index: " + nIndex);
		}
		assert(nIndex >= 0);
		int[]	anCards = AlsaCtl.getCards();
		if (DEBUG)
		{
			System.out.println("card index: " + anCards[0]);
		}
		assert(nIndex == anCards[0]);
	}



	public void testGetNames()
	{
		int[]	anCards = AlsaCtl.getCards();
		String	strName = AlsaCtl.getCardName(anCards[0]);
		assert(strName != null && !strName.equals(""));
		String	strLongName = AlsaCtl.getCardLongName(anCards[0]);
		assert(strLongName != null && !strLongName.equals(""));
		assert(!strName.equals(strLongName));
		if (DEBUG)
		{
			System.out.println("card name: " + strName);
			System.out.println("card long name: " + strLongName);
		}
	}



	public void testDefaults()
	{
		int	nDefault;
		nDefault = AlsaCtl.getDefaultCard();
		assert(nDefault >= 0);
		nDefault = AlsaCtl.getDefaultMixerCard();
		assert(nDefault >= 0);
		nDefault = AlsaCtl.getDefaultPcmCard();
		assert(nDefault >= 0);
		nDefault = AlsaCtl.getDefaultPcmDevice();
		assert(nDefault >= 0);
		nDefault = AlsaCtl.getDefaultRawmidiCard();
		assert(nDefault >= 0);
		nDefault = AlsaCtl.getDefaultRawmidiDevice();
		assert(nDefault >= 0);
	}
}

