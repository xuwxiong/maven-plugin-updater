package intact.maven.plugins.versionupdater.source.model;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetProperty;

@ObjectCreate(pattern = "feed/entry/link")
public class SubItem {
	@SetProperty(attributeName="rel", pattern="feed/entry/link")
	private String rel;

	@BeanPropertySetter(pattern="feed/entry/link/m:inline/feed/entry/content/m:properties/d:Value")
	private String value;
	
	@BeanPropertySetter(pattern = "feed/entry/link/m:inline/feed/entry/id")	
	private String id;
	
	@BeanPropertySetter(pattern="feed/entry/link/m:inline/feed/entry/content/m:properties/d:ArtifactId")
    private String artifactId;
	
	@BeanPropertySetter(pattern="feed/entry/link/m:inline/feed/entry/content/m:properties/d:GroupId")
	private String groupId;
	
	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	
}
