package intact.maven.plugins.versionupdater.utils;

import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectDependency;
import intact.maven.plugins.versionupdater.source.model.ItemContent;
import intact.maven.plugins.versionupdater.source.model.ItemExclusionEntry;
import intact.maven.plugins.versionupdater.source.model.ItemExtendedEntry;
import intact.maven.plugins.versionupdater.source.model.ItemProperties;
import intact.maven.plugins.versionupdater.source.model.ItemsList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.maven.model.Exclusion;

public class ConverterFromProjectConfigurationsToWikiList 
{
	public static ItemExtendedEntry convertProjectDependencyToItemEntry(ProjectDependency pProjectDependency)
	{
		ItemExtendedEntry itemEntry = null;
		
		if (null != pProjectDependency)
		{			
			String id = pProjectDependency.getId();
			
			if (StringUtils.isEmpty(id))
			{
				id = String.valueOf(RandomUtils.nextInt());
			}
			
			String groupId = pProjectDependency.getGroupId();
			String artifactId = pProjectDependency.getArtifactId();
			String version = pProjectDependency.getVersion();
			String scope = pProjectDependency.getScope();
			String dependencyType = pProjectDependency.getType();
			String versionRelease = pProjectDependency.getRelease();
			boolean toReleaseValue = pProjectDependency.isToRelease();
			String includedInBOMRelease = pProjectDependency.getIncludedInBOMRelease();
			
			itemEntry = new ItemExtendedEntry();
			
			itemEntry.setId(id);
			
			ItemContent entryContent =  new ItemContent();
			
			ItemProperties properties = new ItemProperties();
			
			properties.setGroupId(groupId);
			properties.setArtifactId(artifactId);
			properties.setVersion(version);
			properties.setType(dependencyType);
			properties.setScope(scope);
			properties.setVersionRelease(versionRelease);
			properties.setToRelease(toReleaseValue);
			properties.setIncludedInBOMRelease(includedInBOMRelease);
			
			entryContent.setProperties(properties);
			itemEntry.setContent(entryContent);
			
			ItemExclusionEntry exclusionEntry = null;
			
			for(Exclusion exclusion:pProjectDependency.getExclusions())
			{
				exclusionEntry = new ItemExclusionEntry();
				exclusionEntry.setArtifactId(exclusion.getArtifactId());
				exclusionEntry.setGroupId(exclusion.getGroupId());
				
				itemEntry.addExclusion(exclusionEntry);
			}
		}
		
		return itemEntry;
	}
	
	public static ItemsList convertProjectDependencyToItemEntry(ProjectConfiguration  pProjectConfig)
	{
		ItemsList itemList = null;
		
		if (null != pProjectConfig)
		{		
			itemList = new ItemsList();
			
			for(ProjectDependency projectDependency:pProjectConfig)
			{
				itemList.addEntry(convertProjectDependencyToItemEntry(projectDependency));
			}
		}
		
		return itemList;
	}	
}
