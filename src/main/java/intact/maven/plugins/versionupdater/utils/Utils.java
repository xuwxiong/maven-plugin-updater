package intact.maven.plugins.versionupdater.utils;

import intact.maven.plugins.versionupdater.Pom;
import intact.maven.plugins.versionupdater.dependencies.model.IDelegableDetailView;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;
import intact.maven.plugins.versionupdater.source.model.ItemEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Dependency;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

public class Utils
{
	public static final String PATTERN_FILE_PROJECT_CONFIG = "project-configuration.%s";
	
	private static final DependencyKeyTransformer DEPENDENCY_KEY_TRANSFORMER = new DependencyKeyTransformer();
	
	private static final DirHavingPomFilter MODULES_FILTER = new DirHavingPomFilter();
	
	private static final String FILE_NAME_SEPARATOR = File.separator;
	
	public static String getKey(Pom pPom)
	{
		return getKey(pPom.getGroupId(), pPom.getArtifactId());
	}
	
	public static String getKeyPomAsDependency(Pom pPom)
	{
		return getKey(pPom.getGroupId(), pPom.getArtifactId(),"","");
	}	
	
	public static String getKey(Dependency pDependency)
	{
		return getKey(pDependency.getGroupId(), pDependency.getArtifactId(), pDependency.getType(), pDependency.getScope());
	}
	
	public static String getKeyIgnoringTypeAnsCope(Dependency pDependency)
	{
		return getKey(pDependency.getGroupId(), pDependency.getArtifactId());
	}
	
	public static String getKey(ItemEntry pEntry)
	{
		return getKey(pEntry.getGroupId(), pEntry.getArtifactId(), pEntry.getType(), pEntry.getScope());
	}	
	
	public static String getKey(String pGroupId, String pArtifactId,String pType,String pScope)
	{
		return String.format("%s:%s:%s", getKey(pGroupId, pArtifactId),(StringUtils.isNotBlank(pType)  ? pType:"jar"),(StringUtils.isNotBlank(pScope)  ? pScope:""));
	}
	
	public static String getKey(String pGroupId, String pArtifactId)
	{
		return String.format("%s:%s", pGroupId, pArtifactId);
	}
	
	public static File getFile(String pFilename) throws IOException
	{		
		return getFile(pFilename, ViewOutputFormatEnum.CODE_XML);
	}
	
	public static File getFile(String pFilename,String pExtension) throws IOException
	{
		File file = new File(pFilename);
		if (file.exists())
		{
			return file;
		}
		// try URL
		URL url = new URL(pFilename);
		file = File.createTempFile("project-configurations", pExtension);
		file.deleteOnExit();
		FileUtils.copyURLToFile(url, file);
		return file;
	}	
	
