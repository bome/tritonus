/*
 *	SaslParser.java
 */

import	java.io.InputStream;
import	java.io.IOException;
import	java.io.InputStreamReader;
import	java.io.BufferedReader;

import	java.util.StringTokenizer;

import	org.tritonus.share.TDebug;



public class SaslParser
implements Runnable
{
	private RTSystem	m_rtSystem;
	private boolean		m_bRunning;
	private BufferedReader	m_bufferedReader;



	protected SaslParser(RTSystem rtSystem, InputStream inputStream)
	{
		m_rtSystem = rtSystem;
		m_bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
		while (m_bRunning)
		{
			String	strLine = m_bufferedReader.readLine();
			if (strLine == null)
			{
				/* EOF signaled.
				 */
				break;
			}
			strLine = strLine.trim();
			if (strLine.equals(""))
			{
				continue;
			}
			TDebug.out("line: " + strLine);
			String[]	astrParts = splitString(strLine);
			boolean		bHighPriority = false;
			int		nIndex = 0;
			if (astrParts[nIndex].equals("*"))
			{
				bHighPriority = true;
				nIndex++;
			}
			float	fTime = Float.parseFloat(astrParts[nIndex]);
			nIndex++;
			String	strCommandName = astrParts[nIndex];
			nIndex++;
			if (strCommandName.equals("end"))
			{
				m_rtSystem.scheduleEnd(fTime);
			}
			else
			{
				float	fDuration = Float.parseFloat(astrParts[nIndex]);
				m_rtSystem.scheduleInstrument(strCommandName, fTime, fDuration);
			}
		}
	}



	private static String[] splitString(String str)
	{
			// jdk1.4 method:
			// String[]	astrParts = str.split("\\s");

			StringTokenizer	tokenizer = new StringTokenizer(str);
			String[]	astrParts = new String[tokenizer.countTokens()];
			int	nIndex = 0;
			while (tokenizer.hasMoreTokens())
			{
				astrParts[nIndex] = tokenizer.nextToken();
				nIndex++;
			}
			return astrParts;
	}
}



/*** SaslParser.java ***/
