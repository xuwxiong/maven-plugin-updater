package intact.maven.plugins.versionviewer;

import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;
import intact.maven.plugins.versionupdater.source.WikiAccess;
import intact.maven.plugins.versionupdater.source.WikiPageManager;
import intact.maven.plugins.versionupdater.utils.Utils;
import intact.maven.plugins.versionupdater.utils.ViewOutputFormatEnum;

import java.util.StringTokenizer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "view", requiresProject = false, requiresOnline = true, requiresDirectInvocation = true)
public class ViewProject extends AbstractMojo
{
	private static final String SEPARATOR_VERSION_BCO_INDICATOR = "/";
	private static final String TOKEN_BCO_INDICATOR = "bco";
	
	@Parameter(property = "bco", required = false, readonly = true)
	protected boolean bcoRelease;	
	
	@Parameter(property = "format", required = true, defaultValue="xml", readonly = true)
	protected String format;
	
	@Parameter(property = "purpose", required = true, defaultValue="release", readonly = true)
	protected String purpose;

	@Parameter(property = "version", required = true, readonly = true)
	protected String version;

	private void normalizeArguments() 
	{
		StringTokenizer tokenizer = new StringTokenizer(this.version, SEPARATOR_VERSION_BCO_INDICATOR);
		
		if (tokenizer.hasMoreTokens())
		{
			this.version = Utils.normalizeVersion(tokenizer.nextToken());			
		}
		
		if (tokenizer.hasMoreTokens())
		{
			if (TOKEN_BCO_INDICATOR.equalsIgnoreCase(tokenizer.nextToken()))
			{
				this.bcoRelease = true;	
			}			
		}		
	}

	@Override
	public void execute()
	{		
		try
		{
			ProjectConfiguration prjConfig = null;
			
			normalizeArguments();			
			
			if (bcoRelease)
			{
				prjConfig = WikiPageManager.buildProjectConfig(version,WikiAccess.CRITERIA_BCO_RELEASE,false);
			}
			else
			{						
				prjConfig = WikiPageManager.buildProjectConfig(version,false);				
			}
			
			prjConfig.setDescription("Project Configuration for version ["+version+"]");
			
			getLog().info("Generating Content From Wiki version ["+version+"]");
			
			if (null != prjConfig)
			{				
				ProjectConfigurations configs = new ProjectConfigurations();
				configs.addProjectConfiguration(prjConfig);
				
				ViewOutputFormatEnum outputFormat = ViewOutputFormatEnum.fromCodeAndPurpose(format, purpose);
				
				String fileContentPath = Utils.generateProjectConfigurationFile(configs,outputFormat);
				
				if (null!= fileContentPath)
				{
					getLog().info("Generated Content , see ["+fileContentPath+"]");	
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		ViewProject viewPrj = new ViewProject();
		//viewPrj.projectsFile = "C:\\Hilali\\D13\\ReleaseScript\\projects.txt";
//		viewPrj.bcoRelease = true;
//		viewPrj.version = "ReleaseTestList ";
//		viewPrj.version = "BCO 5.13 D11 WRTY6";
//		viewPrj.version = "BCO6D13";
		viewPrj.version = "BCO62D14";
		viewPrj.format = "xml";
		viewPrj.purpose = "bom";
		
		viewPrj.execute();
		
	}
}
