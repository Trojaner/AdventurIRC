package de.static_interface.shadow.adventurirc.io;

import java.util.Random;

public class FileManager
{
	private static final AdventurIRCConfiguration programConfiguration = new AdventurIRCConfiguration();

	public static final AdventurIRCErrorLogWriter logWriter = AdventurIRCErrorLogWriter.createLogWriter();

	public static final String
		CFG_NICKNAME = "nickname",
		CFG_DOBEEP = "beep",
		CFG_TIME_FORMAT_LOG = "time_format_log",
		CFG_TIME_FORMAT_CHAT = "time_format_chat",
		CFG_CHAT_OUTPUT_FORMAT = "chat_output_format",
		CFG_LOG_CHAT_OUTPUT = "log_chat_output";

	public static String getString(String key)
	{
		String string = programConfiguration.getString(key);
		if ( string == null )
		{
			if ( key.equals(CFG_NICKNAME) )
			{
				String name = "AdventuriaJin"+(new Random()).nextInt(5);
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
				setString(CFG_TIME_FORMAT_CHAT+"_comment", "(For chat output) For possible format symbols see http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html");
				setString(key, "HH:mm:ss");
				return "HH:mm:ss";
			}
			if ( key.equals(CFG_TIME_FORMAT_LOG) )
			{
				setString(CFG_TIME_FORMAT_LOG+"_comment", "(For log files) For possible format symbols see http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html");
				setString(key, "dd.MM.YYYY");
				return "DD.MM.YYYY";
			}
			if ( key.equals(CFG_CHAT_OUTPUT_FORMAT) )
			{
				setString(CFG_CHAT_OUTPUT_FORMAT+"_comment", "This formats the chat output in the client. Default is [%s] %s: %s, while first %s is the time, the second is the name and the third one is the chat message.");
				setString(key, "[%s] %s: %s");
				return "[%s] %s: %s";
			}
			if ( key.equals(CFG_LOG_CHAT_OUTPUT) )
			{
				setString(CFG_LOG_CHAT_OUTPUT+"_comment", "This logs the chat into files. The path will be in .AdventurIRC/logs/<servername>/<channelname>.txt");
				setString(key, "true");
				return "true";
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