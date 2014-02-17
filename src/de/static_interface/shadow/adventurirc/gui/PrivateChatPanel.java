package de.static_interface.shadow.adventurirc.gui;

import java.util.Date;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.pircbotx.User;

public class PrivateChatPanel extends BasicChatPanel
{
	private static final long serialVersionUID = 1L;
	
	User receipt;
	
	public PrivateChatPanel(User receipt)
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
		add(textInput);
		textOutput.setEditable(false);
	}
	
	@Override
	public void matchSize(int sizeX, int sizeY)
	{
		textOutputScrollPane.setBounds(5, 5, sizeX-20, sizeY-95);
		textInput.setBounds(5, sizeY-90, sizeX-20, 25);
	}

	@Override
	public void sendMessageToReceipt(String toWrite)
	{
		receipt.send().message(toWrite);
		insertString(String.format("%s <%s>: %s", timeFormat.format(new Date()), nickname, toWrite));
	}
}