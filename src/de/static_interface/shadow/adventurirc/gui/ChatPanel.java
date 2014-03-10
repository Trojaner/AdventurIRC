package de.static_interface.shadow.adventurirc.gui;

import static org.pircbotx.Colors.BLACK;
import static org.pircbotx.Colors.DARK_GREEN;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextField;

import de.static_interface.shadow.adventurirc.AdventurIRC;

public abstract class ChatPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	TextOutput textOutput = new TextOutput();
	JTextField textInput = new JTextField();
	
	public ChatPanel()
	{
		setLayout(null);
		add(textOutput);
		add(textInput);
	}
	
	public abstract void resize();
	
	public abstract void send(String toSend);
	
	public void write(String sender, String toWrite)
	{
		textOutput.write(String.format("%s%s%s%s: %s", new SimpleDateFormat("[HH:MM:SS] ").format(new Date()), sender.equals(AdventurIRC.nickname) ? DARK_GREEN : BLACK, sender, BLACK));
	}
}
