package intact.maven.plugins.versionupdater.dependencies;

import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;
import intact.maven.plugins.versionupdater.utils.ViewOutputFormatEnum;

import java.io.File;

public class ProjectConfigurationsParser 
{
	
	private ProjectConfigurationsParser()
	{
	}
	
	public static ProjectConfigurations parse(File pFile) throws ProjectConfigurationsException
	{
		return parse(pFile, ViewOutputFormatEnum.XML);
	}

	public static ProjectConfigurations parse(File pFile,ViewOutputFormatEnum outputFormat) throws ProjectConfigurationsException
	{
		if (null == pFile) {
			throw new ProjectConfigurationsException("pFile cannot be null");
		}
		if (!pFile.isFile()) {
			throw new ProjectConfigurationsException(String.format("%s is not a valid file,", pFile.toString()));
		}
		if (!pFile.exists()) {
			throw new ProjectConfigurationsException(String.format("%s does not exists,", pFile.toString()));
		}
		
		try {
			
			ProjectConfigurations projectConfigurations = outputFormat.read(pFile, ProjectConfigurations.class);
			
			validate(projectConfigurations);
			
			return projectConfigurations;
			
		} catch (Exception e) 
		{
			throw new ProjectConfigurationsException("Error while parsing file", e);
		}
	}
	
	private static void validate(ProjectConfigurations pProjectConfigurations) throws ProjectConfigurationsException
	{
		for (ProjectConfiguration project : pProjectConfigurations)
		{
			String parentKey = project.getParentKey();
			if (null != parentKey)
			{
				ProjectConfiguration parentProject = pProjectConfigurations.getProjectConfiguration(parentKey);
				project.setParent(parentProject);
			}
		}
	}
}
