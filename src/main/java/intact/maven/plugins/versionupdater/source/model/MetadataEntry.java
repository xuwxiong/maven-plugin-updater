package intact.maven.plugins.versionupdater.source.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.commons.digester3.annotations.rules.SetProperty;
import org.apache.commons.lang.StringUtils;

@ObjectCreate(pattern = "edmx:Edmx/edmx:DataServices/Schema/EntityType")
public class MetadataEntry
{
	private static final String NAME_GROUPID 		= "GroupId";
	private static final String NAME_ARTIFACTID 	= "ArtifactId";	
	private static final String NAME_VERSION 		= "Version";
	
	private static final String NAME_SUFFIX_ITEM 	= "Item";
	
	@SetProperty(pattern = "edmx:Edmx/edmx:DataServices/Schema/EntityType",attributeName="Name")
	private String name;
	
	private Map<String,MetadataEntryProperty> properties = new HashMap<String,MetadataEntryProperty>();
	private Map<String,MetadataEntryNavigationProperty> navigationProperties = new HashMap<String,MetadataEntryNavigationProperty>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@SetNext
	public void addProperty(MetadataEntryProperty pEntryProperty) {
		properties.put(pEntryProperty.getName(), pEntryProperty);
	}
	
	@SetNext
	public void addNavigationProperty(MetadataEntryNavigationProperty pEntryProperty) {
		navigationProperties.put(pEntryProperty.getName(), pEntryProperty);
	}
	
	public boolean isWikiListEntry()
	{
		boolean wikiListFlag = false;
		
		wikiListFlag = (null != properties.get(NAME_GROUPID)) && (null != properties.get(NAME_ARTIFACTID)) && (null != properties.get(NAME_VERSION));
		
		return wikiListFlag;
	}
	
	public boolean isPropertyExist(String pPropertyName)
	{
		return (null != properties.get(pPropertyName)) || (null != navigationProperties.get(pPropertyName));
	}
	
	public String getEntryId()
	{
		String currentName = getName();
		
		String entryId = currentName;
		
		if (StringUtils.isNotBlank(currentName))
		{
			currentName = currentName.trim();
			
			if (currentName.endsWith(NAME_SUFFIX_ITEM))
			{
				int indexSuffix = currentName.lastIndexOf(NAME_SUFFIX_ITEM);
				
				entryId = currentName.substring(0, indexSuffix);
			}
		}
		
		return entryId;
	}
	
	public String[] getExistingFields(String[] pAllFields)
	{
		String[] resultArray = null;
		String extractedField = null;
		MetadataEntryProperty currentProperty = null;
		MetadataEntryNavigationProperty currentNavigationProperty = null;
		int nbElements = 0;
		List<String> fieldsList = new ArrayList<String>();
		
		for(String fieldName:pAllFields)
		{
			currentProperty = properties.get(fieldName);
			extractedField = null;
			
			if (null != currentProperty)
			{
				extractedField = currentProperty.getName();
			}
			else
			{
				currentNavigationProperty = navigationProperties.get(fieldName);
				
				if (null != currentNavigationProperty)
				{
					extractedField = currentNavigationProperty.getName();
				}				
			}
			
			if (null!= extractedField)
			{
				fieldsList.add(extractedField);
			}
		}
		
		if (!fieldsList.isEmpty())
		{
			nbElements = fieldsList.size();
			resultArray = new String[nbElements];
			fieldsList.toArray(resultArray);
		}
		
		
		return resultArray;
	}
}
