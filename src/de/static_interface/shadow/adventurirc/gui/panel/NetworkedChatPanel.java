package de.static_interface.shadow.adventurirc.gui.panel;

import java.util.Arrays;

import de.static_interface.shadow.adventurirc.AdventurIRC;

public abstract class NetworkedChatPanel extends ChatPanel
{
	private static final long serialVersionUID = 1L;

	private String servername;

	public NetworkedChatPanel(String servername)
	{
		this.servername = servername;
	}

	public String getServername()
	{
		return servername;
	}

	public abstract void send(String text);

	@Override
	public void write(String text)
	{
		if ( checkCommand(text) ) return;
		else super.write(text);
	}

	private boolean checkCommand(String text)
	{
		if ( !text.startsWith("/") ) return false;
		if ( text.startsWith("//") ) { send(text); return true; }

		String cmd = text.substring(1).split(" ")[0];
		String[] args = Arrays.copyOfRange(text.split(" "), 1, text.split(" ").length);

		if ( cmd.equalsIgnoreCase("join") )
		{
			if ( args.length < 1 )
			{
				write(PREFIX+"Du musst einen Channelnamen angeben !");
				return true;
			}

			//Join channel
			return true;
		}
		if ( cmd.equalsIgnoreCase("part") )
		{
			if ( args.length < 1 )
			{
				if ( !(this instanceof PublicChatPanel) ) { write(PREFIX+"Das hier ist kein IRC-Channel"); return true; }
				else { /* part channel */ return true; }
			}

			//Part another channel
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
				write(PREFIX+"Du musst einen neuen Nicknamen angeben.");
				return true;
			}
			
			AdventurIRC.nickname = args[0];
			//Rename in NetworkManager
			return true;
		}
		else
		{
			write("Diesen Befehl gibt es nicht !");
			return false;
		}
	}
}
