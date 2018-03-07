package intact.maven.plugins.versionupdater;

import intact.maven.plugins.versionupdater.utils.FileUtils;

import java.io.File;

import org.junit.Test;

public class UpdateProjectMojoTest {
	
	@Test
	public void testUpdateWithUnknownProject() throws Exception
	{
		File workingFolder = new File("data/junit-test/5.5");
		File singleModuleProjectFolder = new File(workingFolder, "single-module-project");
		
		FileUtils.cloneFolder(new File("data/simple-library"), new File(workingFolder, "simple-library"));
		FileUtils.cloneFolder(new File("data/single-module-project"), singleModuleProjectFolder);

		File projectsFile = new File(workingFolder, "projects-with-unknown-project.txt");
		FileUtils.cloneFile(new File("data/projects-with-unknown-project.txt"), projectsFile);
		
		UpdateProjectMojo updateProjectMojo = new UpdateProjectMojo();
		
		updateProjectMojo.projectsFile = projectsFile.getAbsolutePath();
		updateProjectMojo.projectConfigurationsFile = "src/test/resources/project-configurations.xml";
		updateProjectMojo.version = "5.5";
		
		updateProjectMojo.execute();
		
		Pom updatedPom = new Pom(singleModuleProjectFolder,true);
		
		PomValidator.validateSingleModule5_5(updatedPom);
	}
	
	
}
