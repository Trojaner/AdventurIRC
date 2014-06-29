package de.static_interface.shadow.adventurirc.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.io.AdventurIRCLog;
import de.static_interface.shadow.adventurirc.io.NetworkManager;

public class ServerPanel extends HomePanel
{
	private static final long serialVersionUID = 1L;

	private String servername;

	private AdventurIRCLog log;

	public ServerPanel(String servername)
	{
		this.servername = servername;
		log = ( logChat ? new AdventurIRCLog(servername, "main") : null );
		textInput.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if ( !checkCommand(textInput.getText()) ) { write(PREFIX, "Hier sind nur Commands möglich !"); return; }
				textInput.setText("");
			}
		});
		resizeComponents();
	}

	protected boolean checkCommand(String text)
	{
		if ( !text.startsWith("/") ) return false;
		if ( text.startsWith("//") ) return (checkCommand(text.substring(1)));

		String cmd = text.substring(1).split(" ")[0];
		String[] args = Arrays.copyOfRange(text.split(" "), 1, text.split(" ").length);

		System.out.println(cmd);

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
		else
		{
			write(PREFIX, "Diesen Befehl gibt es nicht !");
			return false;
		}
	}

	@Override
	public void log(String toLog)
	{
		if ( logChat ) log.write(toLog);
	}
}
