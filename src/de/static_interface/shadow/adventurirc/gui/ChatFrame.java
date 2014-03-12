package de.static_interface.shadow.adventurirc.gui;

import java.util.HashMap;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.pircbotx.Channel;
import org.pircbotx.User;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.io.FileManager;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ChatFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	HashMap<UUID, PrivateChatPanel> private_chats = new HashMap<UUID, PrivateChatPanel>();
	HashMap<Channel, PublicChatPanel> public_chats = new HashMap<Channel, PublicChatPanel>();
	
	JTabbedPane content = new JTabbedPane();
	
	OptionsPane options = new OptionsPane();
	
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
		content.setBounds(10, 5, (int) getSize().getWidth()-5, (int) getSize().getHeight()-10);
		content.addTab("Optionen", options);
	}
	
	@Override
	public void repaint()
	{
		super.repaint();
		for ( ChatPanel p : private_chats.values() ) p.resize();
		for ( ChatPanel p : public_chats.values() ) p.resize();
	}
	
	private void addPrivateChatPanel(User u)
	{
		private_chats.put(u.getUserId(), new PrivateChatPanel(u));
		content.add(u.getNick(), private_chats.get(u.getUserId()));
		repaint();
	}
	
	private void addPublicChatPanel(Channel c)
	{
		public_chats.put(c, new PublicChatPanel(c));
		content.add(c.getName(), public_chats.get(c));
		repaint();
	}
	
	public PublicChatPanel getChannel(Channel c)
	{
		if ( public_chats.get(c) == null ) addPublicChatPanel(c);
		return public_chats.get(c);
	}
	
	public PrivateChatPanel getUserPanel(User u)
	{
		if ( private_chats.get(u.getUserId()) == null ) addPrivateChatPanel(u);
		return private_chats.get(u.getUserId());
	}
}
class OptionsPane extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	JTextField userName = new JTextField(AdventurIRC.nickname);
	JLabel userNameLabel = new JLabel("Nickname ändern");
	JCheckBox doBeepCheckBox = new JCheckBox("<html>Einen Ton ausgeben, wenn der Nickname erwähnt wird.");
	
	boolean doBeep = Boolean.parseBoolean(FileManager.getString(FileManager.CFG_DOBEEP));
	
	public OptionsPane()
	{
		setLayout(null);
		add(userName);
		userName.setBounds(150, 5, 135, 25);
		userNameLabel.setBounds(5, 5, 145, 25);
		add(userNameLabel);
		JButton userNameButton = new JButton();
		add(doBeepCheckBox);
		doBeepCheckBox.setBounds(5, 35, 180, 40);
		userNameButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if ( userName.getText().trim().equals("") ) return;
				
				FileManager.setString(FileManager.CFG_NICKNAME, userName.getText());
			}
		});
		doBeepCheckBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				doBeep = !doBeep;
			}
		});
	}
}