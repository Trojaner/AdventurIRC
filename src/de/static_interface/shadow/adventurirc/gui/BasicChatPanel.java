package de.static_interface.shadow.adventurirc.gui;

import java.text.SimpleDateFormat;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

import org.pircbotx.User;

import de.static_interface.shadow.adventurirc.AdventurIRC;
import de.static_interface.shadow.adventurirc.io.NetworkManager;


import static org.pircbotx.Colors.*;

public abstract class BasicChatPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	JScrollPane textOutputScrollPane = new JScrollPane();
	JTextPane textOutput = new JTextPane();
	
	JTextField textInput = new JTextField();
	
	static final String nickname = AdventurIRC.nickname;
	static final SimpleDateFormat timeFormat = AdventurIRC.timeFormat;
	
	public BasicChatPanel()
	{
		setLayout(null);
		add(textOutputScrollPane);
		textOutputScrollPane.setViewportView(textOutput);
		textInput.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if ( textInput.getText().trim().equals("") ) return;
				if ( textInput.getText().startsWith("/") ) 
				{
					checkCommand(textInput.getText());
					textInput.setText("");
					return;
				}
				sendMessageToReceipt(textInput.getText());
				textInput.setText("");
			}
		});
		add(textInput);
	}
	
	public void insertString(String toInsert)
	{
		try
		{
			textOutput.getStyledDocument().insertString(textOutput.getStyledDocument().getLength(), removeColors(toInsert+"\n"), getStyle(BLACK));
		}
		catch ( BadLocationException e )
		{
			e.printStackTrace();
		}
	}

	Color parseToColor(String toParse)
	{
		if (toParse.equals( BLUE )) return Color.BLUE;
		if (toParse.equals( BROWN )) return Color.GREEN.darker().darker();
		if (toParse.equals( CYAN )) return Color.CYAN;
		if (toParse.equals( DARK_BLUE )) return Color.BLUE.darker().darker();
		if (toParse.equals( DARK_GRAY )) return Color.DARK_GRAY;
		if (toParse.equals( DARK_GREEN )) return Color.GREEN.darker();
		if (toParse.equals( GREEN )) return Color.GREEN;
		if (toParse.equals( LIGHT_GRAY )) return Color.LIGHT_GRAY;
		if (toParse.equals( MAGENTA )) return Color.MAGENTA;
		if (toParse.equals( OLIVE )) return Color.GREEN.darker().darker().darker();
		if (toParse.equals( PURPLE )) return Color.MAGENTA.darker();
		if (toParse.equals( RED )) return Color.RED;
		if (toParse.equals( TEAL )) return Color.CYAN.darker();
		if (toParse.equals( WHITE )) return Color.WHITE;
		if (toParse.equals( YELLOW )) return Color.YELLOW;
		else return Color.BLACK;
	}
	
	/**
	 * getStyle method returns Style used to draw colored text.
	 * @param name Name of the color. Only {@link See org.pircbotx.Colors} are allowed.
	 * @return Returns a colored style which can be used to draw colored text on that chat panel.
	 */
	public Style getStyle(String name)
	{
		System.out.println(name+" == "+parseToColor(name));
		Style s = textOutput.getStyle(name);
		if ( s == null ) s = textOutput.addStyle(name, null);
		StyleConstants.setForeground(s, parseToColor(name));
		return s;
	}
	
	public void checkCommand(String toCheck)
	{
		if ( 
				toCheck.startsWith("/msg") ||
				toCheck.startsWith("/pn") || 
				toCheck.startsWith("/query")
			)
		{
			if ( toCheck.split(" ").length < 3 ) return;

			System.out.println(toCheck.split(" ")[1]);
			User receipt = NetworkManager.getUser(toCheck.split(" ")[1]);
			String toSend = toCheck.substring(toCheck.indexOf(' ', toCheck.indexOf(' ')+1)+1);
			ChatFrame.mainChatFrame.addChat(receipt).sendMessageToReceipt(toSend);
			return;
		}
	}
	
	public abstract void matchSize(int sizeX, int sizeY);
	
	public abstract void sendMessageToReceipt(String toWrite);
}