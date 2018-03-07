package intact.maven.plugins.versionupdater.source.model;

import intact.maven.plugins.versionupdater.GAVHolder;
import intact.maven.plugins.versionupdater.dependencies.model.IDelegableDetailView;
import intact.maven.plugins.versionupdater.dependencies.model.IDetailView;

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;

@ObjectCreate(pattern = "feed/entry/content")
public class ItemContent implements GAVHolder, IDelegableDetailView
{
	private ItemProperties properties;

	public ItemProperties getProperties() {
		return properties;
	}
	
	@SetNext
	public void setProperties(ItemProperties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return (null != properties) ? properties.toString():"";
	}

	@Override
	public String getGroupId() {
		return (null != properties) ? properties.getGroupId():null;
	}

	@Override
	public String getArtifactId() {
		return (null != properties) ? properties.getArtifactId():null;
	}
	
	public String getType() {
		return (null != properties) ? properties.getType():null;
	}
	
	public String getScope() {
		return (null != properties) ? properties.getScope():null;
	}

	@Override
	public String getVersion() {
		return (null != properties) ? properties.getVersion():null;
	}
	
	public String getVersionRelease() {
		return (null != properties) ? properties.getVersionRelease():null;
	}
	
	public boolean isToRelease() {
		return (null != properties) ? properties.isToRelease():false;
	}
	
	public String getIncludedInBOMRelease() {
		return (null != properties) ? properties.getIncludedInBOMRelease():null;
	}

	@Override
	public Class<? extends IDetailView> getDetailViewClass() 
	{
		return ItemProperties.class;
	}
}
