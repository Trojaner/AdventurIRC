package de.static_interface.shadow.adventurirc.gui;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;


import de.static_interface.shadow.adventurirc.gui.panel.ChatPanel;
import de.static_interface.shadow.adventurirc.gui.panel.HomePanel;
import de.static_interface.shadow.adventurirc.gui.panel.PrivateChatPanel;
import de.static_interface.shadow.adventurirc.gui.panel.PublicChatPanel;
import de.static_interface.shadow.adventurirc.gui.panel.ServerPanel;
import de.static_interface.shadow.adventurirc.io.NetworkManager;
import de.static_interface.shadow.adventurirc.io.OptionPanel;

public class IRCFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	//String = server_ip.nickname
	private static final HashMap<String, PrivateChatPanel> privatechatpanels = new HashMap<String, PrivateChatPanel>();
	
	//String = server_ip.channelname
	private static final HashMap<String, PublicChatPanel> publicchatpanels = new HashMap<String, PublicChatPanel>();

	//String = server_ip
	private static final HashMap<String, ServerPanel> serverpanels = new HashMap<String, ServerPanel>();

	public final HomePanel HomePanel = new HomePanel();
	private final OptionPanel optionPanel = new OptionPanel();

	private static JTabbedPane tabbedContents = new JTabbedPane();

	public IRCFrame()
	{
		setSize(400, 250);
		addContent(ChatPanel.PREFIX, HomePanel);
		add(tabbedContents);
		tabbedContents.add(optionPanel, "Optionen");
		addWindowListener(
		new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				NetworkManager.quitServer("irc.adventuria.eu");
				dispose();
			}
		}
		);

		addMouseListener(
		new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				System.out.println("~");

				if ( e.getComponent().getSize().getHeight() < 144 )
				{
					e.getComponent().setSize(e.getComponent().getWidth(), 144);
				}
				if ( e.getComponent().getSize().getWidth() < 400 )
				{
					e.getComponent().setSize(400, e.getComponent().getHeight());
				}

				for ( Component c : tabbedContents.getComponents() )
				{
					c.setSize(getSize());
					((ChatPanel) c).resizeComponents();
				}
			}
		});

		addComponentListener(
		new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				if ( e.getComponent().getSize().getHeight() < 144 )
				{
					e.getComponent().setSize(e.getComponent().getWidth(), 144);
				}
				if ( e.getComponent().getSize().getWidth() < 400 )
				{
					e.getComponent().setSize(400, e.getComponent().getHeight());
				}

				for ( Component c : tabbedContents.getComponents() )
				{
					c.setSize(getSize());
					if ( c instanceof ChatPanel ) ((ChatPanel) c).resizeComponents();
				}
			}
		});
		setVisible(true);
	}

	private void addContent(String title, ChatPanel p)
	{
		tabbedContents.add(title, p);
	}
	
	private void removeContent(ChatPanel p)
	{
		tabbedContents.remove(p);
	}

	public void addServerPanel(String server)
	{
		serverpanels.put(server, new ServerPanel(server));
		addContent(server, serverpanels.get(server));
	}

	public ServerPanel getServerPanel(String server)
	{
		return serverpanels.get(server);
	}

	public Vector<PrivateChatPanel> getPrivateChatPanels(String server)
	{
		Vector<PrivateChatPanel> panels = new Vector<PrivateChatPanel>();
		for ( Entry<String, PrivateChatPanel> p : privatechatpanels.entrySet() )
		{
			if ( p.getKey().equals(server) ) panels.add(p.getValue());
		}
		return panels;
	}

	public Vector<PublicChatPanel> getPublicChatPanels(String server)
	{
		Vector<PublicChatPanel> panels = new Vector<PublicChatPanel>();
		for ( Entry<String, PublicChatPanel> p : publicchatpanels.entrySet() )
		{
			if ( p.getKey().equals(server) ) panels.add(p.getValue());
		}
		return panels;
	}

	public PrivateChatPanel getPrivateChatPanel(String server, String username)
	{
		return privatechatpanels.get(server+"."+username);
	}

	public void addPrivateChatPanel(String server, String username, PrivateChatPanel panel)
	{
		privatechatpanels.put(server+"."+username, panel);
		addContent(server+"."+username, panel);
	}

	public void removePrivateChatPanel(String server, String username)
	{
		removeContent(privatechatpanels.get(server+"."+username));
		privatechatpanels.remove(server+"."+username);
	}

	public PublicChatPanel getPublicChatPanel(String server, String channelname)
	{
		return publicchatpanels.get(server+"."+channelname);
	}

	public void addPublicChatPanel(String server, String channelname, PublicChatPanel panel)
	{
		publicchatpanels.put(server+"."+channelname, panel);
		addContent(server+"."+channelname, panel);
	}

	public void removePublicChatPanel(String server, String channelname)
	{
		removeContent(privatechatpanels.get(server+"."+channelname));
		publicchatpanels.remove(server+"."+channelname);
	}
}