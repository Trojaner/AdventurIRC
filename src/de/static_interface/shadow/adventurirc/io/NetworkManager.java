package de.static_interface.shadow.adventurirc.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.QuitEvent;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.Beeper;
import de.static_interface.shadow.adventurirc.gui.panel.ChatPanel;
import de.static_interface.shadow.adventurirc.gui.panel.PrivateChatPanel;
import de.static_interface.shadow.adventurirc.gui.panel.PublicChatPanel;

public class NetworkManager
{
	//server_hostname
	protected static HashMap<String, PircBotX> servers = new HashMap<String, PircBotX>();

	public static void connectToServer(String hostname, int port, String username)
	{
		Listener listener = new Listener(hostname);

		AdventurIRC.frame.HomePanel.write(ChatPanel.PREFIX, "Verbinde zu "+hostname+" ...");

		Configuration<PircBotX> cfg = new Configuration.Builder<PircBotX>()
									.setName(username)
									.setServer(hostname, port)
									.setFinger("You shall not finger.")
									.setVersion(AdventurIRC.VERSION)
									.setEncoding(StandardCharsets.UTF_8)
									.addListener(listener)
									.setRealName(username)
									.setLogin(username)
									.setLocale(Locale.GERMAN)
									.buildConfiguration();
		PircBotX bot = new PircBotX(cfg);
		new Thread(new Connection(bot)).start();
		servers.put(hostname, bot);
		while ( !bot.isConnected() );
		AdventurIRC.frame.HomePanel.write(ChatPanel.PREFIX, "Du bist nun mit "+hostname+" verbunden.");
		AdventurIRC.frame.addServerPanel(hostname);
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
		while ( !servers.get(hostname).isConnected() );
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

	public static void sendPrivateMessage(ChatPanel currentPanel, String hostname, String username, String message)
	{
		User u = servers.get(hostname).getUserChannelDao().getUser(username);
		if ( u == null )
		{
			currentPanel.write(ChatPanel.PREFIX, username+" ist nicht online !");
			return;
		}
		u.send().message(message);
		if ( AdventurIRC.frame.getPrivateChatPanel(hostname, username) == null )
		{
			AdventurIRC.frame.addPrivateChatPanel(hostname, username, new PrivateChatPanel(hostname, u));
		}
	}

	public static void quitServer(String hostname)
	{
		quitServer(hostname, AdventurIRC.VERSION);
	}

	public static void rename(String hostname, String newNickname)
	{
		if ( hostname.equals("*") )
		{
			for ( PircBotX bot : servers.values() )
			{
				bot.sendIRC().changeNick(newNickname);
			}
			return;
		}
		servers.get(hostname).sendIRC().changeNick(newNickname);
	}

	public static void quitServer(String hostname, String reason)
	{
		if ( hostname.equals("*") )
		{
			for ( PircBotX bot : servers.values() )
			{
				bot.sendIRC().quitServer(reason);
			}
		}
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
	public void onJoin(JoinEvent<PircBotX> event) throws Exception
	{
		AdventurIRC.frame.getPublicChatPanel(event.getBot().getConfiguration().getServerHostname(), event.getChannel().getName()).refreshUserList(true);
		AdventurIRC.frame.getPublicChatPanel(event.getBot().getConfiguration().getServerHostname(), event.getChannel().getName()).write(ChatPanel.PREFIX, event.getUser().getNick()+" ist dem Channel beigetreten !");
	}

	@Override
	public void onNickChange(NickChangeEvent<PircBotX> event) throws Exception
	{
		PrivateChatPanel oldPanel = AdventurIRC.frame.getPrivateChatPanel(hostname, event.getOldNick());
		AdventurIRC.frame.remove(oldPanel);
		AdventurIRC.frame.addPrivateChatPanel(hostname, event.getNewNick(), oldPanel);
		for ( PublicChatPanel panel : AdventurIRC.frame.getPublicChatPanels(event.getBot().getConfiguration().getServerHostname()) )
		{
			panel.refreshUserList(true);
		}
	}

	@Override
	public void onQuit(QuitEvent<PircBotX> event) throws Exception
	{
		AdventurIRC.frame.getPrivateChatPanel(hostname, event.getUser().getNick()).write(ChatPanel.PREFIX, event.getUser().getNick()+" hat den Server verlassen. ("+event.getReason()+")");
	}

	@Override
	public void onPart(PartEvent<PircBotX> event) throws Exception
	{
		AdventurIRC.frame.getPublicChatPanel(event.getBot().getConfiguration().getServerHostname(), event.getChannel().getName()).refreshUserList(true);
		if ( !NetworkManager.servers.get(hostname).getUserChannelDao().userExists(event.getUser().getNick()) ) AdventurIRC.frame.getPublicChatPanel(hostname, event.getChannel().getName()).write(ChatPanel.PREFIX, event.getUser().getNick()+" hat den Server verlassen. ("+event.getReason()+")");
		else AdventurIRC.frame.getPublicChatPanel(event.getBot().getConfiguration().getServerHostname(), event.getChannel().getName()).write(ChatPanel.PREFIX, event.getUser().getNick()+" hat den Channel verlassen !");
	}

	@Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception
	{
		AdventurIRC.frame.getPublicChatPanel(hostname, event.getChannel().getName()).write(event.getUser().getNick(), event.getMessage());
		if ( event.getMessage().contains(AdventurIRC.nickname) ) Beeper.beep();
	}

	@Override
	public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception
	{
		if ( AdventurIRC.frame.getPrivateChatPanel(hostname, event.getUser().getNick()) == null ) AdventurIRC.frame.addPrivateChatPanel(hostname, event.getUser().getNick(), new PrivateChatPanel(hostname, event.getUser()));
		AdventurIRC.frame.getPrivateChatPanel(hostname, event.getUser().getNick()).write(event.getUser().getNick(), event.getMessage());
		Beeper.beep();
	}
}