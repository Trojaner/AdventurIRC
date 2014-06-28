package de.static_interface.shadow.adventurirc.gui.panel;

import org.pircbotx.Channel;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.gui.TextOutputPane;
import de.static_interface.shadow.adventurirc.io.AdventurIRCLog;
import de.static_interface.shadow.adventurirc.io.NetworkManager;

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
		log = ( logChat ? new AdventurIRCLog(servername, channel.getName()) : null );
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

		textOutput.setBounds(5, 5, (int) (x_max*0.7), (int) (y_max*0.8));
		textInput.setBounds(5, (int) (textOutput.getBounds().getHeight()+5), (int) (x_max*0.7), (int) (y_max*0.14));
		userList.setBounds(textOutput.getWidth()+5, 5, (int) (x_max*0.29), ((textOutput.getHeight()+textInput.getHeight())));
		if ( textInput.getHeight() < 26 )
		{
			textOutput.setBounds(5, 5, (int) (x_max*0.7), (int) (y_max*0.6));
			textInput.setBounds(5, (int) (textOutput.getBounds().getHeight()+5), (int) (x_max*0.7), 27);
			userList.setBounds(textOutput.getWidth()+5, 5, (int) (x_max*0.29), ((textOutput.getHeight()+textInput.getHeight())));
		}

		if ( textInput.getHeight() > 39 )
		{
			textOutput.setBounds(5, 5, (int) (x_max*0.7), (int) (y_max*0.923));
			textInput.setBounds(5, (int) (textOutput.getBounds().getHeight()+5), (int) (x_max*0.7), (int) (y_max*0.07));
			userList.setBounds(textOutput.getWidth()+5, 5, (int) (x_max*0.29), ((textOutput.getHeight()+textInput.getHeight())));
			if ( textInput.getHeight() > 39 )
			{
				textOutput.setBounds(5, 5, (int) (x_max*0.7), (int) (y_max*0.943));
				textInput.setBounds(5, (int) (textOutput.getBounds().getHeight()+5), (int) (x_max*0.7), (int) (y_max*0.035));
				userList.setBounds(textOutput.getWidth()+5, 5, (int) (x_max*0.29), ((textOutput.getHeight()+textInput.getHeight())));
			}
		}
	}

	@Override
	public void send(String text)
	{
		write(AdventurIRC.nickname, text);
		channel.send().message(text);
	}

	@Override
	protected boolean checkCommand(String text)
	{
		if ( text.equalsIgnoreCase("/part") )
		{
			NetworkManager.partChannel(channel.getBot().getConfiguration().getServerHostname(), channel);
			getParent().remove(this);
			return true;
		}
		else return super.checkCommand(text);
	}

	@Override
	public void log(String toLog)
	{
		if ( logChat ) log.write(toLog);
	}
}
