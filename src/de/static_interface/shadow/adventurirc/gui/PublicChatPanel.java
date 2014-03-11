package de.static_interface.shadow.adventurirc.gui;

import javax.swing.JScrollPane;

import org.pircbotx.Channel;

import de.static_interface.shadow.adventurirc.AdventurIRC;

public class PublicChatPanel extends ChatPanel
{
	private static final long serialVersionUID = 1L;
	
	private Channel channel;
	
	private JScrollPane userListScrollPane = new JScrollPane();
	private TextOutput userList = new TextOutput();
	
	public PublicChatPanel(Channel channel)
	{
		super();
		userListScrollPane.setViewportView(userList);
		add(userListScrollPane);
		this.channel = channel;
	}
	
	@Override
	public void resize()
	{
		textOutputScrollPane.setBounds(5, 5, (int) this.getSize().getWidth()-135, (int) this.getSize().getHeight()-40);
		textInput.setBounds(5, (int) this.getSize().getHeight()-30, (int) this.getSize().getWidth()-10, 25);
		userListScrollPane.setBounds((int) this.getSize().getWidth()-125, 5, 120, (int) this.getSize().getHeight()-40);
	}
	
	@Override
	public void send(String toSend)
	{
		channel.send().message(toSend);
		write(AdventurIRC.nickname, toSend);
	}
}
