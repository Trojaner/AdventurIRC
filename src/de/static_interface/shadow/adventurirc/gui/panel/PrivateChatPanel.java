package de.static_interface.shadow.adventurirc.gui.panel;

import org.pircbotx.User;

import de.static_interface.shadow.adventurirc.AdventurIRC;

public class PrivateChatPanel extends NetworkedChatPanel
{
	private static final long serialVersionUID = 1L;

	private User user;

	public PrivateChatPanel(String servername, User user)
	{
		super(servername);
		this.user = user;
	}

	@Override
	public void resizeComponents()
	{
		int y_max = (int) (getSize().getHeight());
		int x_max = (int) (getSize().getWidth());

		textOutput.setBounds(5, 5, x_max - 10 ,(int) (y_max*0.7));
		textInput.setBounds(5, (int) (textOutput.getBounds().getHeight()+10), x_max - 10, ((int) (y_max - textOutput.getBounds().getHeight()) < 27 ? 27 : (int) (y_max - textOutput.getBounds().getHeight())));
	}

	public User getUser()
	{
		return user;
	}

	@Override
	public void send(String text)
	{
		write(AdventurIRC.nickname+" "+text);
		user.send().message(text);
	}
}
