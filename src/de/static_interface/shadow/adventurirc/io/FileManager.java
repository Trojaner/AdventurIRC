package de.static_interface.shadow.adventurirc.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

import de.static_interface.shadow.tameru.Configuration;

public class FileManager
{
	private static final AdventurIRCConfiguration programConfiguration = new AdventurIRCConfiguration();

	public static final AdventurIRCLogWriter logWriter = AdventurIRCLogWriter.createLogWriter();

	public static final String
		CFG_NICKNAME = "nickname",
		CFG_DOBEEP = "beep",
		CFG_TIME_FORMAT_LOG = "time_format_log",
		CFG_TIME_FORMAT_CHAT = "time_format_chat";

	public static String getString(String key)
	{
		String string = programConfiguration.getString(key);
		if ( string == null )
		{
			if ( key.equals(CFG_NICKNAME) )
			{
				String name = "AdventuriaJin"+(new Random()).nextInt();
				setString(key, name);
				return name;
			}
			if ( key.equals(CFG_DOBEEP) )
			{
				setString(key, "true");
				return "true";
			}
			if ( key.equals(CFG_TIME_FORMAT_CHAT) )
			{
				setString("CFG_TIME_FORMAT_COMMENT", "(For chat output) For possible format symbols see http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html");
				setString(key, "<HH:mm:ss> ");
				return "<HH:mm:ss>";
			}
			if ( key.equals(CFG_TIME_FORMAT_LOG) )
			{
				setString("CFG_TIME_FORMAT_LOG", "(For log files) For possible format symbols see http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html");
				setString(key, "<dd.MM.YYYY>");
				return "<dd.MM.YYYY>";
			}
		}
		
		return string;
	}

	public static void setString(String key, String value)
	{
		programConfiguration.deleteString(key);
		programConfiguration.putString(key, value);
		programConfiguration.save();
	}
}
class AdventurIRCConfiguration extends Configuration
{
	protected static final Path oldWindowsPath = Paths.get(System.getProperty("user.home")+File.separator+"AdventurIRC");
	protected static final Path path = Paths.get(System.getProperty("user.home")+File.separator+".AdventurIRC");

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