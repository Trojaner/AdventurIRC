package de.static_interface.shadow.adventurirc.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import de.static_interface.shadow.shadconfig.Configuration;

public class FileManager
{
	private static final AdventurIRCConfiguration programConfiguration = new AdventurIRCConfiguration();
	
	public static final String
		CFG_NICKNAME = "nickname";
	
	public static String getString(String key)
	{
		return programConfiguration.getString(key);
	}
	
	public static void setString(String key, String value)
	{
		programConfiguration.putString(key, value);
		programConfiguration.save();
	}
}
class AdventurIRCConfiguration extends Configuration
{
	private static Path getAbsoluteHomePath()
	{
		final boolean isMSWindows = System.getProperty("os.name").toLowerCase().contains("windows");
		
		Path path;
		
		if ( isMSWindows )
		{
			path = Paths.get(System.getProperty("user.home")+File.separator+"AdventurIRC");
			if ( !Files.exists(path) )
			{
				try
				{
					Files.createDirectories(path);
					Files.setAttribute(path, "dos:hidden", true);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			path = Paths.get(System.getProperty("user.home")+File.separator+".AdventurIRC");
			if ( !Files.exists(path) )
			{
				try
				{
					Files.createDirectories(path);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		path = Paths.get(path.toAbsolutePath().toString()+File.separator+"config");
		return path;
	}
	
	public AdventurIRCConfiguration()
	{
		super(getAbsoluteHomePath());
	}
}