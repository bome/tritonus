import	java.util.List;

import	junit.framework.TestCase;

import	org.tritonus.lowlevel.alsa.AlsaCtl;
import	org.tritonus.lowlevel.alsa.AlsaMixer;



public class AlsaMixerTestCase
extends TestCase
{
	private static final boolean	DEBUG = true;



	public AlsaMixerTestCase(String strName)
	{
		super(strName);
	}



	public void testOpenClose()
		throws Exception
	{
		int	nDefaultMixerCard = AlsaCtl.getDefaultMixerCard();
		String	strMixerName = "hw:" + nDefaultMixerCard;
		AlsaMixer	mixer = new AlsaMixer(strMixerName);
		assert(mixer != null);
		mixer.close();
		// Intentionally a second time to test idempotence of close().
		mixer.close();
	}



	public void testControls()
		throws Exception
	{
		int	nDefaultMixerCard = AlsaCtl.getDefaultMixerCard();
		String	strMixerName = "hw:" + nDefaultMixerCard;
		AlsaMixer	mixer = new AlsaMixer(strMixerName);
		List	controlsList = mixer.getControls();
		assert(controlsList != null);
		assert(controlsList.size() > 0);
		mixer.close();
	}

}

