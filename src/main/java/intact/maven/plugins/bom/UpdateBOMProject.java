package intact.maven.plugins.bom;

import intact.maven.plugins.versionupdater.Pom;
import intact.maven.plugins.versionupdater.UpdateProjectMojoException;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectDependency;
import intact.maven.plugins.versionupdater.source.WikiPageManager;
import intact.maven.plugins.versionupdater.utils.Utils;
import intact.maven.plugins.versionupdater.utils.ViewOutputFormatEnum;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "updateBOM", requiresProject = false, requiresOnline = true, requiresDirectInvocation = true)
public class UpdateBOMProject extends AbstractMojo
{
	private static final String PATTERN_MESSAGE_UPDATING_POM_FROM_WIKI = "Updating POM  [%s] From Wiki Version [%s]";
	private static final String PATTERN_MESSAGE_UPDATING_WIKI_FROM_POM = "Updating Wiki Version [%s] From POM [%s]";
	
	private static final String PATTERN_MESSAGE_POM_UPDATED_SUCCESS = "POM Updated On [%s] From Wiki Version [%s]";
	private static final String PATTERN_MESSAGE_POM_UPDATED_FAIL 	= "POM [%s] Not Updated From Wiki Version [%s]";
	
	private static final String PATTERN_MESSAGE_WIKI_UPDATED_SUCCESS = "Wiki Version [%s] Updated From POM [%s]";
	private static final String PATTERN_MESSAGE_WIKI_UPDATED_FAIL 	 = "Wiki Version [%s] Not Updated From POM [%s]";
	
	@Parameter(property = "version", required = true, readonly = true)
	protected String version;
	
	@Parameter(property = "pomDirectory", required = true, defaultValue="./",readonly = true )
	protected String pomDirectory;
	
	@Parameter(property = "direction", required = true, defaultValue="pom", readonly = true)
	protected String direction;	

	@Override
	public void execute()
	{		
		try
		{
			if (!UpdateBOMDirectionEnum.isValidCode(direction))
			{
				throw new UpdateProjectMojoException("Directon property (-Ddirection) not valid. Allowed values "+UpdateBOMDirectionEnum.VALUES_LIST);
			}
			
			File projectDirectory = new File(pomDirectory);
			
			if (!projectDirectory.exists()) {
				throw new UpdateProjectMojoException(String.format("%s does not exists,", projectDirectory.toString()));
			}
			if (!projectDirectory.isDirectory()) {
				throw new UpdateProjectMojoException(String.format("%s is not a valid directory,", projectDirectory.toString()));
			}
			
			Pom pom = new Pom(projectDirectory);
			
			if (isToUpdatePom())
			{			
				updatePOM(pom);
			}
			else
			{
				updateWiki(pom);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void updateWiki(Pom pPom) throws Exception
	{
		String messageLog = null;
		String bomType = pPom.getArtifactId();		
		
		messageLog = String.format(PATTERN_MESSAGE_UPDATING_WIKI_FROM_POM, version,bomType);		
		getLog().info(messageLog);
					
		ProjectConfiguration projectConfig = buildProjectConfigFromDependencyManagement(pPom);
		
		WikiPageManager.updateWikiEntries(version,ViewOutputFormatEnum.JSON_BOM,projectConfig);
		
		messageLog = String.format(PATTERN_MESSAGE_WIKI_UPDATED_SUCCESS, version,bomType);
		
		getLog().info(messageLog);	
	}
	
	private ProjectConfiguration buildProjectConfigFromDependencyManagement(Pom pPom) throws IllegalAccessException, InvocationTargetException 
	{
		ProjectConfiguration projectConfig = new ProjectConfiguration(false);
		
		projectConfig.setName(version);
		
		List<Dependency> dependencies = pPom.getDependencyManagement().getDependencies();
		
		for(Dependency dependency:dependencies)
		{
			projectConfig.addDependency(new ProjectDependency(dependency));
		}		
		
		return projectConfig;
	}

	private void updatePOM(Pom pPom) throws Exception
	{
		String messageLog = null;
		
		String bomType = pPom.getArtifactId();
		
		messageLog = String.format(PATTERN_MESSAGE_UPDATING_POM_FROM_WIKI, bomType,version);		
		getLog().info(messageLog);
					
		ProjectConfiguration projectConfig = WikiPageManager.buildBOM(version,bomType);
					
		if (null != projectConfig)
		{
			syncDependencyManagement(pPom,projectConfig);
			
			pPom.update(projectConfig,true);
			
			pPom.save();
			
			messageLog = String.format(PATTERN_MESSAGE_POM_UPDATED_SUCCESS, pPom.getPomFile().getAbsolutePath(),version);
			getLog().info(messageLog);				
		}
		else
		{
			messageLog = String.format(PATTERN_MESSAGE_POM_UPDATED_FAIL, bomType,version);
			getLog().info(messageLog);
		}			
	}
	
	private void syncDependencyManagement(Pom pPom, ProjectConfiguration projectConfig) 
	{
		List<Dependency> dependencies = pPom.getDependencyManagement().getDependencies();
		
		List<String> exsitingPomDependencies = Utils.getDependenciesAsKeys(dependencies);
		List<String> incomingDependencies = Utils.getDependenciesAsKeys(projectConfig.getDependencies());
		List<Dependency> dependenciesToRemove = new ArrayList<Dependency>();
		
		String currentDependencyKey = null;
		
		for(ProjectDependency dependency:projectConfig)
		{			
			currentDependencyKey = Utils.getKey(dependency);
			
			if(!exsitingPomDependencies.contains(currentDependencyKey))
			{
				pPom.addDependencyManagement(dependency);
			}
		}
		
		dependencies = pPom.getDependencyManagement().getDependencies();
		exsitingPomDependencies = Utils.getDependenciesAsKeys(dependencies);		
		
		for(Dependency dependency:dependencies)
		{
			currentDependencyKey = Utils.getKey(dependency);
			
			if(!incomingDependencies.contains(currentDependencyKey))
			{
				dependenciesToRemove.add(dependency);
			}
		}
		
		pPom.removeDependencyManagement(dependenciesToRemove);
	}
	
	private boolean isToUpdatePom()
	{
		return UpdateBOMDirectionEnum.isPOMDirection(this.direction);
	}

	public static void main(String[] args) {
		
		UpdateBOMProject upPrj = new UpdateBOMProject();
		upPrj.version = "PluginTestList";
		upPrj.pomDirectory = "C:\\Hilali\\D13\\ReleaseScript\\Tests";
		upPrj.direction = "wiki";
		
		upPrj.execute();
		
	}
}
