package intact.maven.plugins.versionupdater.utils;

import intact.maven.plugins.versionupdater.Pom;
import intact.maven.plugins.versionupdater.PomException;
import intact.maven.plugins.versionupdater.github.model.Commit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;


public class PomUtil {
	
	private static final String PATTERN_POM_DIR = "c:\\tmp\\%s\\";
	
	private static Base64 BASE64_DECODER = new Base64();

	public static boolean validateScm(String connection) {
		 return Consts.CONNECTION.equals(connection);
	}
	
	
	
	public static void incrementVersion(Pom pom, String version) {
		version = incrementVersion(version);
		pom.getModel().setVersion(version);
	}

	public static String incrementVersion(String version) {
		String[] versionInListString = StringUtils.split(version,'.');
		String branches = versionInListString[versionInListString.length-1];
		int value = Integer.valueOf(branches)+ 1;
		versionInListString[versionInListString.length-1]=String.valueOf(value);
		version="";
		for (String aVersionInListString : versionInListString) {
			version = version + aVersionInListString + ".";
		}
		version = StringUtils.substring(version, 0, version.length()-1);
		version = version + "-" + Consts.SNAPSHOT;
		return version;
	}

	public static String validateBranch(String version) {
		String[] tokens = version.split(Consts.SEPARATORS_REGEXP);
		for (int i = tokens.length-1 ; i > 0 ; i--){
			if (!StringUtils.isNumeric(tokens[i]) && !tokens[i].equals(Consts.SNAPSHOT)){
				return tokens[i];
			}
		}
		return Consts.MASTER;
	}

	public boolean validateProperties(Properties properties) {
		return properties.containsKey(Consts.SHORTGROUPID);
	}
	
	public static File buildPomFile(String pArtifactId,String pPomXMlContent) throws IOException
	{
		String dirName = String.format(PATTERN_POM_DIR,pArtifactId);
		File pomDir = new File(dirName);
		
		if (!pomDir.exists())
		{
			pomDir.mkdir();
		}
		
		File pomFile = new File(dirName+"pom.xml");
		
		FileWriter fileWriter = null;
		BufferedWriter bw = null;
		
		try
		{
			fileWriter = new FileWriter(pomFile);
			bw = new BufferedWriter(fileWriter);
			
			bw.write(pPomXMlContent);
		}
		finally
		{
			if (null != bw)
			{
				bw.close();				
			}
			
			if (null != fileWriter)
			{
				fileWriter.close();				
			}
		}
		
		
		return pomFile;
	}
	
	public static Model buildModel(String pArtifactId,String pPomXMlContent,boolean pToDecode) throws PomException
	{
		try {
			
			String pomXMlContent = pPomXMlContent;
			
			if (pToDecode)
			{
				pomXMlContent = decodeContent(pPomXMlContent);
			}			
			
			File pomFile = buildPomFile(pArtifactId,pomXMlContent);
			
			final MavenXpp3Reader mavenReader = new MavenXpp3Reader();
			final FileReader reader = new FileReader(pomFile);
			final Model model = mavenReader.read(reader);
			model.setPomFile(pomFile);
			
			return model;
		} catch (Exception e) {
			throw new PomException("Error reading pom Content.", e);
		}	
	}
	
	public static Model buildModel(String pArtifactId,String pPomXMlContent) throws PomException
	{
		return buildModel(pArtifactId,pPomXMlContent, false);
	}	
	
	public static List<String> getPomModules(String pArtifactId,String pPomXMlContent,boolean pToDecode)
	{
		List<String> modules = null;
		
		String pomXMlContent = pPomXMlContent;
		
		if (pToDecode)
		{
			pomXMlContent = decodeContent(pPomXMlContent);
		}
		
		try {
			Model model = buildModel(pArtifactId,pomXMlContent);
			
			modules = model.getModules();
		} catch (PomException e) {
			e.printStackTrace();
		}
		
		return modules;
	}	
	
	public static List<String> getPomModules(String pArtifactId,String pPomXMlContent)
	{
		return getPomModules(pArtifactId,pPomXMlContent, false);
	}
	
	public static String decodeContent(String pRawContent)
	{
		return new String(BASE64_DECODER.decode(pRawContent));
	}
	
	
	public static String extractArtifactId(intact.maven.plugins.versionupdater.github.model.Pom pPom)
	{
		String artifactId = pPom.getPath();
		artifactId = artifactId.substring(0,artifactId.lastIndexOf("pom.xml")-1);		
		
		return artifactId;
	}



	public static Pom mappEffectivePom(String pArtifactId,intact.maven.plugins.versionupdater.github.model.Pom pPom) throws Exception 
	{
		List<intact.maven.plugins.versionupdater.github.model.Pom>  pomModules =  pPom.getModules();
		
		List<String> pModulesName = new ArrayList<String>();
		List<String> pModulesContent = new ArrayList<String>();
		
		String name = null;
		
		for(intact.maven.plugins.versionupdater.github.model.Pom module:pomModules)
		{
			name = extractArtifactId(module);
			
			pModulesName.add(name);
			pModulesContent.add(decodeContent(module.getContent()));
		}
		
		
		return new Pom(pArtifactId,decodeContent(pPom.getContent()),pModulesName,pModulesContent);
	}
	
	public static void buildCommit(Commit commit,Pom effectivePom) throws Exception
	{
		FileReader fileReader = null;
		
		try
		{
			File file = effectivePom.getPomFile();
			
			fileReader = new FileReader(file);
			
			StringBuilder builder = new StringBuilder("");
			
			BufferedReader br = new BufferedReader(fileReader); 
			String s; 
			while((s = br.readLine()) != null) { 
				builder.append(s);
				builder.append("\n");
			}
			
			commit.setContent(new String(BASE64_DECODER.encode(builder.toString().getBytes())));
			
		}
		finally
		{
			if (null != fileReader)
			{
				fileReader.close();				
			}			
		}
	}

}