	public static String generateProjectConfigurationFile(ProjectConfigurations pConfigs,ViewOutputFormatEnum pFormat) throws Exception
	{
		String resultStr = null;
		
		StringBuffer buffer = new StringBuffer("");
		if (pFormat.isHeaderNeeded())
		{
			buffer.append(pFormat.getHeader());
			buffer.append("\n");
		}
		
		String content = pFormat.printFormat(pConfigs);
		
		buffer.append(content);
		
		String fileName = String.format(PATTERN_FILE_PROJECT_CONFIG, pFormat.getCode());
		
		File outputFile = new File(fileName);
		
		FileUtils.writeStringToFile(outputFile,buffer.toString());
		
		resultStr = outputFile.getAbsolutePath();
		
		return resultStr;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getDependenciesAsKeys(List<Dependency> pDependencies)
	{
		if (null == pDependencies)
		{
			return null;
		}
		
		List<String> dependenciesAsKeys = new ArrayList<String>();
		
		dependenciesAsKeys.addAll(CollectionUtils.collect(pDependencies, DEPENDENCY_KEY_TRANSFORMER));
		
		return dependenciesAsKeys;
	}
	
	static class DependencyKeyTransformer implements Transformer
	{
		@Override
		public Object transform(Object input) {
			if (input instanceof Dependency)
			{
				Dependency dependency = (Dependency) input;
				return getKey(dependency);
			}
			
			return null;
		}		
	}

	
	public static void buildProjectArtifactList(List<String> projectsArtifact,File projects)  throws Exception
	{
		BufferedReader breader = null;
		
		try
		{
			FileReader reader = new FileReader(projects);
			breader = new BufferedReader(reader);
			String line;
			while ((line = breader.readLine()) != null)
			{
				if (line.isEmpty()) 
				{
					continue;
				}
				
				projectsArtifact.add(line);
			}
		}
		finally
		{
			if (null != breader)
			{
				breader.close();
			}
		}	
		
	}
	
	static class ClassJSonViewComparator implements Comparator<Class<?>>
	{
		public static final ClassJSonViewComparator INSTANCE = new ClassJSonViewComparator();

		@Override
		public int compare(Class<?> arg0, Class<?> arg1) {
			
			if (arg1.isAssignableFrom(arg0))
			{
				return 0;
			}
			
			return arg0.getName().compareTo(arg1.getName());
		}
		
	}
	
	public static String[] getFieldsWithAnnotation(Object pObj,Class<? extends ContentViews.Normal> pViewClass)
	{
		String[] fieldsSelectionResult = null;
		
		Class<?> objClass = null;
		JsonView jsonViewAnnotation = null;
		JsonProperty jsonPropertyAnnotation = null;
		Class<?>[] jsonViewValues = null;
		List<String> fieldsSelectionList = new ArrayList<String>();
		String fieldName = null;
		
		if (pObj instanceof IDelegableDetailView)
		{
			objClass = ((IDelegableDetailView)pObj).getDetailViewClass();
		}
		else
		{
			objClass = pObj.getClass();	
		}
		
		for(Field field : objClass.getDeclaredFields())
		{
			jsonViewAnnotation = field.getAnnotation(JsonView.class);
			
			if (null != jsonViewAnnotation)
			{
				jsonViewValues = jsonViewAnnotation.value();
				
				fieldName = field.getName();
				
				jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
				
				if (null != jsonPropertyAnnotation)
				{
					if (StringUtils.isNotBlank(jsonPropertyAnnotation.value()))
					{
						fieldName = jsonPropertyAnnotation.value();
					}
				}
				
				if (0 <= Arrays.binarySearch(jsonViewValues, pViewClass,ClassJSonViewComparator.INSTANCE))
				{
					fieldsSelectionList.add(fieldName);
				}
			}			
		}
		
		if (!fieldsSelectionList.isEmpty())
		{
			fieldsSelectionResult = new String[fieldsSelectionList.size()];
			fieldsSelectionList.toArray(fieldsSelectionResult);
		}
		
		return fieldsSelectionResult;
	}
	
	/** BCO 6.2 D14 --> BCO62D14
	 * 
	 * @param pVersion
	 * @return
	 */
	public static String normalizeVersion(String pVersion)
	{
		String versionNormalized = null;
		
		if(StringUtils.isNotBlank(pVersion))
		{
			versionNormalized = pVersion.replaceAll("[^A-Za-z0-9]", "");
		}
		
		return versionNormalized;
	}
	
	public static File[] getModules(File pProjectDir)
	{
		File[] resultFilesList = pProjectDir.listFiles(MODULES_FILTER);
		
		return resultFilesList;
	}
	
	static class DirHavingPomFilter implements FileFilter
	{

		@Override
		public boolean accept(File pathname) 
		{
			boolean resultFlag = false;
			
			if ((null != pathname) && (pathname.isDirectory()))
			{
				resultFlag = getPomFile(pathname).exists();
			}
			
			return resultFlag;
		}
		
		private File getPomFile(File pathname)
		{
			File resultFile = null;
			
			String fileName = pathname.getAbsolutePath().trim();
			
			if (!fileName.endsWith(FILE_NAME_SEPARATOR))
			{
				fileName += FILE_NAME_SEPARATOR;
			}
			
			fileName += "pom.xml";
			
			resultFile = new File(fileName);
			
			return resultFile;
		}
		
	}
}
