/*
 *	AbstractInstrument.java
 */


public abstract class AbstractInstrument
{
	private int		m_nStartTime;
	private int		m_nEndTime;



	protected AbstractInstrument()
	{
	}



	// should be a constructor argument, but is not to simplify instantiation and inheritance
	public void setStartAndEndTime(int nStartTime, int nEndTime)
	{
		m_nStartTime = nStartTime;
		m_nEndTime = nEndTime;		
	}



	public int getStartTime()
	{
		return m_nStartTime;
	}



	public int getEndTime()
	{
		return m_nEndTime;
	}



	public void doIPass(RTSystem rtSystem)
	{
	}



	public void doKPass(RTSystem rtSystem)
	{
	}



	public void doAPass(RTSystem rtSystem)
	{
	}
}



/*** AbstractInstrument.java ***/
