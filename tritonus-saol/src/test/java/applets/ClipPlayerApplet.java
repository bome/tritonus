/*
 *	ClipPlayerApplet.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer
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
 */

package applets;

import	java.awt.event.ActionEvent;
import	java.awt.event.ActionListener;

import	java.io.IOException;

import	java.net.URL;
import	java.net.MalformedURLException;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.Clip;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.LineEvent;
import	javax.sound.sampled.LineListener;
import	javax.sound.sampled.LineUnavailableException;

import	javax.swing.JApplet;
import	javax.swing.JButton;
import	javax.swing.JPanel;


public class ClipPlayerApplet
	extends	JApplet
	implements	LineListener
{
	private AudioInputStream	m_audioInputStream;
	private AudioFormat		m_format;
	private Clip		m_clip;

	private JPanel		m_panel;
	private JButton		m_loopButton;
	private JButton		m_stopButton;


	public ClipPlayerApplet()
	{
	}



	public void init()
	{
		System.out.println("ClipPlayerApplet.init(): context class loader: " + Thread.currentThread().getContextClassLoader());
		System.out.println("ClipPlayerApplet.init(): system class loader: " + ClassLoader.getSystemClassLoader());
		String	strClipURL = getParameter("clipurl");
		System.out.println("URL str: " + strClipURL);
		URL	clipURL = null;
		try
		{
			clipURL = new URL(getDocumentBase(), strClipURL);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		System.out.println("URL: " + clipURL);
		loadClip(clipURL);
		JPanel	panel = new JPanel();
		this.getContentPane().add(panel);
		// TODO: label showing the url
		m_loopButton = new JButton("Loop");
		m_loopButton.addActionListener(new ActionListener()
					       {
						       public void actionPerformed(ActionEvent ae)
							       {
								       m_clip.loop(Clip.LOOP_CONTINUOUSLY);
							       }
					       });
		panel.add(m_loopButton);
		m_stopButton = new JButton("Stop");
		m_stopButton.addActionListener(new ActionListener()
					       {
						       public void actionPerformed(ActionEvent ae)
							       {
								       m_clip.loop(0);
							       }
					       });
		m_stopButton.setEnabled(false);
		panel.add(m_stopButton);
	}



	public void destroy()
	{
		if (m_clip != null)
		{
			m_clip.close();
		}
	}



	private void loadClip(URL clipURL)
	{
		System.out.println("ClipPlayerApplet.loadClip(): setting another class loader");
		ClassLoader	originalClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
		try
		{
			m_audioInputStream = AudioSystem.getAudioInputStream(clipURL);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (m_audioInputStream != null)
		{
			m_format = m_audioInputStream.getFormat();
			DataLine.Info	info = new DataLine.Info(Clip.class, m_format, AudioSystem.NOT_SPECIFIED);
			try
			{
				m_clip = (Clip) AudioSystem.getLine(info);
				m_clip.addLineListener(this);
				m_clip.open(m_audioInputStream);
			}
			catch (LineUnavailableException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			// m_clip.loop(nLoopCount);
		}
		else
		{
			// TODO: popup (also for other error conditions)
			System.out.println("ClipPlayerApplet.<init>(): can't get data from URL " + clipURL);
		}
		Thread.currentThread().setContextClassLoader(originalClassLoader);
		System.out.println("ClipPlayerApplet.loadClip(): restored the original class loader");
	}



	public void update(LineEvent event)
	{
		System.out.println("ClipPlayerApplet.update(): received event: " + event);
		if (event.getType().equals(LineEvent.Type.START))
		{
			m_loopButton.setEnabled(false);
			m_stopButton.setEnabled(true);
		}
		if (event.getType().equals(LineEvent.Type.STOP))
		{
			m_loopButton.setEnabled(true);
			m_stopButton.setEnabled(false);
		}
	}


}



/*** ClipPlayerApplet.java ***/
