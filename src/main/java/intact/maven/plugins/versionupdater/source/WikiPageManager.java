package intact.maven.plugins.versionupdater.source;
import intact.maven.plugins.versionupdater.UpdateProjectMojoException;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;
import intact.maven.plugins.versionupdater.source.model.ItemsList;
import intact.maven.plugins.versionupdater.source.model.MetadataDataServices;
import intact.maven.plugins.versionupdater.source.model.MetadataEntry;
import intact.maven.plugins.versionupdater.utils.ConverterFromProjectConfigurationsToWikiList;
import intact.maven.plugins.versionupdater.utils.ConverterFromWikiListToProjectConfigurations;
import intact.maven.plugins.versionupdater.utils.ViewOutputFormatEnum;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.WinHttpClients;
import org.apache.maven.plugin.logging.Log;


public class WikiPageManager 
{	
	private static final WikiPageManager instance = new WikiPageManager();
	
	private Log log;
	
	private WikiPageManager()
	{		
        if (!WinHttpClients.isWinAuthAvailable()) {
            log("Integrated Win auth is not supported!!!");
            throw new UpdateProjectMojoException("Integrated Win auth is not supported!!!");
        }
	}
	
	public static WikiPageManager getInstance()
	{
		return instance;
	}
	
	public static void updateWikiEntries(String pVersion,ViewOutputFormatEnum pOutputFormat,ProjectConfiguration pProjectConfiguration) throws Exception
	{
		if (null == pProjectConfiguration)
		{
			return;
		}
		
		WikiAccess.updateWikiEntries(pVersion,pOutputFormat,ConverterFromProjectConfigurationsToWikiList.convertProjectDependencyToItemEntry(pProjectConfiguration));		
	}
	

	
	public static ProjectConfiguration buildProjectConfig(ProjectConfigurations pProjectConfigurations,String pVersion,boolean pIgnoreTypeAndScope) throws Exception
	{
		ProjectConfiguration  projectConfig = null;
		
		if (null != pProjectConfigurations)
		{
			projectConfig = pProjectConfigurations.getProjectConfiguration(pVersion);
			
			projectConfig = buildProjectConfig(projectConfig, pIgnoreTypeAndScope);
		}
		
		return projectConfig;		
	}	
	
	public static ProjectConfiguration buildProjectConfig(ProjectConfiguration  pProjectConfig,boolean pIgnoreTypeAndScope) throws Exception
	{
		ProjectConfiguration  projectConfig = null;
		
		if ((null != pProjectConfig) && (pProjectConfig.isWikiSource()))
		{
			projectConfig = buildProjectConfig(pProjectConfig.getWikiURL(),pIgnoreTypeAndScope);
		}
		else
		{
			projectConfig = pProjectConfig;
		}
		
		return projectConfig;		
	}
	
	
	
	public static ProjectConfiguration buildProjectConfig(String pVersion,boolean pIgnoreTypeAndScope) throws Exception
	{
		return buildProjectConfig(pVersion,null,pIgnoreTypeAndScope);
	}
	
	public static ProjectConfiguration buildProjectConfig(String pVersion,String pCriteria,boolean pIgnoreTypeAndScope) throws Exception
	{	
		ProjectConfiguration  projectConfig = null;
		ItemsList configList = WikiAccess.findEntries(pVersion, pCriteria);
        
        projectConfig = ConverterFromWikiListToProjectConfigurations.convert(pIgnoreTypeAndScope,configList);	
		
		return projectConfig;
	}	
	
	public static ProjectConfiguration buildBOM(String pVersion,String pBomType) throws Exception
	{
		ItemsList entries = WikiAccess.findEntries(pVersion);
		
		ProjectConfiguration projectConfig = ConverterFromWikiListToProjectConfigurations.convert(false,entries,entries.getItemsWithinBOM(pBomType));
		
		return projectConfig;
	}
	
	public static List<String> getWikiList() throws Exception
	{
		List<String> wikiList = new ArrayList<String>();
		
		MetadataDataServices metaDataSrvs = WikiAccess.findMetadataEntries();
		
		for(MetadataEntry entry:metaDataSrvs)
		{
			if (entry.isWikiListEntry())
			{
				wikiList.add(entry.getEntryId());
			}
		}
		
		return wikiList;
	}
	
	
	private static Log getLog() {
		return getInstance().log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
	
	private static void log(Object pMsg)
	{
		if (null == pMsg)
		{
			return;
		}
		
		Log currentLog = getLog();
		
		if (null != currentLog)
		{
			getLog().debug(pMsg.toString());
		}
		else
		{
			System.out.println(pMsg.toString());
		}
	}
	

}
