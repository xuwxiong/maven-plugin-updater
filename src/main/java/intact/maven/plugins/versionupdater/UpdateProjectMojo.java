package intact.maven.plugins.versionupdater;

import intact.maven.plugins.versionupdater.dependencies.ProjectConfigurationsParser;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;
import intact.maven.plugins.versionupdater.source.WikiPageManager;
import intact.maven.plugins.versionupdater.utils.Utils;
import intact.maven.plugins.versionupdater.utils.ViewOutputFormatEnum;

import java.io.File;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "update", requiresProject = false, requiresOnline = true, requiresDirectInvocation = true)
public class UpdateProjectMojo extends UpdateProject
{

	@Parameter(property = "projectConfigurationsFile", required = true, readonly = true)
	protected String projectConfigurationsFile;

	@Override
	protected ProjectConfiguration buildProjectConfiguration() throws Exception {
		
		String format = validateFormat(projectConfigurationsFile);

		File projectConfigurationsF = Utils.getFile(projectConfigurationsFile);
		
		getLog().info("Loading project configurations " + projectConfigurationsF.getAbsolutePath());		
		
		ViewOutputFormatEnum outputFormat = ViewOutputFormatEnum.fromCodeAndPurpose(format, ViewOutputFormatEnum.PURPOSE_RELEASE);
		
		ProjectConfigurations projectConfigurations = ProjectConfigurationsParser.parse(projectConfigurationsF,outputFormat);
		
		ProjectConfiguration projectConfiguration = WikiPageManager.buildProjectConfig(projectConfigurations, version,true);

		return projectConfiguration;
	}

	private String validateFormat(String pFileName) 
	{
		String resultFormat = null;
		boolean isValidExtension = false;
		
		int indexOfExtension = pFileName.lastIndexOf(".");
		
		if (indexOfExtension >= 0)
		{
			resultFormat = pFileName.substring(indexOfExtension+1);
			
			isValidExtension = ViewOutputFormatEnum.isValidEnum(resultFormat, "");
		}
		
		if(!isValidExtension)
		{
			throw new ProjectMojoException(String.format("%s is not a valid file,", pFileName));
		}
		
	
		return resultFormat;
	}
}
