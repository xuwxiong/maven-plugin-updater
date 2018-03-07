package intact.maven.plugins.versionupdater;

import intact.maven.plugins.versionupdater.dependencies.ProjectConfigurationsParser;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;
import intact.maven.plugins.versionupdater.source.WikiPageManager;
import intact.maven.plugins.versionupdater.utils.Utils;
import intact.maven.plugins.versionupdater.utils.ViewOutputFormatEnum;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "updateWiki", requiresProject = false, requiresOnline = true, requiresDirectInvocation = true)
public class UpdateProjectMojoToWiki extends AbstractMojo
{
	@Parameter(property = "version", required = true, readonly = true)
	protected String version;

	@Parameter(property = "projectConfigurationsFile", required = true, readonly = true)
	protected String projectConfigurationsFile;
	
	@Parameter(property = "purpose", required = true, defaultValue="release", readonly = true)
	protected String purpose;

	@Override
	public void execute() 
	{
		try
		{
			String format = validateFormat(projectConfigurationsFile);
			
			File projectConfigurationsF = Utils.getFile(projectConfigurationsFile);
			
			getLog().info("Loading project configurations " + projectConfigurationsF.getAbsolutePath());
			
			ViewOutputFormatEnum outputFormat = ViewOutputFormatEnum.fromCodeAndPurpose(format, purpose);
			
			ProjectConfigurations projectConfigurations = ProjectConfigurationsParser.parse(projectConfigurationsF,outputFormat);
			ProjectConfiguration projectConfiguration = projectConfigurations.iterator().next();
			
			ViewOutputFormatEnum outputFormatToUpdate = ViewOutputFormatEnum.fromCodeAndPurpose(ViewOutputFormatEnum.CODE_JSON, purpose);
			
			WikiPageManager.updateWikiEntries(version,outputFormatToUpdate,projectConfiguration);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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

	public static void main(String[] args) {
		
		UpdateProjectMojoToWiki updPrj = new UpdateProjectMojoToWiki();
		updPrj.version = "ReleaseTestList ";
		updPrj.projectConfigurationsFile="C:\\dev\\workspaceReleaseTest\\project-version-updater-maven-plugin\\project-configuration.json";
		updPrj.purpose = "release";
		
		updPrj.execute();
		
	}
}
