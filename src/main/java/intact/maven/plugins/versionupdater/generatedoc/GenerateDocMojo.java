package intact.maven.plugins.versionupdater.generatedoc;

import intact.maven.plugins.versionupdater.dependencies.ProjectConfigurationsParser;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;
import intact.maven.plugins.versionupdater.utils.Utils;

import java.io.File;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "generate-doc", requiresProject = false, requiresOnline = true, requiresDirectInvocation = true)
public class GenerateDocMojo extends AbstractMojo
{
	@Parameter(property = "projectConfigurationsFile", required = true, readonly = true)
	protected String paramProjectConfigurationsFile;

	@Parameter(property = "outputFolder", required = true, readonly = true)
	protected String paramOutputFolder;

	@Parameter(property = "isProcess", required = false, readonly = true)
	protected String isProcess;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		try
		{			
			File projectConfigurationsF = Utils.getFile(paramProjectConfigurationsFile);
			
			getLog().info("Loading project configurations " + projectConfigurationsF.getAbsolutePath());
			
			ProjectConfigurations projectConfigurations = ProjectConfigurationsParser.parse(projectConfigurationsF);
			
			File outputFolder = new File(paramOutputFolder);
			
			if (!outputFolder.exists()) {
				throw new GenerateDocMojoMojoException(String.format("%s does not exists,", outputFolder.toString()));
			}
			if (!outputFolder.isDirectory()) {
				throw new GenerateDocMojoMojoException(String.format("%s is not a valid folder,", outputFolder.toString()));
			}
			
			String xslFileName = "project-configuration-transformer.xsl";
			
			if (isProcess != null){
				xslFileName = "project-configuration-transformer_bp.xsl";
			}
			Source xsl = new StreamSource(this.getClass().getResource(xslFileName).openStream());
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer(xsl);
			
			for (ProjectConfiguration projectConfiguration : projectConfigurations)
			{
				getLog().info("Using project configuration " + projectConfiguration.getName());
			
				transformer.setParameter("project-configuration-name", projectConfiguration.getName());
				transformer.transform(new StreamSource(new StringReader(projectConfiguration.toXML(true))), new StreamResult(new File(outputFolder, projectConfiguration.getName() + ".html")));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
