package de.static_interface.shadow.adventurirc.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AdventurIRCLog
{
	private BufferedWriter writer = null;
	private Path path = null;

	public AdventurIRCLog(String servername, String channelorusername)
	{
		path = Paths.get(AdventurIRCConfiguration.path.toAbsolutePath().toString()+File.separator+"logs"+File.separator+servername+File.separator+channelorusername+".txt");
		if ( !Files.exists(path.getParent()) )
		{
			try
			{
				Files.createDirectories(path.getParent());
				//Makes the thread "sleep", in case the file system did not yet create the directory
				while ( !Files.exists(path.getParent()) );
				Files.createFile(path);
				//Makes the thread "sleep", in case the file system did not yet create the file
				while ( !Files.exists(path) );
				writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
			}
			catch (IOException e)
			{
				e.printStackTrace(FileManager.logWriter);
			}
		}
	}

	public void write(String toWrite)
	{
		if ( writer == null ) try
		{
			writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			e.printStackTrace(FileManager.logWriter);
		}
		new Thread(new WriterThread(toWrite, writer)).start();
	}
}
class WriterThread extends Thread
{
	private String toWrite;
	private BufferedWriter writer;

	public WriterThread(String toWrite, BufferedWriter writer)
	{
		this.writer = writer;
		this.toWrite = toWrite;
	}

	@Override
	public void run()
	{
		try
		{
			writer.write(toWrite.concat("\n"));
			writer.flush();
			return;
		}
		catch (IOException e)
		{
			e.printStackTrace(FileManager.logWriter);
			return;
		}
	}
}