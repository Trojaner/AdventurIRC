package de.static_interface.shadow.adventurirc.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.gui.panel.ChatPanel;
import de.static_interface.shadow.adventurirc.gui.panel.PublicChatPanel;

public class NetworkManager
{
	//server_hostname
	protected static HashMap<String, PircBotX> servers = new HashMap<String, PircBotX>();

	public static void connectToServer(String hostname, int port, String username)
	{
		Listener listener = new Listener(hostname);

		AdventurIRC.frame.HomePanel.write("Verbinde zu "+hostname);

		Configuration<PircBotX> cfg = new Configuration.Builder<PircBotX>()
									.setName(username)
									.setServer(hostname, port)
									.setFinger("You shall not finger.")
									.setVersion(AdventurIRC.VERSION)
									.setEncoding(StandardCharsets.UTF_8)
									.addListener(listener)
									.setLogin(username)
									.setLocale(Locale.GERMAN)
									.buildConfiguration();
		PircBotX bot = new PircBotX(cfg);
		new Thread(new Connection(bot)).start();
		servers.put(hostname, bot);
	}

	public static void connectToServerWithPassword(String hostname, int port, String username, String password)
	{
		connectToServer(hostname, port, username);
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace(FileManager.logWriter);
		}
		servers.get(hostname).sendIRC().message("nickserv", password);
	}

	public static void joinChannel(String hostname, String channel)
	{
		while ( !servers.get(hostname).isConnected() ) try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(FileManager.logWriter); }

		servers.get(hostname).sendIRC().joinChannel(channel);

		AdventurIRC.frame.addPublicChatPanel(hostname, channel, new PublicChatPanel(hostname, servers.get(hostname).getUserChannelDao().getChannel(channel)));
	}

	public static void partChannel(String hostname, Channel channel)
	{
		partChannel(hostname, channel, AdventurIRC.VERSION);
	}

	public static void partChannel(String hostname, Channel channel, String reason)
	{
		channel.send().part(reason);

		AdventurIRC.frame.getPublicChatPanel(hostname, channel.getName());
	}

	public static void quitServer(String hostname)
	{
		quitServer(hostname, AdventurIRC.VERSION);
	}

	public static void quitServer(String hostname, String reason)
	{
		for ( Channel c : servers.get(hostname).getUserBot().getChannels() )
		{
			partChannel(hostname, c);
		}

		servers.get(hostname).sendIRC().quitServer(reason);
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
class Listener extends ListenerAdapter<PircBotX>
{
	private String hostname;

	private User user;

	public Listener(String hostname)
	{
		this.hostname = hostname;
	}

	protected void receiveUser()
	{
		this.user = NetworkManager.servers.get(hostname).getUserBot();
	}

	protected User getUser()
	{
		return user;
	}

	@Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception
	{
		//<Time> <User>:<Message>
		AdventurIRC.frame.getPublicChatPanel(hostname, event.getChannel().getName()).write(ChatPanel.timeFormat_Chat.format(new Date())+" "+event.getUser()+" "+event.getMessage());
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception
	{
		//<Time> <User>:<Message>
		AdventurIRC.frame.getPrivateChatPanel(hostname, event.getUser().getNick()).write(ChatPanel.timeFormat_Chat.format(new Date())+" "+event.getUser()+" "+event.getMessage());
	}
}