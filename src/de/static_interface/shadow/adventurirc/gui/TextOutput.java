package de.static_interface.shadow.adventurirc.gui;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import static org.pircbotx.Colors.removeFormattingAndColors;
import static org.pircbotx.Colors.removeFormatting;
import static org.pircbotx.Colors.BLACK;

public class TextOutput extends JTextPane
{
	private static final long serialVersionUID = 1L;
	
	public TextOutput()
	{
		super();
		setEditable(false);
		ColorUtils.registerStyles(getStyledDocument());
	}
	
	public void write(String text)
	{
		String textWithoutFormatting = removeFormatting(text);
		String[] colorSplit = textWithoutFormatting.split(String.valueOf((char) 3));
		for ( int x = 0; x < colorSplit.length; x++ )
		{
			if ( colorSplit[x].trim().replaceAll("\n", "").equals("") )
			{
				colorSplit[x] = BLACK;
				continue;
			}
			colorSplit[x] = ((char) 3)+colorSplit[x];
		}
		
		for ( String string : colorSplit )
		{
			try
			{
				getStyledDocument().insertString(getStyledDocument().getLength(), removeFormattingAndColors(string), ColorUtils.getStyle(string.substring(0, 3), getStyledDocument()));
			}
			catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}
	}
}
