package de.static_interface.shadow.adventurirc.gui;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;

import static org.pircbotx.Colors.removeFormatting;
import static org.pircbotx.Colors.BLACK;

public class TextOutput extends JTextPane
{
	private static final long serialVersionUID = 1L;
	
	private static final String colorCodeStart = String.valueOf((char) 3);
	
	public TextOutput()
	{
		super();
		setEditable(false);
		ColorUtils.registerStyles(getStyledDocument());
	}
	
	public void write(String text)
	{
		String[] splitByColorCode = removeFormatting(text).trim().split(String.valueOf((char) 3));
		Style style;
		for ( String s : splitByColorCode )
		{
			if ( s.startsWith("[") )
			{
				try
				{
					getStyledDocument().insertString(getStyledDocument().getLength(), s, ColorUtils.getStyle(BLACK, getStyledDocument()));
				}
				catch (BadLocationException e)
				{
					e.printStackTrace();
				}
				continue;
			}
			
			if ( Character.isDigit(s.charAt(0)) && Character.isDigit(s.charAt(1)) )
			{
				style = ColorUtils.getStyle(colorCodeStart+s.substring(0, 2), getStyledDocument());
				s = s.substring(2);
			}
			else
			{
				style = ColorUtils.getStyle(BLACK, getStyledDocument());
			}
			
			try
			{
				getStyledDocument().insertString(getStyledDocument().getLength(), s, style);
			}
			catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			getStyledDocument().insertString(getStyledDocument().getLength(), "\n", ColorUtils.getStyle(BLACK, getStyledDocument()));
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
	}
}
