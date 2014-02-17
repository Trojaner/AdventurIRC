package de.static_interface.shadow.adventurirc.gui;

import java.util.Date;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.pircbotx.Channel;
import org.pircbotx.User;

public class PublicChatPanel extends BasicChatPanel
{
	private static final long serialVersionUID = 1L;

	Channel receipt;
	
	JTextPane userList = new JTextPane();
	
	JScrollPane userListScrollPane = new JScrollPane();
	
	public PublicChatPanel(Channel receipt)
	{
		try
		{
			UIManager.setLookAndFeel(new NimbusLookAndFeel());
		}
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		this.receipt = receipt;
		userListScrollPane.setViewportView(userList);
		add(userListScrollPane);
		userList.setEditable(false);
		textOutput.setEditable(false);
	}
	
	@Override
	public void matchSize(int sizeX, int sizeY)
	{
		textOutputScrollPane.setBounds(5, 5, sizeX-155, sizeY-95);
		textInput.setBounds(5, sizeY-90, sizeX-20, 25);
		userListScrollPane.setBounds(sizeX-150, 5, 135, sizeY-95);
	}
	
	public void rewriteUserList()
	{
		userList.setText("");
		for ( User u : receipt.getUsers() )
		{
			userList.setText(userList.getText()+u.getNick()+"\n");
		}
	}
	
	@Override
	public void sendMessageToReceipt(String toWrite)
	{
		receipt.send().message(toWrite);
		insertString(String.format("%s <%s>: %s", timeFormat.format(new Date()), nickname, toWrite));
	}
}
