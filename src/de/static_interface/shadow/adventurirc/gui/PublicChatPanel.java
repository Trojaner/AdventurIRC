package de.static_interface.shadow.adventurirc.gui;

import javax.swing.JScrollPane;

import org.pircbotx.Channel;
import org.pircbotx.User;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import static org.pircbotx.Colors.RED;
import static org.pircbotx.Colors.BLACK;


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
	public void write(String sender, String toWrite)
	{
		super.write(sender, toWrite);
		updateUserList();
	}
	
	private boolean isPrivilegued(User u)
	{
		
		//Has to be hard coded for a while
		return ( 
				u.getNick().toLowerCase().contains("trojaner") ||
				u.getNick().toLowerCase().contains("rinu") ||
				u.getNick().toLowerCase().contains("shadow") ||
				u.getNick().toLowerCase().contains("adventuriabot") ||
				u.getNick().toLowerCase().contains("ircguardian")
				);
	}
	
	private void updateUserList()
	{
		userList.setText("");
		for ( User u : channel.getUsers() )
		{
			userList.write(String.format("%s%s%s", isPrivilegued(u) ? "@" : "", isPrivilegued(u) ? RED : BLACK, u.getNick()));
		}
	}
	
	@Override
	public void send(String toSend)
	{
		channel.send().message(toSend);
		write(AdventurIRC.nickname, toSend);
	}
}
