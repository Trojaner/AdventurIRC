package de.static_interface.shadow.adventurirc.gui.panel;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextField;

import de.static_interface.shadow.adventurirc.gui.TextOutputPane;
import de.static_interface.shadow.adventurirc.io.FileManager;

public abstract class ChatPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public static final String PREFIX = "[AdventurIRC]";

	public static final SimpleDateFormat defaultTimeFormat_Chat = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat defaultTimeFormat_Log = new SimpleDateFormat("dd.MM.YYYY");

	public static SimpleDateFormat timeFormat_Chat = new SimpleDateFormat(FileManager.getString(FileManager.CFG_TIME_FORMAT_CHAT));
	public static SimpleDateFormat timeFormat_Log = new SimpleDateFormat(FileManager.getString(FileManager.CFG_TIME_FORMAT_LOG));

	protected TextOutputPane textOutput = new TextOutputPane();
	
	protected JTextField textInput = new JTextField();

	public ChatPanel()
	{
		setLayout(null);
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
}
