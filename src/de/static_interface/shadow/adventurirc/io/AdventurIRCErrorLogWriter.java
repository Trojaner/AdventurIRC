package de.static_interface.shadow.adventurirc.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Date;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.gui.panel.ChatPanel;

public class AdventurIRCErrorLogWriter extends PrintStream
{
	private AdventurIRCErrorLogWriter() throws FileNotFoundException
	{
		super(AdventurIRCConfiguration.path.toAbsolutePath().toString()+File.separator+"logs"+File.separator+ChatPanel.timeFormat_Log.format(new Date()));
	}

	private AdventurIRCErrorLogWriter(boolean stdout)
	{
		super(System.out);
	}

	@Override
	public void println(Object x)
	{
		AdventurIRC.frame.HomePanel.write("", x.toString());
		super.println(x);
	}

	public void printFileOnly(String x)
	{
		super.println(x);
		flush();
	}

	@Override
	public void println(String x)
	{
		AdventurIRC.frame.HomePanel.write("", x);
		super.println(x);
	}

	public static AdventurIRCErrorLogWriter createLogWriter()
	{
		File log = new File(AdventurIRCConfiguration.path.toAbsolutePath().toString()+File.separator+"logs"+File.separator+ChatPanel.timeFormat_Log.format(new Date()));

		try
		{
			if ( !log.getParentFile().exists() ) Files.createDirectories(log.getParentFile().toPath());
			while ( !log.getParentFile().exists() );
			if ( !log.exists() ) Files.createFile(log.toPath());
			while ( !log.exists() );
			return new AdventurIRCErrorLogWriter();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return new AdventurIRCErrorLogWriter(true);
		}
	}
}