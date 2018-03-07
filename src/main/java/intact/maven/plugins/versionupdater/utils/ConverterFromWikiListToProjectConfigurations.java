package intact.maven.plugins.versionupdater.utils;

import intact.maven.plugins.versionupdater.dependencies.ProjectConfigurationsException;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectDependency;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectDependencyExclusion;
import intact.maven.plugins.versionupdater.source.model.ItemContent;
import intact.maven.plugins.versionupdater.source.model.ItemEntry;
import intact.maven.plugins.versionupdater.source.model.ItemExclusionEntry;
import intact.maven.plugins.versionupdater.source.model.ItemExtendedEntry;
import intact.maven.plugins.versionupdater.source.model.ItemProperties;
import intact.maven.plugins.versionupdater.source.model.ItemsList;

import java.util.Collection;

public class ConverterFromWikiListToProjectConfigurations 
{

	public static ProjectConfiguration convert(String pConfigName,boolean pIgnoreTypeAndScope,ItemsList pRefList,ItemsList pItemsList) throws ProjectConfigurationsException
	{
		ProjectConfiguration  projectConfig = null;
		
		if (null != pItemsList)
		{
			projectConfig = new ProjectConfiguration(pIgnoreTypeAndScope);
			
			projectConfig.setName(pConfigName);
			
			ProjectDependency projectDependency = null;
			
			for(ItemEntry entry:pItemsList)
			{
				projectDependency = convertItemEntryToProjectDependency(pRefList,entry);
				
				projectConfig.addDependency(projectDependency);
			}			
		}		
		
		return projectConfig;
	}
	
	public static ProjectConfiguration convert(boolean pIgnoreTypeAndScope,ItemsList pRefList,ItemsList pItemsList) throws ProjectConfigurationsException
	{	
		ProjectConfiguration  projectConfig = null;
		
		if ((null != pItemsList) && (!pItemsList.isEmpty()))
		{
			projectConfig = convert(pItemsList.getId(),pIgnoreTypeAndScope, pRefList,pItemsList);
		}		
		
		return projectConfig;
	}	
	
	public static ProjectConfiguration convert(boolean pIgnoreTypeAndScope,ItemsList pItemsList) throws ProjectConfigurationsException
	{			
		return convert(pIgnoreTypeAndScope,pItemsList, pItemsList);
	}	
	
	public static ProjectDependency convertItemEntryToProjectDependency(ItemsList pRefList,ItemEntry pItemEntry)
	{
		ProjectDependency projectDependency = null;
		
		if (null != pItemEntry)
		{
			ItemContent entryContent =  pItemEntry.getContent();
			
			ItemProperties properties = (null != entryContent) ? entryContent.getProperties():null;
			
			if (null != properties)
			{
				projectDependency = new ProjectDependency();
				
				String groupId = properties.getGroupId();
				String artifactId = properties.getArtifactId();
				String version = properties.getVersion();
				String scope = properties.getScope();
				String dependencyType = properties.getType();
				String versionRelease = properties.getVersionRelease();
				boolean toReleaseValue = properties.isToRelease();
				String includedInBOMRelease = properties.getIncludedInBOMRelease();
				
				projectDependency.setGroupId(groupId);
				projectDependency.setArtifactId(artifactId);
				projectDependency.setVersion(version);
				projectDependency.setScope(scope);
				projectDependency.setType(dependencyType);
				projectDependency.setRelease(versionRelease);
				projectDependency.setToRelease(toReleaseValue);
				projectDependency.setIncludedInBOMRelease(includedInBOMRelease);
								
				if (pItemEntry instanceof ItemExtendedEntry)
				{				
					ItemExtendedEntry extendedItemEntry = (ItemExtendedEntry) pItemEntry;
					
					Collection<ItemExclusionEntry> exclusions = extendedItemEntry.getExclusions();
					
					ProjectDependencyExclusion projectDependencyExclusion = null;
										
					for(ItemExclusionEntry exclusionEntry:exclusions)
					{						
						projectDependencyExclusion = convertItemEntryToProjectDependencyExclusion(exclusionEntry);
						
						projectDependency.addExclusion(projectDependencyExclusion);
					}
				}
			}
		}
		
		return projectDependency;
	}
	
	public static ProjectDependencyExclusion convertItemEntryToProjectDependencyExclusion(ItemExclusionEntry pItemEntry)
	{
		ProjectDependencyExclusion projectDependencyExclusion = new ProjectDependencyExclusion();
				
		String id = pItemEntry.getId();
		String groupId = pItemEntry.getGroupId();
		String artifactId = pItemEntry.getArtifactId();
				
		projectDependencyExclusion.setId(id);
		projectDependencyExclusion.setGroupId(groupId);
		projectDependencyExclusion.setArtifactId(artifactId);
		
		return projectDependencyExclusion;
	}	
}
