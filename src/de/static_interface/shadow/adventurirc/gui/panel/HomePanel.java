package de.static_interface.shadow.adventurirc.gui.panel;

public class HomePanel extends ChatPanel
{
	private static final long serialVersionUID = 1L;

	@Override
	public void write(String text)
	{
		textOutput.write(text);
	}

	@Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		resizeComponents();
	}

	@Override
	public void resizeComponents()
	{
		int y_max = (int) (getSize().getHeight());
		int x_max = (int) (getSize().getWidth());

		textOutput.setBounds(5, 5, x_max-10, (int) (y_max*0.8));
		textInput.setBounds(5, (int) (textOutput.getBounds().getHeight()+5), x_max-10, (int) (y_max*0.14));
		if ( textInput.getHeight() < 26 )
		{
			textOutput.setBounds(5, 5, x_max-10, (int) (y_max*0.6));
			textInput.setBounds(5, (int) (textOutput.getBounds().getHeight()+5), x_max-10, 27);
		}

		if ( textInput.getHeight() > 39 )
		{
			textOutput.setBounds(5, 5, x_max-10, (int) (y_max*0.923));
			textInput.setBounds(5, (int) (textOutput.getBounds().getHeight()+5), x_max-10, (int) (y_max*0.07));
			if ( textInput.getHeight() > 39 )
			{
				textOutput.setBounds(5, 5, x_max-10, (int) (y_max*0.943));
				textInput.setBounds(5, (int) (textOutput.getBounds().getHeight()+5), x_max-10, (int) (y_max*0.035));
			}
		}
	}
}
