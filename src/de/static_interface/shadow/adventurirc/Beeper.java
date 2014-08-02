package de.static_interface.shadow.adventurirc;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.static_interface.shadow.adventurirc.io.FileManager;

public class Beeper implements Runnable
{
	Beeper()
	{
	}

	@Override
	public void run()
	{

		if ( !FileManager.getString(FileManager.CFG_DOBEEP).equals("true") ) return;

		try
		{
			URL defaultsound = getClass().getResource("/de/static_interface/shadow/adventurirc/Randomize.wav");
			File soundfile = new File(defaultsound.toURI());
			AudioInputStream stream = AudioSystem.getAudioInputStream(soundfile);
			Clip clip = AudioSystem.getClip();
			clip.open(stream);
			clip.start();
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
	}

	public static void beep()
	{
		new Thread(new Beeper()).start();
	}
}
