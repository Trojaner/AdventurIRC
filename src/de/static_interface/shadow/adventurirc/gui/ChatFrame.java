package de.static_interface.shadow.adventurirc.gui;

import java.util.HashMap;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.pircbotx.Channel;
import org.pircbotx.User;

import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ChatFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	HashMap<UUID, PrivateChatPanel> private_chats = new HashMap<UUID, PrivateChatPanel>();
	HashMap<Channel, PublicChatPanel> public_chats = new HashMap<Channel, PublicChatPanel>();
	
	JTabbedPane content = new JTabbedPane();
	
	public ChatFrame()
	{
		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				for ( ChatPanel p : private_chats.values() ) p.resize();
				for ( ChatPanel p : public_chats.values() ) p.resize();
			}
		});
		setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2);
		getContentPane().add(content);
		content.setBounds(5, 5, (int) getSize().getWidth()-5, (int) getSize().getHeight()-5);
	}
	
	private void addPrivateChatPanel(User u)
	{
		private_chats.put(u.getUserId(), new PrivateChatPanel(u));
		content.add(private_chats.get(u.getUserId()));
		content.repaint();
	}
	
	private void addPublicChatPanel(Channel c)
	{
		public_chats.put(c, new PublicChatPanel(c));
		content.add(public_chats.get(c));
		content.repaint();
	}
	
	public PublicChatPanel getChannel(Channel c)
	{
		if ( public_chats.get(c) == null ) addPublicChatPanel(c);
		System.out.println(public_chats.get(c));
		return public_chats.get(c);
	}
	
	public PrivateChatPanel getUserPanel(User u)
	{
		if ( private_chats.get(u) == null ) addPrivateChatPanel(u);
		return private_chats.get(u);
	}
}