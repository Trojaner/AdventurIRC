package de.static_interface.shadow.adventurirc.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import de.static_interface.shadow.adventurirc.AdventurIRC;

public class NetworkManager
{
	private static PircBotX bot;
	
	public static String channelName = "#adventuriabot";
	
	public static void connect(String username)
	{
		Configuration<PircBotX> config = new Configuration.Builder<PircBotX>()
												.setName(username)
												.setFinger(username)
												.setVersion(AdventurIRC.VERSION)
												.addListener(new ChatListener())
												.setLogin(username)
												.addAutoJoinChannel(channelName)
												.setEncoding(StandardCharsets.UTF_8)
												.setServer("irc.adventuria.eu", 6667)
												.buildConfiguration();
		
		bot = new PircBotX(config);
//		new Thread(new Connection(bot)).start();
//		try
//		{
//			Thread.sleep(1500);
//		}
//		catch (InterruptedException e)
//		{
//			e.printStackTrace();
//		}
		AdventurIRC.mainFrame.getChannel(bot.getUserChannelDao().getChannel(NetworkManager.channelName));
	}
	
	public static User getUser(String username)
	{
		return bot.getUserChannelDao().getUser(username);
	}
	
	public static Channel joinChannel(String channelname)
	{
		bot.sendIRC().joinChannel(channelname);
		return bot.getUserChannelDao().getChannel(channelname);
	}
	
}
class ChatListener extends ListenerAdapter<PircBotX>
{
	@Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception
	{
		AdventurIRC.mainFrame.getChannel(event.getChannel()).write(event.getUser().getNick(), event.getMessage());
	}
	
	@Override
	public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception
	{
		AdventurIRC.mainFrame.getUserPanel(event.getUser()).write(event.getUser().getNick(), event.getMessage());
	}
}
class Connection implements Runnable
{
	PircBotX bot;
	
	public Connection(PircBotX bot)
	{
		this.bot = bot;
	}

	public void run()
	{
		try
		{
			bot.startBot();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (IrcException e)
		{
			e.printStackTrace();
		}
	}
}