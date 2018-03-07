package intact.maven.plugins.versionupdater.source.model;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetProperty;

@ObjectCreate(pattern = "edmx:Edmx/edmx:DataServices/Schema/EntityType/NavigationProperty")
public class MetadataEntryNavigationProperty
{	
	@SetProperty(pattern = "edmx:Edmx/edmx:DataServices/Schema/EntityType/NavigationProperty",attributeName="Name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
