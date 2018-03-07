package intact.maven.plugins.versionupdater.generatedoc;

import org.junit.Test;

public class GenerateDocMojoTest
{
	@Test
	public void testGenerate() throws Exception
	{
		GenerateDocMojo generateDocMojo = new GenerateDocMojo();
		
		generateDocMojo.paramOutputFolder = "data/generate-doc";
		generateDocMojo.paramProjectConfigurationsFile = GenerateDocMojoTest.class.getResource("project-configurations-for-doc.xml").getFile();
		
		generateDocMojo.execute();
	}
}
