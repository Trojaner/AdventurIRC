package de.static_interface.shadow.adventurirc.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import de.static_interface.shadow.adventurirc.AdventurIRC;

public class NetworkManager
{
	//server_hostname
	private static HashMap<String, PircBotX> servers = new HashMap<String, PircBotX>();

	public static void connectToServer(String hostname, int port, String username)
	{
		Configuration<PircBotX> cfg = new Configuration.Builder<PircBotX>()
									.setName(username)
									.setServer(hostname, port)
									.setFinger("You shall not finger.")
									.setVersion(AdventurIRC.VERSION)
									.setEncoding(StandardCharsets.UTF_8)
									.setLogin(username)
									.setLocale(Locale.GERMAN)
									.buildConfiguration();
		PircBotX bot = new PircBotX(cfg);
		servers.put(hostname, bot);
	}

	public static void connectToServerWithPassword(String hostname, int port, String username, String password)
	{
		
	}
}
class Connection extends Thread
{
	PircBotX bot;

	public Connection(PircBotX bot)
	{
		this.bot = bot;
	}

	@Override
	public void run()
	{
		try
		{
			bot.startBot();
		}
		catch (IOException e)
		{
			e.printStackTrace(FileManager.logWriter);
			FileManager.logWriter.flush();
		}
		catch (IrcException e)
		{
			e.printStackTrace(FileManager.logWriter);
			FileManager.logWriter.flush();
		}
	}
}