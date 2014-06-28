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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
		if ( !AdventurIRC.VERSION.substring(AdventurIRC.VERSION.indexOf(' ')+1).equals(remoteVersion) )
		{
			int majorRemoteVersion = Integer.parseInt(""+remoteVersion.charAt(5));
			int minorRemoteVersion = Integer.parseInt(""+remoteVersion.charAt(7));
			if ( downloadUpdate(majorRemoteVersion, minorRemoteVersion) )
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
			URL url = new URL("http://shadow.static-interface.de/AdventurIRC_Latest_Version");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String read = reader.readLine();
			reader.close();
			return read;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace(FileManager.logWriter);
			FileManager.logWriter.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace(FileManager.logWriter);
			FileManager.logWriter.flush();
		}
		return AdventurIRC.VERSION;
	}
	
	private static boolean downloadUpdate(int major, int minor)
	{
		try
		{
			URL url = new URL(String.format("http://shadow.static-interface.de/VERSIONS/AdventurIRC_Version/1.0/%d/AdventurIRC-1.0.%d.%d.jar", major, major, minor));
			InputStream is = url.openStream();
			File out = new File(String.format("AdventurIRC-1.0.%d.%d.jar", major, minor));
			FileOutputStream os = new FileOutputStream(out);
			int read = is.read();
			while ( read != -1 )
			{
				os.write(read);
				read = is.read();
			}
			os.close();
			return (new File(String.format("AdventurIRC-1.0.%d.%d.jar", major, minor)).exists());
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace(FileManager.logWriter);
			FileManager.logWriter.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace(FileManager.logWriter);
			FileManager.logWriter.flush();
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
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

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
			e.printStackTrace(FileManager.logWriter);
			FileManager.logWriter.flush();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace(FileManager.logWriter);
			FileManager.logWriter.flush();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace(FileManager.logWriter);
			FileManager.logWriter.flush();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace(FileManager.logWriter);
			FileManager.logWriter.flush();
		}

		JLabel label = new JLabel("<html> Es wurde erfolgreich ein Update durchgeführt ! Starte nun "+path+" um von den Änderungen zu profitieren !");
		setAlwaysOnTop(true);
		setSize(640, 150);
		label.repaint();
		add(label);
		setVisible(true);
	}
}