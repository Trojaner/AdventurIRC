package de.static_interface.shadow.adventurirc.gui;

import org.pircbotx.Channel;

public class PublicChatPanel extends ChatPanel
{
	private static final long serialVersionUID = 1L;
	
	private Channel channel;
	
	private TextOutput userList = new TextOutput();
	
	public PublicChatPanel(Channel channel)
	{
		super();
		this.channel = channel;
		add(userList);
	}
	
	@Override
	public void resize()
	{
		textOutput.setBounds(0, 0, (int) this.getSize().getWidth()-135, (int) this.getSize().getHeight()-35);
		textInput.setBounds(0, (int) this.getSize().getHeight()-30, (int) this.getSize().getWidth()-135, 25);
		userList.setBounds((int) this.getSize().getWidth()-130, 0, 125, (int) this.getSize().getHeight()-35);
	}
	
	@Override
	public void send(String toSend)
	{
		channel.send().message(toSend);
	}
}
