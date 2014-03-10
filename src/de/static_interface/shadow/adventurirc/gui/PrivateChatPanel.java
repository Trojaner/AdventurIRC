package de.static_interface.shadow.adventurirc.gui;

import org.pircbotx.User;

import de.static_interface.shadow.adventurirc.AdventurIRC;

public class PrivateChatPanel extends ChatPanel
{
	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public PrivateChatPanel(User user)
	{
		super();
		this.user = user;
	}
	
	@Override
	public void resize()
	{
		textOutput.setBounds(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight()-35);
		textInput.setBounds(0, (int) this.getSize().getHeight()-30, (int) this.getSize().getWidth(), 25);
	}
	
	@Override
	public void send(String toSend)
	{
		write(AdventurIRC.nickname, toSend);
		user.send().message(toSend);
	}
}
