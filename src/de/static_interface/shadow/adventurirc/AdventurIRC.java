package de.static_interface.shadow.adventurirc;

import de.static_interface.shadow.adventurirc.gui.ChatFrame;
import de.static_interface.shadow.adventurirc.io.FileManager;
import de.static_interface.shadow.adventurirc.io.NetworkManager;

public class AdventurIRC implements Runnable
{
	public static String nickname = FileManager.getString(FileManager.CFG_NICKNAME);
	
	public static final String VERSION = "AdventurIRC V1.0.2.3";
	
	public static final ChatFrame mainFrame = new ChatFrame();
	
	public void run()
	{
		NetworkManager.connect(nickname);
		mainFrame.setVisible(true);
	}
}
