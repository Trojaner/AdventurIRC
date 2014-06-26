package de.static_interface.shadow.adventurirc;

import de.static_interface.shadow.adventurirc.gui.IRCFrame;
import de.static_interface.shadow.adventurirc.io.FileManager;

public class AdventurIRC implements Runnable
{
	public static String nickname = FileManager.getString(FileManager.CFG_NICKNAME);

	public static final IRCFrame frame = new IRCFrame();

	public static final String VERSION = "AdventurIRC V1.0.2.3";

	public void run()
	{
		new IRCFrame();
	}
}
