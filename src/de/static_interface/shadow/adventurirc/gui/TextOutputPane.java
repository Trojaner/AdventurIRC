package de.static_interface.shadow.adventurirc.gui;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import static org.pircbotx.Colors.BLUE;
import static org.pircbotx.Colors.BROWN;
import static org.pircbotx.Colors.CYAN;
import static org.pircbotx.Colors.DARK_BLUE;
import static org.pircbotx.Colors.DARK_GRAY;
import static org.pircbotx.Colors.DARK_GREEN;
import static org.pircbotx.Colors.GREEN;
import static org.pircbotx.Colors.LIGHT_GRAY;
import static org.pircbotx.Colors.MAGENTA;
import static org.pircbotx.Colors.OLIVE;
import static org.pircbotx.Colors.PURPLE;
import static org.pircbotx.Colors.RED;
import static org.pircbotx.Colors.TEAL;
import static org.pircbotx.Colors.WHITE;
import static org.pircbotx.Colors.YELLOW;
import static org.pircbotx.Colors.removeFormatting;
import static org.pircbotx.Colors.BLACK;

public class TextOutputPane extends JScrollPane
{
	private static final long serialVersionUID = 1L;

	private TextOutput textOutput = new TextOutput();

	public TextOutputPane()
	{
		super();
		setViewportView(textOutput);
	}

	public void write(String text)
	{
		textOutput.write(text);
	}
	
}
class TextOutput extends JTextPane
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
		}
	}
}
class ColorUtils
{
	public static StyledDocument registerStyles(StyledDocument document)
	{
		registerStyle(document, BLUE);
		registerStyle(document, TEAL);
		registerStyle(document, BROWN);
		registerStyle(document, CYAN);
		registerStyle(document, DARK_BLUE);
		registerStyle(document, DARK_GRAY);
		registerStyle(document, DARK_GREEN);
		registerStyle(document, GREEN);
		registerStyle(document, LIGHT_GRAY);
		registerStyle(document, MAGENTA);
		registerStyle(document, OLIVE);
		registerStyle(document, PURPLE);
		registerStyle(document, RED);
		registerStyle(document, WHITE);
		registerStyle(document, YELLOW);

		return document;
	}

	public static Style getStyle(String color, StyledDocument document)
	{
		return document.getStyle(color);
	}

	private static void registerStyle(StyledDocument document, String color)
	{
		StyleConstants.setForeground(document.addStyle(color, null), getColor(color));
	}

	public static Color getColor(String color)
	{
		if ( color.equals(BLUE) ) return Color.BLUE;
		if ( color.equals(BROWN) ) return Color.ORANGE.darker();
		if ( color.equals(CYAN) ) return Color.CYAN;
		if ( color.equals(DARK_BLUE) ) return Color.BLUE.darker();
		if ( color.equals(DARK_GRAY) ) return Color.DARK_GRAY;
		if ( color.equals(DARK_GREEN) ) return Color.GREEN.darker();
		if ( color.equals(GREEN) ) return Color.GREEN;
		if ( color.equals(LIGHT_GRAY) ) return Color.LIGHT_GRAY;
		if ( color.equals(MAGENTA) ) return Color.MAGENTA;
		if ( color.equals(OLIVE) ) return Color.GREEN.darker().darker();
		if ( color.equals(PURPLE) ) return Color.MAGENTA.darker();
		if ( color.equals(RED) ) return Color.RED;
		if ( color.equals(TEAL) ) return Color.CYAN.darker();
		if ( color.equals(WHITE) ) return Color.WHITE;
		if ( color.equals(YELLOW) ) return Color.YELLOW;
		else return Color.BLACK;
	}
}