package de.static_interface.shadow.adventurirc.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextField;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.gui.TextOutputPane;
import de.static_interface.shadow.adventurirc.io.FileManager;
import de.static_interface.shadow.adventurirc.io.NetworkManager;

public abstract class ChatPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public static final String PREFIX = "[AdventurIRC]";

	public static final SimpleDateFormat defaultTimeFormat_Chat = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat defaultTimeFormat_Log = new SimpleDateFormat("dd.MM.YYYY");

	public static SimpleDateFormat timeFormat_Chat = new SimpleDateFormat(FileManager.getString(FileManager.CFG_TIME_FORMAT_CHAT));
	public static SimpleDateFormat timeFormat_Log = new SimpleDateFormat(FileManager.getString(FileManager.CFG_TIME_FORMAT_LOG));

	protected static final boolean logChat = FileManager.getString(FileManager.CFG_LOG_CHAT_OUTPUT).equalsIgnoreCase("true");

	protected TextOutputPane textOutput = new TextOutputPane();
	
	protected JTextField textInput = new JTextField();

	public ChatPanel()
	{
		setLayout(null);
		textInput.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if ( textInput.getText().trim().equals("") ) { textInput.setText(""); return; }
				if ( !checkCommand(textInput.getText()) )
				{
					write(PREFIX, "Hier sind nur Commands möglich !");
				};
				textInput.setText("");
			}
		});
		add(textOutput);
		add(textInput);
		resizeComponents();
	}

	/**
	 * Writes text to the output, format: prefix+": "+text
	 * @param prefix String before ": "
	 * @param text String after ": "
	 */
	public void  write(String prefix, String text)
	{
		log(String.format(FileManager.getString(FileManager.CFG_CHAT_OUTPUT_FORMAT), timeFormat_Chat.format(new Date()), prefix, text));
		textOutput.write(String.format(FileManager.getString(FileManager.CFG_CHAT_OUTPUT_FORMAT), timeFormat_Chat.format(new Date()), prefix, text));
	}

	public abstract void log(String toWrite);

	/**
	 * Resizes the contained components to match the new frame size
	 */
	public abstract void resizeComponents();

	protected boolean checkCommand(String text)
	{
		if ( !text.startsWith("/") ) return false;
		if ( text.startsWith("//") ) return checkCommand(text.substring(1));

		String cmd = text.substring(1).split(" ")[0];
		String[] args = Arrays.copyOfRange(text.split(" "), 1, text.split(" ").length);

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
			NetworkManager.rename("*", AdventurIRC.nickname);
			return true;
		}
		else
		{
			write(PREFIX, "Dieser Befehl ist hier nicht verfügbar !");
		}
		return true;
	}
}
