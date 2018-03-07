package intact.maven.plugins.versionupdater.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils
{
	public static File cloneFile(File pOriginalFile, File pDestFile) throws IOException
	{
		FileReader reader = new FileReader(pOriginalFile);
		BufferedReader breader = new BufferedReader(reader);
		FileWriter writer = new FileWriter(pDestFile);
		
		String line;
		while ((line = breader.readLine()) != null)
		{
			writer.write(line);
			writer.write('\n');
		}
		reader.close();
		writer.close();
		return pDestFile;
	}
	
	
	
	public static void cloneFolder(File pSrcFolder, File pDestFolder) throws IOException
	{
		pDestFolder.mkdirs();
		
		for (File file : pSrcFolder.listFiles())
		{
			if (file.isDirectory())
			{
				cloneFolder(file, new File(pDestFolder, file.getName()));
			}
			else
			{
				cloneFile(file, new File(pDestFolder, file.getName()));
			}
		}
	}
	
	
	public static File cloneFileToPom(File pSrcFolder, File pDestFolder) throws IOException
	{
		pDestFolder.mkdirs();
		return cloneFile(pSrcFolder, new File(pDestFolder,"pom.xml"));
	}

}
