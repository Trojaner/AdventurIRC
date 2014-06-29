package de.static_interface.shadow.adventurirc;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import de.static_interface.shadow.adventurirc.io.AdventurIRCConfiguration;
import de.static_interface.shadow.adventurirc.io.FileManager;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Bootstrap extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final URL latestVersionURL = getLatestVersionURL();
	private static final String localVersion = AdventurIRC.VERSION.substring(AdventurIRC.VERSION.indexOf(' ')+1);
	private static final String remoteVersion = getRemoteVersion();

	private static final URL getLatestVersionURL()
	{
		try
		{
			return new URL("http://shadow.static-interface.de/AdventurIRC_Latest_Version");
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace(FileManager.logWriter);
			return null;
		}
	}

	private static final String getRemoteVersion()
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(latestVersionURL.openStream(), StandardCharsets.UTF_8));
			String version = reader.readLine().trim();
			reader.close();
			return version;
		}
		catch (IOException e)
		{
			e.printStackTrace(FileManager.logWriter);
			return AdventurIRC.VERSION;
		}
	}

	private static void downloadVersion() throws IOException
	{
		File output = new File("AdventurIRC_"+remoteVersion+".jar");
		URL downloadURL = new URL("http://shadow.static-interface.de/latest_advirc.jar");
		double size = getFileSize(downloadURL);
		DownloadProgressBar bar = new DownloadProgressBar();
		BufferedReader reader = new BufferedReader(new InputStreamReader(downloadURL.openStream()));
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		int read = reader.read();
		double val = 0.0;
		while ( read != -1 )
		{
			val = (output.length()/size*59D);
			bar.setValue(val);
			writer.write(read);
			read = reader.read();
		}
		reader.close();
		writer.close();
		bar.dispose();
		bar = null;
	}

	private static int getFileSize(URL url) throws IOException
	{
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("HEAD");
		con.getInputStream();
		int length = con.getContentLength();
		con.disconnect();
		return length;
	}

	public static void main(String[] args)
	{
		if ( !Files.exists(AdventurIRCConfiguration.path) )
		{
			String newNickname = JOptionPane.showInputDialog("Füge hier deinen Nicknamen ein damit du chatten kannst !");
			FileManager.setString(FileManager.CFG_NICKNAME, newNickname);
		}

		String download = FileManager.getString(FileManager.CFG_DOWNLOAD_UPDATES);

		if ( localVersion.equals(remoteVersion) || download.equals("NO") )
		{
			new Thread(new AdventurIRC()).start();
			return;
		}
		if ( download.equals("PROMPT") )
		{
			int selection = JOptionPane.showConfirmDialog(null, "Ein Update ist verfügbar. Soll es heruntergeladen werden ?", "AdventurIRC – Update verfügbar", JOptionPane.YES_NO_OPTION);
			if ( selection != JOptionPane.OK_OPTION )
			{
				new Thread(new AdventurIRC()).start();
				return;
			}
		}

		try
		{
			downloadVersion();
		}
		catch (IOException e)
		{
			e.printStackTrace(FileManager.logWriter);
			return;
		}
		JOptionPane.showConfirmDialog(null, "Das Update wurde heruntergeladen.", "AdventurIRC – Download abgeschlossen", JOptionPane.PLAIN_MESSAGE);
		return;
	}
}
class DownloadProgressBar extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JProgressBar progressBar = new JProgressBar();

	private static final double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private static final double screenWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

	private static final double positionY = (screenHeight / 2)-(75/2);
	private static final double positionX = (screenWidth / 2)-200;

	public DownloadProgressBar()
	{
		add(progressBar);
		setBounds((int) positionX, (int) positionY, 400, 75);
		setTitle("AdventurIRC – Update herunterladen");
		progressBar.setMaximum(100);
		setResizable(false);
		setVisible(true);
	}

	public int getValue()
	{
		return progressBar.getValue();
	}

	public void setValue(double value)
	{
		progressBar.setValue((int) (value));
	}
}