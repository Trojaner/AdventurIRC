package de.static_interface.shadow.adventurirc.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import de.static_interface.shadow.tameru.Configuration;

public class AdventurIRCConfiguration extends Configuration
{
	protected static final Path oldWindowsPath = Paths.get(System.getProperty("user.home")+File.separator+"AdventurIRC");
	public static final Path path = Paths.get(System.getProperty("user.home")+File.separator+".AdventurIRC");

	private static Path getAbsoluteHomePath()
	{
		final boolean isMSWindows = System.getProperty("os.name").toLowerCase().contains("windows");
		
		if ( Files.exists(oldWindowsPath) )
		{
			try
			{
				Files.move(oldWindowsPath, path, StandardCopyOption.REPLACE_EXISTING);
			}
			catch (IOException e)
			{
				e.printStackTrace(FileManager.logWriter);
				FileManager.logWriter.flush();
			}
		}
		
		if ( !Files.exists(path) )
		{
			try
			{
				Files.createDirectories(path);
				if ( isMSWindows ) Files.setAttribute(path, "dos:hidden", true);
			}
			catch (IOException e)
			{
				e.printStackTrace(FileManager.logWriter);
				FileManager.logWriter.flush();
			}
		}
		return Paths.get(path.toAbsolutePath().toString()+File.separator+"config");
	}

	public AdventurIRCConfiguration()
	{
		super(getAbsoluteHomePath());
	}
}