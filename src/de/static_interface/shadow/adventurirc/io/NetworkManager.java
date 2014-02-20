package de.static_interface.shadow.adventurirc.io;

import java.io.IOException;

import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.gui.ChatFrame;

public class NetworkManager
{
	public static final String channelName = "#advTest";
	
	static Connection connection;
	
	static Channel channelInstance;
	
	public static void connect(String nickname)
	{
		Configuration<PircBotX> cfg = new Configuration.Builder<PircBotX>().setName(nickname).setLogin(nickname).setVersion(AdventurIRC.VERSION).setFinger(nickname).addAutoJoinChannel(channelName).addListener(new ChatListener()).setServer("irc.adventuria.eu", 6667).buildConfiguration();
		PircBotX bot = new PircBotX(cfg);
		connection = new Connection(bot);
		new Thread(connection).start();
		channelInstance = connection.bot.getUserChannelDao().getChannel(channelName);
	}
	
	public static Channel getChannelInstance()
	{
		return channelInstance;
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
		catch ( IrcException e )
		{
			e.printStackTrace();
		}
	}
}
class ChatListener extends ListenerAdapter<PircBotX>
{
	@Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception
	{
		ChatFrame.mainChatFrame.write(event);
		ChatFrame.mainChatFrame.insertUserList();
	}
	
	@Override
	public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception
	{
		ChatFrame.mainChatFrame.write(event);
	}
}