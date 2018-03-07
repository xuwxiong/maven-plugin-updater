package intact.maven.plugins.versionupdater;

import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.source.WikiPageManager;

import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "updateFromWiki", requiresProject = false, requiresOnline = true, requiresDirectInvocation = true)
public class UpdateProjectMojoFromWiki extends UpdateProject
{

	@Override
	protected ProjectConfiguration buildProjectConfiguration() throws Exception 
	{
		ProjectConfiguration projectConfiguration = WikiPageManager.buildProjectConfig(version,true);

		return projectConfiguration;
	}
	
	public static void main(String[] args) 
	{
		UpdateProjectMojoFromWiki updtMnger = new UpdateProjectMojoFromWiki();
		
		updtMnger.rootDir = "C:\\dev\\workspaceUpdate622\\";
		updtMnger.projectsFile = "projects.txt";
		updtMnger.includeModules = "false";
		updtMnger.version = "BCO62D14";
		
		updtMnger.execute();		
	}
}
