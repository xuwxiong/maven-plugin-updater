package intact.maven.plugins.versionupdater;

import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectDependency;
import intact.maven.plugins.versionupdater.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class UpdateProject extends AbstractMojo
{
	@Parameter(property = "rootDir", required = false, defaultValue="./", readonly = true)
	protected String rootDir;
	
	@Parameter(property = "projectsFile", required = true, readonly = true)
	protected String projectsFile;

	@Parameter(property = "version", required = true, readonly = true)
	protected String version;

	@Parameter(property = "defaultProjectVersion", required = false, readonly = true)
	protected String defaultProjectVersion;
	
	@Parameter(property = "includeModules", required = false, defaultValue="false",  readonly = true)
	protected String includeModules;	

	protected final Set<String> visitedDependencies = new HashSet<String>();

	@Override
	public void execute()
	{		
		try
		{	
			File projects = new File(rootDir+projectsFile);
			
			if (!projects.exists()) {
				throw new UpdateProjectMojoException(String.format("%s does not exists,", projects.toString()));
			}
			if (!projects.isFile()) {
				throw new UpdateProjectMojoException(String.format("%s is not a valid file,", projects.toString()));
			}

			ProjectConfiguration projectConfiguration = buildProjectConfiguration();
			
			getLog().info("Using project configuration " + projectConfiguration.getName());
			
			// Override project configuration file by command line value
			if (null != defaultProjectVersion)
			{
				getLog().info(String.format("Value of defaultProjectVersion is '%s'", defaultProjectVersion));
				projectConfiguration.setDefaultVersion(defaultProjectVersion);
			}
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

					try
					{
						updateProject(projectConfiguration,true,line,"true".equalsIgnoreCase(includeModules));
					}
					catch(UpdateProjectMojoException e)
					{
						getLog().error(String.format("Error managing project %s [%s]", line, e.getMessage()));
					}
					catch(ShowStopperException e)
					{
						getLog().error(String.format("Show stopper exception occured: %s", e.getMessage()));
						return;
					}
					catch(Exception e)
					{
						getLog().error(String.format("Error managing project %s [%s]", line, e.getMessage()), e);
					}
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
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected abstract ProjectConfiguration buildProjectConfiguration() throws Exception;
	
	@SuppressWarnings("unused")
	private void updateProject(ProjectConfiguration pProjectConfiguration,boolean pIgnoreTypeAndScope,String pProjectName) throws Exception
	{
		updateProject(pProjectConfiguration,pIgnoreTypeAndScope, pProjectName, false);
	}
	
	private void updateProject(ProjectConfiguration pProjectConfiguration,boolean pIgnoreTypeAndScope,String pProjectName,boolean updateSubs) throws Exception
	{
		File project = new File(rootDir+pProjectName);

		updateProject(pProjectConfiguration,pIgnoreTypeAndScope, project);
	}
	
	private void updateProject(ProjectConfiguration pProjectConfiguration,boolean pIgnoreTypeAndScope,File pProject) throws Exception
	{
		updateProject(pProjectConfiguration,pIgnoreTypeAndScope,pProject,false);
	}
	
	private void updateProject(ProjectConfiguration pProjectConfiguration,boolean pIgnoreTypeAndScope,File pProject,boolean updateSubs) throws Exception
	{
		if (!pProject.exists()) 
		{
			throw new UpdateProjectMojoException(String.format("Project %s in projectsFile does not exists", pProject.toString()));
		}
		
		if (!pProject.isDirectory()) 
		{
			throw new UpdateProjectMojoException(String.format("Project %s in projectsFile is not a valid folder", pProject.toString()));
		}

		getLog().debug("Loading project " + pProject.getAbsolutePath());

		Pom pom = new Pom(pProject);
		
		getLog().info("Updating project " + pProject.getAbsolutePath());
		
		if (updateSubs)
		{
			File[] modules = Utils.getModules(pProject);
			
			if ((null != modules) && (modules.length > 0))
			{
				for(File module:modules)
				{
					updateProject(pProjectConfiguration,pIgnoreTypeAndScope,module);
				}
			}
		}
		
		checkUpdateOrder(pom);
		
		pom.update(pProjectConfiguration,false);
		
		addDependencies(pom);
		
		updateProjectConfiguration(pom, pProjectConfiguration);
		
		pom.getVersion();
		
		getLog().debug("Saving project " + pProject.getAbsolutePath());
		
		pom.save();		
	}	
	
	private void addDependencies(Pom pPom)
	{
		// Add all dependencies found in this pom
		visitedDependencies.add(Utils.getKey(pPom));
		
		for (Dependency dependency : pPom.getDependencies())
		{
			visitedDependencies.add(Utils.getKey(dependency));
		}
		if (null != pPom.getDependencyManagement())
		{
			for (Dependency dependency : pPom.getDependencyManagement().getDependencies())
			{
				visitedDependencies.add(Utils.getKey(dependency));
			}
		}
		for (Pom pom : pPom.getSubPoms())
		{
			addDependencies(pom);
		}
	}

	private void checkUpdateOrder(Pom pPom) throws ShowStopperException
	{
		String key = Utils.getKey(pPom);
		if (visitedDependencies.contains(key))
		{
			throw new ShowStopperException(String.format("Project %s is being updated, but it is referenced by project already updated.", key));
		}
	}
	
	/*
	 * As soon as one project is updated, add this dependency to projectConfiguration
	 * in memory
	 */
	private static void updateProjectConfiguration(Pom pPom, ProjectConfiguration pProjectConfiguration)
	{
		ProjectDependency dependency = pProjectConfiguration.getDependency(pPom);
		
		if (null == dependency)
		{
			dependency = new ProjectDependency();
			dependency.setGroupId(pPom.getGroupId());
			dependency.setArtifactId(pPom.getArtifactId());
			dependency.setVersion(pPom.getVersion());
		}
		pProjectConfiguration.addDependency(dependency);
	}
}
