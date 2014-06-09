package de.static_interface.shadow.adventurirc.gui;

import static org.pircbotx.Colors.BLACK;
import static org.pircbotx.Colors.DARK_GREEN;
import static org.pircbotx.Colors.RED;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.pircbotx.User;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.io.NetworkManager;

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
				if ( textInput.getText().startsWith("/") )
				{
					checkForCommands(textInput.getText());
				}
				else
				{
					send(textInput.getText());	
				}
				textInput.setText("");
			}
		});
		add(textInput);
	}
	
	public abstract void resize();
	
	public abstract void send(String toSend);
	
	public void write(String sender, String toWrite)
	{
		if ( toWrite.contains(AdventurIRC.nickname) ) if ( !sender.equals(AdventurIRC.nickname) && AdventurIRC.mainFrame.options.doBeep )
		{
			textOutput.write(String.format("%s%s%s: %s%s", new SimpleDateFormat("[HH:MM:ss] ").format(new Date()), sender.equals(AdventurIRC.nickname) ? DARK_GREEN : RED, sender, RED, toWrite));
			sound();
			return;
		}
		textOutput.write(String.format("%s%s%s: %s%s", new SimpleDateFormat("[HH:MM:ss] ").format(new Date()), sender.equals(AdventurIRC.nickname) ? DARK_GREEN : BLACK, sender, BLACK, toWrite));
	}
	
	public void sound()
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/de/static_interface/shadow/adventurirc/Randomize.wav"));
			System.out.println(getClass().getResourceAsStream("/de/static_interface/shadow/adventurirc/Randomize.wav"));
			AudioFormat format = audioInputStream.getFormat();
			int size = (int) (format.getFrameSize()*audioInputStream.getFrameLength());
			byte[] audio = new byte[size];
			DataLine.Info info = new DataLine.Info(Clip.class, format, size);
			audioInputStream.read(audio, 0, size);
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(format, audio, 0, size);
			clip.start();
		}
		catch (Exception e)
		{
		}
	}
	
	private void checkForCommands(String toCheck)
	{
		if ( toCheck.toLowerCase().startsWith("/msg") )
		{
			if ( toCheck.split(" ").length == 1 )
			{
				write("[AdventurIRC] ", "Du musst einen Benutzernamen angeben !");
				return;
			}
			
			User u = NetworkManager.getUser(toCheck.split(" ")[1]);
			
			if ( u == null )
			{
				write("[AdventurIRC] ", "Dieser User ist nicht online.");
			}
			
			PrivateChatPanel panel = AdventurIRC.mainFrame.getUserPanel(u);
			String toWrite = toCheck.substring(toCheck.indexOf(' '));
			toWrite = toWrite.substring(toWrite.indexOf(' '));
			panel.send(toWrite);
			return;
		}
		if ( toCheck.toLowerCase().startsWith("/join ") )
		{
			if ( toCheck.split(" ").length == 1 )
			{
				write("[AdventurIRC] ", "Du musst einen Channelnamen angeben !");
				return;
			}
			
			AdventurIRC.mainFrame.getChannel(NetworkManager.joinChannel(toCheck.split(" ")[1])).write("[AdventurIRC] ", AdventurIRC.nickname+" ist dem Channel beigetreten !");
			return;
		}
		if ( toCheck.toLowerCase().startsWith("/nick") )
		{
			if ( toCheck.split(" ").length == 1 )
			{
				write("[AdventurIRC] ", "Du musst einen neuen Nickname angeben !");
				return;
			}
			
			NetworkManager.rename(toCheck.split(" ")[1]);
			return;
		}
	}
}