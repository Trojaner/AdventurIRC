package de.static_interface.shadow.adventurirc.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.io.AdventurIRCLog;
import de.static_interface.shadow.adventurirc.io.NetworkManager;

public abstract class NetworkedChatPanel extends ChatPanel
{
	private static final long serialVersionUID = 1L;

	private String servername;

	protected AdventurIRCLog log;

	public NetworkedChatPanel(String servername)
	{
		this.servername = servername;
		for ( ActionListener l : textInput.getActionListeners() ) textInput.removeActionListener(l);
		textInput.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if ( textInput.getText().trim().equals("") ) return; 
				if ( checkCommand(textInput.getText()) ) { textInput.setText(""); return; }

				send(textInput.getText());
				textInput.setText("");
			}
		});
	}

	public abstract void log(String toLog);

	public String getServername()
	{
		return servername;
	}

	public abstract void send(String text);

	@Override
	public void write(String prefix, String text)
	{
		if ( checkCommand(text) ) return;
		else super.write(prefix, text);
	}

	@Override
	protected boolean checkCommand(String text)
	{
		if ( !text.startsWith("/") ) return false;
		if ( text.startsWith("//") ) { send(text); return true; }

		String cmd = text.substring(1).split(" ")[0];
		String[] args = Arrays.copyOfRange(text.split(" "), 1, text.split(" ").length);

		if ( cmd.equalsIgnoreCase("join") )
		{
			if ( args.length < 1 )
			{
				write(PREFIX, "Du musst einen Channelnamen angeben !");
				return true;
			}

			NetworkManager.joinChannel(servername, args[0]);

			return true;
		}
		if ( cmd.equalsIgnoreCase("part") )
		{
			if ( args.length < 1 )
			{
				write(PREFIX, "Das hier ist kein IRC-Channel");
				return true;
			}

			NetworkManager.partChannel(servername, AdventurIRC.frame.getPublicChatPanel(servername, args[0]).getChannel());
			return true;
		}
		if ( cmd.equalsIgnoreCase("close") )
		{
			this.getParent().remove(this);
			return true;
		}
		if ( cmd.equalsIgnoreCase("nick") )
		{
			if ( args.length < 1 )
			{
				write(PREFIX, "Du musst einen neuen Nicknamen angeben.");
				return true;
			}
			
			AdventurIRC.nickname = args[0];
			NetworkManager.rename(servername, AdventurIRC.nickname);
			return true;
		}
		if ( cmd.equalsIgnoreCase("msg") )
		{
			if ( args.length < 2 )
			{
				write(PREFIX, "Du musst einen Empfänger und eine Nachricht angeben.");
				return true;
			}

			String msg = "";
			for ( String s : Arrays.copyOfRange(args, 1, args.length) ) msg = msg + s + " ";
			NetworkManager.sendPrivateMessage(this, servername, args[0], msg);
			return true;
		}
		else
		{
			write(PREFIX, "Diesen Befehl gibt es nicht !");
		}
		return true;
	}
}
