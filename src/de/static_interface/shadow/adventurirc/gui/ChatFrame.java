package de.static_interface.shadow.adventurirc.gui;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.io.NetworkManager;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ChatFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTabbedPane contentPane = new JTabbedPane();
	private PublicChatPanel channelPanel = new PublicChatPanel(NetworkManager.getChannelInstance());
	private HashMap<UUID, PrivateChatPanel> chatPanels = new HashMap<UUID, PrivateChatPanel>();
	
	public static final ChatFrame mainChatFrame = new ChatFrame();
	
	public ChatFrame()
	{
		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent arg0)
			{
				int sizeX = (int) arg0.getComponent().getSize().getWidth();
				int sizeY = (int) arg0.getComponent().getSize().getHeight();
				
				for ( PrivateChatPanel p : chatPanels.values() )
				{
					p.matchSize(sizeX, sizeY);
				}
				
				channelPanel.matchSize(sizeX, sizeY);
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setContentPane(contentPane);
		contentPane.addTab(NetworkManager.channelName, channelPanel);
	}
	
	private void writeToChannel(MessageEvent<PircBotX> e)
	{
		channelPanel.insertString(String.format("%s <%s>: %s", AdventurIRC.timeFormat.format(new Date()), e.getUser().getNick(), e.getMessage()));
	}
	
	public void write(Event<PircBotX> event)
	{
		if ( event instanceof MessageEvent<?> )
		{
			writeToChannel((MessageEvent<PircBotX>) event);
			return;
		}
		else
		{
			writeToUser((PrivateMessageEvent<PircBotX>) event);
		}
	}
	
	private void writeToUser(PrivateMessageEvent<PircBotX> e)
	{
		PrivateChatPanel chatPanel = chatPanels.get(e.getUser().getUserId());
		if ( chatPanel == null )
		{
			addChat(e.getUser());
			writeToUser(e);
			return;
		}
		chatPanel.insertString(String.format("%s <%s>: %s", AdventurIRC.timeFormat.format(new Date()), e.getUser().getNick(), e.getMessage()));
	}
	
	public void addChat(User u)
	{
		if ( !(chatPanels.get(u.getUserId()) == null) ) return;
		PrivateChatPanel chatPanel = new PrivateChatPanel(u);
		chatPanels.put(u.getUserId(), chatPanel);
		contentPane.addTab(u.getNick(), chatPanel);
		chatPanel.matchSize((int) this.getSize().getWidth(),(int) this.getSize().getHeight());
	}
	
	public void insertUserList()
	{
		channelPanel.rewriteUserList();
	}
}
