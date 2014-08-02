package de.static_interface.shadow.adventurirc.gui.panel;

import java.util.HashMap;
import java.util.Map.Entry;

import org.pircbotx.Channel;
import org.pircbotx.User;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.gui.TextOutputPane;
import de.static_interface.shadow.adventurirc.io.AdventurIRCLog;
import de.static_interface.shadow.adventurirc.io.NetworkManager;
import static org.pircbotx.Colors.BLACK;
import static org.pircbotx.Colors.BROWN;
import static org.pircbotx.Colors.DARK_GRAY;
import static org.pircbotx.Colors.RED;
import static org.pircbotx.Colors.DARK_GREEN;

public class PublicChatPanel extends NetworkedChatPanel
{
	private static final long serialVersionUID = 1L;

	private Channel channel;

	private TextOutputPane userList = new TextOutputPane();

	private HashMap<String, User> usersWithLevel = new HashMap<String, User>();

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
		textInput.setBounds(5, textOutput.getHeight()+5, textOutput.getWidth(), (y_max - (textOutput.getHeight()+10)));
		userList.setBounds(textOutput.getWidth()+5, 5, (int) (x_max*0.29)-5, ((textOutput.getHeight()+textInput.getHeight())));

		if ( textInput.getHeight() > 35 )
		{
			textInput.setBounds(5, y_max-40, (int) (x_max*0.7), 35);
			textOutput.setBounds(5, 5, (int) (x_max*0.7), textInput.getY()-5);
		}
	}

	@Override
	public void send(String text)
	{
		write(AdventurIRC.nickname, text);
		channel.send().message(text);
	}

	public void refreshUserList(boolean onlyJoin, User... joinedUsers)
	{
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		for ( User u : channel.getOwners() )
		{
			usersWithLevel.put(RED+"@ "+(u.isVerified() ? DARK_GREEN : BLACK), u);
		}

		for ( User u : channel.getOps() )
		{
			usersWithLevel.put(RED+"@ "+(u.isVerified() ? BLACK : DARK_GRAY), u);
		}

		for ( User u : channel.getVoices() )
		{
			usersWithLevel.put(BROWN+"V "+(u.isVerified() ? BLACK : DARK_GRAY), u);
		}

		for ( User u : channel.getHalfOps() )
		{
			usersWithLevel.put(RED+"* "+(u.isVerified() ? DARK_GREEN : BLACK), u);
		}

		for ( User u : channel.getSuperOps() )
		{
			usersWithLevel.put(RED+"% "+(u.isVerified() ? BLACK : DARK_GRAY), u);
		}

		for ( User u : channel.getNormalUsers() )
		{
			usersWithLevel.put(BLACK+" ", u);
		}

		for ( Entry<String, User> e : usersWithLevel.entrySet() )
		{
			userList.write(e.getKey()+e.getValue().getNick());
		}
	}

	@Override
	protected boolean checkCommand(String text)
	{
		if ( text.startsWith("/nick") )
		{
			super.checkCommand(text);
			refreshUserList(false);
		}
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