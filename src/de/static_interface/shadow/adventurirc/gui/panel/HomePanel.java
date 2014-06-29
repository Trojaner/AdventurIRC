package de.static_interface.shadow.adventurirc.gui.panel;

import de.static_interface.shadow.adventurirc.io.FileManager;

public class HomePanel extends ChatPanel
{
	private static final long serialVersionUID = 1L;

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		resizeComponents();
	}

	public void log(String toWrite)
	{
		FileManager.logWriter.printFileOnly(toWrite);
	}

	@Override
	public void resizeComponents()
	{
		int y_max = (int) (getSize().getHeight());
		int x_max = (int) (getSize().getWidth());

		textOutput.setBounds(5, 5, x_max-10, (int) (y_max*0.8));
		textInput.setBounds(5, textOutput.getHeight()+5, textOutput.getWidth(), (y_max - (textOutput.getHeight()+10)));
		
		while ( textInput.getHeight() < 25 )
		{
			textOutput.setBounds(5, 5, x_max-10, textOutput.getHeight()-5);
			textInput.setBounds(5, textOutput.getHeight()+5, textOutput.getWidth(), (y_max - (textOutput.getHeight()+10)));
		}
		while ( textInput.getHeight() > 35 )
		{
			textOutput.setBounds(5, 5, x_max-10, textOutput.getHeight()+5);
			textInput.setBounds(5, textOutput.getHeight()+5, textOutput.getWidth(), (y_max - (textOutput.getHeight()+10)));
		}
	}
}