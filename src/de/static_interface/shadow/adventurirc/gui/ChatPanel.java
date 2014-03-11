package de.static_interface.shadow.adventurirc.gui;

import static org.pircbotx.Colors.BLACK;
import static org.pircbotx.Colors.DARK_GREEN;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public abstract class ChatPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	TextOutput textOutput = new TextOutput();
	JScrollPane textOutputScrollPane = new JScrollPane();
	JTextField textInput = new JTextField();
	
	public ChatPanel()
	{
		setLayout(null);
		textOutputScrollPane.setViewportView(textOutput);
		add(textOutputScrollPane);
		textInput.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if ( textInput.getText().trim().equals("") ) return;
				send(textInput.getText());
				textInput.setText("");
			}
		});
		add(textInput);
	}
	
	public abstract void resize();
	
	public abstract void send(String toSend);
	
	public void write(String sender, String toWrite)
	{
		textOutput.write(String.format("%s%s%s: %s\n", new SimpleDateFormat("[HH:MM:ss] ").format(new Date()), sender.equals(AdventurIRC.nickname) ? DARK_GREEN : BLACK, sender, toWrite));
	}
}