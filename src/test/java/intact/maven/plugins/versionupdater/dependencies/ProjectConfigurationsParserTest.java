package intact.maven.plugins.versionupdater.dependencies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;

import java.io.File;

import org.apache.maven.model.Dependency;
import org.junit.Test;

public class ProjectConfigurationsParserTest {

	@Test
	public void test() throws Exception
	{
		ProjectConfigurations projectDependencies = ProjectConfigurationsParser.parse(new File("src/test/resources/project-configurations-simple.xml"));
		
		assertNotNull(projectDependencies);
		assertEquals(2, projectDependencies.getProjectConfigurations().size());
		
		ProjectConfiguration project54 = projectDependencies.getProjectConfiguration("5.4");
		assertNotNull(project54);
		assertEquals("5.4", project54.getName());
		assertEquals("5.4.0.0-SNAPSHOT", project54.getDefaultVersion());
		assertNull(project54.getParent());
		assertEquals(2, project54.getDependencies().size());
		
		Dependency dependencyToFind = new Dependency();
		dependencyToFind.setGroupId("a.b.c");
		dependencyToFind.setArtifactId("titi");

		
		assertNull(project54.getDependency(null, null,null));
		assertNull(project54.getDependency("", "", ""));
		assertNull(project54.getDependency("a.b.c", "", ""));
		assertNull(project54.getDependency("a.b.c.d", "titi", ""));
		assertNotNull(project54.getDependency("a.b.c", "titi", ""));
		Dependency dependencyFound = project54.getDependency("a.b.c", "titi", "");
		assertEquals("a.b.c", dependencyFound.getGroupId());
		assertEquals("titi", dependencyFound.getArtifactId());
		assertEquals("1.0.0", dependencyFound.getVersion());
		
		assertEquals(dependencyFound, project54.getDependency(dependencyToFind));
		
		System.out.println();
	}
}
