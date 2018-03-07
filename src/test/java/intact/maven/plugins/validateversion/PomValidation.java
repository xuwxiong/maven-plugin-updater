package intact.maven.plugins.validateversion;

import intact.maven.plugins.versionupdater.Pom;
import intact.maven.plugins.versionupdater.PomValidator;
import intact.maven.plugins.versionupdater.dependencies.ProjectConfigurationsException;
import intact.maven.plugins.versionupdater.dependencies.ProjectConfigurationsParser;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;
import intact.maven.plugins.versionupdater.utils.FileUtils;

import java.io.File;

import org.junit.Test;

public class PomValidation {

	@Test
	public void testSingleModuleUpdateTo5_4() throws Exception
	{
		File workingFolder = new File("data/junit-test/5.4/single-module-project");
		FileUtils.cloneFolder(new File("data/single-module-project"), workingFolder);
		
		Pom pom = new Pom(workingFolder);
		
		PomValidator.validateSingleModule5_3(pom);
		
		pom.update(buildProjectConfigurations().getProjectConfiguration("5.4"));
		pom.save();

		PomValidator.validateSingleModule5_4(new Pom(workingFolder));
	}

	

	private ProjectConfigurations buildProjectConfigurations() throws ProjectConfigurationsException
	{
		return ProjectConfigurationsParser.parse(new File("src/test/resources/project-configurations.xml"));
		/*
		ProjectDependencies projectDependencies = new ProjectDependencies();
		Project project = new Project();
		project.setName("5.4");
		Dependency dependency = new Dependency();
		dependency.setGroupId("intact.business-object-domain.core-business-components");
		dependency.setArtifactId("bco-bm-jaxb2-personal");
		dependency.setVersion("NEW-VERSION-BCO");
		project.addDependency(dependency);
		dependency = new Dependency();
		dependency.setGroupId("intact.business-object-domain");
		dependency.setArtifactId("domain-business-values");
		dependency.setVersion("NEW-VERSION-ENUM");
		project.addDependency(dependency);
		
		return projectDependencies;
		*/
	}
}
