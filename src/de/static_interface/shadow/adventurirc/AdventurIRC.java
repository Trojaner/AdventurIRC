package de.static_interface.shadow.adventurirc;

import java.text.SimpleDateFormat;

import de.static_interface.shadow.adventurirc.gui.SetupFrame;
import de.static_interface.shadow.adventurirc.io.FileManager;
import de.static_interface.shadow.adventurirc.io.NetworkManager;
import static de.static_interface.shadow.adventurirc.gui.ChatFrame.mainChatFrame;

public class AdventurIRC
{
	public static String nickname;
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("DD_MM_YYYY");
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat("[HH:mm:ss] ");
	
	public static final String VERSION = "AdventurIRC_v1.0.1";
	
	public static void main(String[] args)
	{
		SetupFrame setupFrame = new SetupFrame();
		String _nickname = FileManager.getString(FileManager.CFG_NICKNAME);
		while ( _nickname == null )
		{
			setupFrame.setVisible(true);
			_nickname = FileManager.getString(FileManager.CFG_NICKNAME);
		}
		nickname = _nickname;
		NetworkManager.connect(nickname);
		mainChatFrame.setVisible(true);
	}
}
