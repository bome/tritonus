/*
 *	RTSystem.java
 */

import	java.io.IOException;

import	java.util.LinkedList;
import	java.util.Iterator;
import	java.util.List;

/*
 *      Tritonus classes.
 *      Using these makes the program not portable to other
 *      Java Sound implementations.
 */
import  org.tritonus.share.TDebug;



// IDEA: isolate output into an 'interfaced' class
public class RTSystem
extends Thread
{
	private Output			m_output;
	private boolean			m_bRunning;
	private int			m_nTime;
	private float			m_fTimeStep;
	private int			m_nARate;
	private int			m_nKRate;
	private int			m_nAToKRateFactor;
	private List			m_activeInstruments;
	private List			m_scheduledInstruments;
	private int			m_nScheduledEndTime;
	private float			m_fFloatToIntTimeFactor;
	private float			m_fIntToFloatTimeFactor;



	public RTSystem(Output output)
	{
		m_output = output;
		setRates(44100, 100);
		m_activeInstruments = new LinkedList();
		m_scheduledInstruments = new LinkedList();
		m_nScheduledEndTime = Integer.MAX_VALUE;
	}



	private void setRates(int nARate, int nKRate)
	{
		m_nARate = nARate;
		m_nKRate = nKRate;
		m_nAToKRateFactor = nARate / nKRate;
		m_fTimeStep = 1.0F / (float) nKRate;
		// following is only correct for 60 BPM
		m_fFloatToIntTimeFactor = (float) nKRate;
		m_fIntToFloatTimeFactor = m_fTimeStep;
	}



	public void run()
	{
		try
		{
			runImpl();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}



	private void runImpl()
		throws IOException
	{
		m_bRunning = true;
		m_nTime = 0;
		while (m_bRunning)
		{
			doI();
			doK();
			for (int i = 0; i < m_nAToKRateFactor; i++)
			{
				doA();
			}
			advanceTime();
		}
		m_output.close();
	}



	private void doI()
	{
		TDebug.out("doI()");
		TDebug.out("time: " + getTime());
		synchronized (m_scheduledInstruments)
		{
			Iterator	scheduledInstruments = m_scheduledInstruments.iterator();
			while (scheduledInstruments.hasNext())
			{
				TDebug.out("scheduled instrument");
				AbstractInstrument	instrument = (AbstractInstrument) scheduledInstruments.next();
				TDebug.out("instrument start time: " + instrument.getStartTime());
				if (getTime() >= instrument.getStartTime())
				{
					TDebug.out("...activating");
					scheduledInstruments.remove();
					instrument.doIPass(this);
					m_activeInstruments.add(instrument);
				}
			}
		}
		Iterator	activeInstruments = m_activeInstruments.iterator();
		while (activeInstruments.hasNext())
		{
			AbstractInstrument	instrument = (AbstractInstrument) activeInstruments.next();
			if (getTime() > instrument.getEndTime())
			{
					TDebug.out("...DEactivating");
				activeInstruments.remove();
			}
		}
		if (getTime() >= getScheduledEndTime())
		{
			stopEngine();
		}
	}



	private void doK()
	{
		Iterator	activeInstruments = m_activeInstruments.iterator();
		while (activeInstruments.hasNext())
		{
			AbstractInstrument	instrument = (AbstractInstrument) activeInstruments.next();
			instrument.doKPass(this);
		}
	}



	private void doA()
		throws IOException
	{
		// TDebug.out("doA()");
		m_output.init();
		Iterator	activeInstruments = m_activeInstruments.iterator();
		while (activeInstruments.hasNext())
		{
			// TDebug.out("doA(): has active Instrument");
			AbstractInstrument	instrument = (AbstractInstrument) activeInstruments.next();
			instrument.doAPass(this);
		}
		m_output.emit();
	}



	public void scheduleInstrument(String strInstrumentName, float fStartTime, float fDuration)
	{
		AbstractInstrument	instrument = createInstrumentInstance(strInstrumentName);
		int			nStartTime = Math.round(fStartTime * m_fFloatToIntTimeFactor);
		int			nEndTime = Math.round((fStartTime + fDuration) * m_fFloatToIntTimeFactor);
		instrument.setStartAndEndTime(nStartTime, nEndTime);
		synchronized (m_scheduledInstruments)
		{
			m_scheduledInstruments.add(instrument);
		TDebug.out("adding instrument");
		TDebug.out("start: " + nStartTime);
		TDebug.out("end: " + nEndTime);
		}
	}



	public void scheduleEnd(float fEndTime)
	{
		m_nScheduledEndTime = Math.round(fEndTime * m_fFloatToIntTimeFactor);
		// TODO:
	}



	public void stopEngine()
	{
		m_bRunning = false;
	}



	private void advanceTime()
	{
		m_nTime++;
	}


	public int getTime()
	{
		return m_nTime;
	}

	public void output(float fValue)
	{
		m_output.output(fValue);
	}



	private int getScheduledEndTime()
	{
		return m_nScheduledEndTime;
	}



	private AbstractInstrument createInstrumentInstance(String strInstrumentName)
	{
		// TODO:
		// return new Instrument_tone();
		return new tone();
	}
}



/*** RTSystem.java ***/
