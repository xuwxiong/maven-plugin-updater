package intact.maven.plugins.versionupdater.source.model;

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetProperty;

@ObjectCreate(pattern = "edmx:Edmx/edmx:DataServices/Schema/EntityType/Property")
public class MetadataEntryProperty
{
	@SetProperty(pattern = "edmx:Edmx/edmx:DataServices/Schema/EntityType/Property",attributeName="Name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
