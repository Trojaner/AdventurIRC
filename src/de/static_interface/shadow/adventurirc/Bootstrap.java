package de.static_interface.shadow.adventurirc;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import de.static_interface.shadow.adventurirc.io.FileManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Bootstrap extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	
	public static void main(String[] args)
	{
		String remoteVersion = getRemoteVersion();
		System.out.println(remoteVersion.charAt(5)+" "+remoteVersion.charAt(7));
		if ( !remoteVersion.equalsIgnoreCase(AdventurIRC.VERSION.substring(AdventurIRC.VERSION.indexOf(' ')+1)) )
		{
			if ( downloadUpdate(Integer.parseInt(""+remoteVersion.charAt(5)), Integer.parseInt(""+remoteVersion.charAt(7))) )
			{
				File file = new File(String.format("AdventurIRC_1.0.%d.%d.jar", Integer.parseInt(""+remoteVersion.charAt(5)), Integer.parseInt(""+remoteVersion.charAt(7))));
				new UpdateFinishedNotification(file.getAbsolutePath());
				return;
			}
		}
		if ( FileManager.getString(FileManager.CFG_NICKNAME) != null )
		{
			new Thread(new AdventurIRC()).start();
			return;
		}
		else
		{
			new Bootstrap().setVisible(true);
			return;
		}
	}
	
	private static String getRemoteVersion()
	{
		try
		{
			URL url = new URL("http://shadow.static-interface.de/VERSION");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String read = reader.readLine();
			reader.close();
			return read;
		}
		catch (MalformedURLException e)
		{
		}
		catch (IOException e)
		{
		}
		return AdventurIRC.VERSION;
	}
	
	private static boolean downloadUpdate(int major, int minor)
	{
		try
		{
			URL url = new URL(String.format("http://shadow.static-interface.de/VERSIONS/1.0/%d/AdventurIRC_1.0.%d.%d.jar", major, major, minor));
			InputStreamReader reader = new InputStreamReader(url.openStream());
			FileWriter writer = new FileWriter(new File(String.format("AdventurIRC_1.0.%d.%d.jar", major, minor)));
			int read = reader.read();
			while ( read != -1 )
			{
				writer.write(read);
				read = reader.read();
			}
			writer.close();
			return (new File(String.format("AdventurIRC_1.0.%d.%d.jar", major, minor)).exists());
		}
		catch (MalformedURLException e)
		{
		}
		catch (IOException e)
		{
		}
		return false;
	}
	
	public Bootstrap()
	{
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 302, 150);
		getContentPane().setLayout(null);
		
		JLabel lblTrageHierEinfach = new JLabel("Trage hier einfach deinen Nicknamen ein:");
		lblTrageHierEinfach.setBounds(10, 12, 281, 15);
		getContentPane().add(lblTrageHierEinfach);
		
		textField = new JTextField();
		textField.setBounds(10, 42, 280, 25);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnLosGehts = new JButton("und los geht's !");
		btnLosGehts.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if ( textField.getText().trim().equals("") ) return;
				if ( Character.isDigit(textField.getText().charAt(0)) );
				FileManager.setString(FileManager.CFG_NICKNAME, textField.getText().trim());
				main(null);
				dispose();
				return;
			}
		});
		btnLosGehts.setBounds(10, 69, 280, 40);
		getContentPane().add(btnLosGehts);
	}
}
class UpdateFinishedNotification extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	public UpdateFinishedNotification(String path)
	{
		try
		{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName())) 
				{
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		}
		catch ( UnsupportedLookAndFeelException e )
		{
		}
		catch (ClassNotFoundException e)
		{
		}
		catch (InstantiationException e)
		{
		}
		catch (IllegalAccessException e)
		{
		}
		
		JLabel label = new JLabel("<html> Es wurde erfolgreich ein Update durchgeführt ! Starte nun "+path+" um von den Änderungen zu profitieren !");
		setAlwaysOnTop(true);
		setSize(640, 150);
		label.repaint();
		add(label);
		setVisible(true);
	}
}