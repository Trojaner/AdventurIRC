package de.static_interface.shadow.adventurirc.gui;

import java.text.SimpleDateFormat;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import de.static_interface.shadow.adventurirc.AdventurIRC;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

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
				sendMessageToReceipt(textInput.getText());
				textInput.setText("");
			}
		});
		add(textInput);
	}
	public void insertString(String toInsert)
	{
		insertStringWithColor(splitByColorCode(toInsert));
	}
	
	private void insertStringWithColor(String[] toInsert)
	{
		Style toWrite;
		int offset = 0;
		
		for ( String s : toInsert )
		{
			offset = textOutput.getStyledDocument().getLength();
			toWrite = getStyle(((char) 3)+""+s.charAt(1));
			try
			{
				textOutput.getStyledDocument().insertString(offset, String.format("%s\n", s), toWrite);
			}
			catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}
		textOutput.setCaretPosition(offset);
	}
	
	String[] splitByColorCode(String toSplit)
	{
		return toSplit.split(""+((char) 3));
	}
	
	Color parseToColor(String toParse)
	{
		switch ( toParse )
		{
			case BLUE:
				return Color.BLUE;
			case BROWN:
				return Color.GREEN.darker().darker();
			case CYAN:
				return Color.CYAN;
			case DARK_BLUE:
				return Color.BLUE.darker().darker();
			case DARK_GRAY:
				return Color.DARK_GRAY;
			case DARK_GREEN:
				return Color.GREEN.darker();
			case GREEN:
				return Color.GREEN;
			case LIGHT_GRAY:
				return Color.LIGHT_GRAY;
			case MAGENTA:
				return Color.MAGENTA;
			case OLIVE:
				return Color.GREEN.darker().darker().darker();
			case PURPLE:
				return Color.MAGENTA.darker();
			case RED:
				return Color.RED;
			case TEAL:
				return Color.CYAN.darker();
			case WHITE:
				return Color.WHITE;
			case YELLOW:
				return Color.YELLOW;
			default:
				return Color.BLACK;
		}
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
	
	public abstract void matchSize(int sizeX, int sizeY);
	
	public abstract void sendMessageToReceipt(String toWrite);
}