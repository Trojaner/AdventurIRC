package de.static_interface.shadow.adventurirc.gui.panel;

import org.pircbotx.Channel;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.gui.TextOutputPane;

public class PublicChatPanel extends NetworkedChatPanel
{
	private static final long serialVersionUID = 1L;

	private Channel channel;

	private TextOutputPane userList = new TextOutputPane();

	public PublicChatPanel(String servername, Channel channel)
	{
		super(servername);
		this.channel = channel;
		add(userList);
		resizeComponents();
	}

	public Channel getChannel()
	{
		return channel;
	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		resizeComponents();
	}

	@Override
	public void resizeComponents()
	{
		if ( userList == null) return;

		int y_max = (int) (getSize().getHeight());
		int x_max = (int) (getSize().getWidth());

		textOutput.setBounds(5, 5, x_max < 150 ? x_max-10 : (int) (x_max * 0.7), y_max );
	}

	@Override
	public void send(String text)
	{
		write(AdventurIRC.nickname+" "+text);
		channel.send().message(text);
	}
}
