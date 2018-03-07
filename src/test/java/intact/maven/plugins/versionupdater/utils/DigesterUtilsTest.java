package intact.maven.plugins.versionupdater.utils;

import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;
import intact.maven.plugins.versionupdater.source.model.ItemEntry;
import intact.maven.plugins.versionupdater.source.model.ItemExtendedEntry;
import intact.maven.plugins.versionupdater.source.model.ItemsList;
import junit.framework.Assert;

import org.apache.commons.digester3.Digester;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class DigesterUtilsTest
{	
	private static final String XML_FILE_WIKI = "../source/wikifeed.xml";
	private static final String XML_FILE_PROJECT_CONFIG = "../project-configuration.xml";
	
	private Digester digester = DigesterUtils.getDigester();
	
	private ItemsList itemsList;
	private ProjectConfigurations projectConfigs;
	
	@Before
	public void initList() throws Exception
	{
		itemsList = digester.parse(getClass().getResourceAsStream(XML_FILE_WIKI));
		projectConfigs = digester.parse(getClass().getResourceAsStream(XML_FILE_PROJECT_CONFIG));
	}
	
	@Test
	public void testWikiList() throws Exception
	{
		System.out.println(itemsList.getId());
		
		ItemExtendedEntry entry = itemsList.getEntry("https://teamsites.iad.ca.inet/ifc/SOADevelopment/_vti_bin/listdata.svc/BCO62D14(192)");
		
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		
		String jsonEntryValue = mapper.writerWithView(ContentViews.Release.class).writeValueAsString(entry.getContent().getProperties());
		
		System.out.println(jsonEntryValue);
		
		jsonEntryValue = mapper.writerWithView(ContentViews.Bom.class).writeValueAsString(entry.getContent().getProperties());
		
		System.out.println(jsonEntryValue);
		
		jsonEntryValue = mapper.writerWithView(ContentViews.Bom.class).writeValueAsString(entry);
		
		System.out.println(jsonEntryValue);
		
		jsonEntryValue = mapper.writerWithView(ContentViews.Release.class).writeValueAsString(entry);
		
		System.out.println(jsonEntryValue);
		
		Assert.assertEquals("Title Should be BCO62D14","BCO62D14",itemsList.getTitle());
	}
	
	@Test
	public void testProjectConfigList() throws Exception
	{
		System.out.println(itemsList.getId());
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		
		ProjectConfiguration projectConfig = projectConfigs.iterator().next();
		
		ItemsList itemsList = ConverterFromProjectConfigurationsToWikiList.convertProjectDependencyToItemEntry(projectConfig);
				
		String jsonEntryValue = null;
		for(ItemEntry entry:itemsList)
		{
			jsonEntryValue = mapper.writerWithView(ContentViews.Release.class).writeValueAsString(entry);
			
			System.out.println(jsonEntryValue);
		}
		
		jsonEntryValue = mapper.writerWithView(ContentViews.BomRelease.class).writeValueAsString(projectConfig);
		System.out.println(jsonEntryValue);
	}
}
