package intact.maven.plugins.versionupdater;

import com.intact.commons.commonsutils.io.IOHelper;

public class UpdateProjectMojoToWikiDebugTest {
	
	
	public static void main(String[] args) 
	{
		UpdateProjectMojoToWiki updPrj = new UpdateProjectMojoToWiki();
		updPrj.version = IOHelper.askQuestionWithMandatoryAnswer("Wiki Version", "BCO62D14");
		updPrj.projectConfigurationsFile=IOHelper.askQuestionWithMandatoryAnswer("Project Configuration Update From", "");
		updPrj.purpose = IOHelper.askQuestionWithMandatoryAnswer("Purpose", "release");
		
		updPrj.execute();	
		
	}

}
