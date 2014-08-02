package de.static_interface.shadow.adventurirc;

import de.static_interface.shadow.adventurirc.gui.IRCFrame;
import de.static_interface.shadow.adventurirc.io.FileManager;
import de.static_interface.shadow.adventurirc.io.NetworkManager;

public class AdventurIRC implements Runnable
{
	public static String nickname = FileManager.getString(FileManager.CFG_NICKNAME);

	public static final IRCFrame frame = new IRCFrame();

	public static final String VERSION = "AdventurIRC V1.0.3.1";

	public void run()
	{
		NetworkManager.connectToServer("irc.adventuria.eu", 6667, FileManager.getString(FileManager.CFG_NICKNAME));
		NetworkManager.joinChannel("irc.adventuria.eu", "#adventuriabot");
	}
}