package intact.maven.plugins.versionupdater.utils;

import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;
import intact.maven.plugins.versionupdater.source.model.ItemsList;
import intact.maven.plugins.versionupdater.source.model.MetadataDataServices;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.commons.digester3.binder.DigesterLoader;

public class DigesterUtils extends FromAnnotationsRuleModule
{	
	private static DigesterUtils instance = new DigesterUtils();
	private static final DigesterLoader digesterLoader = newLoader(instance);
	
	private DigesterUtils(){}


	@Override
	protected void configureRules() {
		bindRulesFrom(ProjectConfigurations.class);
		bindRulesFrom(ItemsList.class);	
		bindRulesFrom(MetadataDataServices.class);
	}
	
	public static Digester getDigester()
	{
		return digesterLoader.newDigester();
	}
}
