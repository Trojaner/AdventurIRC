package de.static_interface.shadow.shadconfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;

public class Configuration
{
	Path path;
	HashMap<String, String> savedData = new HashMap<>();
	
	public Configuration(File path)
	{
		this.path = path.toPath();
		load();
	}
	
	public Configuration(Path path)
	{
		this.path = path;
		load();
	}
	
	public Configuration(String path)
	{
		this.path = Paths.get(path);
		load();
	}
	
	private void load()
	{
		if ( Files.exists(path) )
		{
			try ( BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8) )
			{
				String readLine = reader.readLine();
				while ( readLine != null )
				{
					savedData.put(readLine.substring(0, readLine.indexOf('|')), readLine.substring(readLine.indexOf('|')+1));
					readLine = reader.readLine();
				}
				reader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				if ( !Files.exists(path.getParent()) )
				{
					Files.createDirectories(path.getParent());
				}
				
				Files.createFile(path);
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
		}
	}
	
	public void putString(String key, String value)
	{
		if ( savedData.get(key) != null )
		{
			savedData.remove(key);
		}
		savedData.put(key, value);
	}
	
	public String getString(String key)
	{
		return savedData.get(key);
	}
	
	public void save()
	{
		try
		{
			BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
			Thread.sleep(50);
			for ( Entry<String, String> e : savedData.entrySet() )
			{
				writer.write(e.getKey()+"|"+e.getValue());
				writer.newLine();
			}
			writer.flush();
			writer.close();
		}
		catch ( IOException | InterruptedException e )
		{
			e.printStackTrace();
		}
	}	
}
