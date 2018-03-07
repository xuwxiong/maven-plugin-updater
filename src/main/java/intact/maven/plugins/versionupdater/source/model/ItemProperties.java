package intact.maven.plugins.versionupdater.source.model;

import intact.maven.plugins.versionupdater.dependencies.model.IDetailView;
import intact.maven.plugins.versionupdater.utils.ContentViews;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@ObjectCreate(pattern = "feed/entry/content/m:properties")
@JsonInclude(Include.NON_EMPTY)
public class ItemProperties implements IDetailView
{
	@BeanPropertySetter(pattern="feed/entry/content/m:properties/d:GroupId")
	@JsonView({ContentViews.Bom.class,ContentViews.BomRelease.class,ContentViews.Release.class})
	@JsonProperty(value="GroupId")
	private String groupId;
	
	@BeanPropertySetter(pattern="feed/entry/content/m:properties/d:ArtifactId")
	@JsonView({ContentViews.Bom.class,ContentViews.BomRelease.class,ContentViews.Release.class})
	@JsonProperty(value="ArtifactId")
	private String artifactId;
	
	@BeanPropertySetter(pattern="feed/entry/content/m:properties/d:VersionId")
	@JsonView({ContentViews.Bom.class,ContentViews.BomRelease.class,ContentViews.Release.class})
	@JsonProperty(value="VersionId")
	private String version;
	
	@BeanPropertySetter(pattern="feed/entry/content/m:properties/d:DependencyScopeValue")
	@JsonView({ContentViews.Bom.class})
	private String scope;
	
	@BeanPropertySetter(pattern="feed/entry/content/m:properties/d:DependencyTypeValue")
	@JsonView({ContentViews.Bom.class})
	private String type;
	
	@BeanPropertySetter(pattern="feed/entry/content/m:properties/d:VersionRelease")
	@JsonView({ContentViews.BomRelease.class,ContentViews.Release.class})
	@JsonProperty(value="VersionRelease")
	private String versionRelease;
	
	@BeanPropertySetter(pattern="feed/entry/content/m:properties/d:Id")
	private String id;
	
	@BeanPropertySetter(pattern="feed/entry/content/m:properties/d:ToRelease")
	@JsonView({ContentViews.BomRelease.class,ContentViews.Release.class})
	@JsonProperty(value="ToRelease")
	private boolean toRelease;
	
	@BeanPropertySetter(pattern="feed/entry/content/m:properties/d:IncludedInBOMRelease")
	@JsonView(ContentViews.BomRelease.class)
	@JsonProperty(value="IncludedInBOMRelease")	
	private String includedInBOMRelease;

	public String getGroupId() {
		return groupId;
	}
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}	

	public boolean isToRelease() {
		return toRelease;
	}

	public void setToRelease(boolean pToRelease) {
		this.toRelease = pToRelease;
	}
	
	public String getIncludedInBOMRelease() {
		return includedInBOMRelease;
	}

	public void setIncludedInBOMRelease(String includedInBOMRelease) {
		this.includedInBOMRelease = includedInBOMRelease;
	}

	public String getArtifactId() {
		return artifactId;
	}
	
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}	

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersionRelease() {
		return versionRelease;
	}

	public void setVersionRelease(String versionRelease) {
		this.versionRelease = versionRelease;
	}


	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		
		StringBuffer buffer = new StringBuffer("");
		
		String groupId = getGroupId();
		String artifactId = getArtifactId();
		String version = getVersion();
		String versionRelease = getVersionRelease();
		
		buffer.append("<dependency>\n");

		buffer.append(String.format("<groupId>%s</groupId>\n", groupId));
	        
		buffer.append(String.format("<artifactId>%s</artifactId>\n", artifactId));
		buffer.append(String.format("<version>%s</version>\n", version));
		buffer.append(String.format("<release>%s</release>\n", (StringUtils.isNotBlank(versionRelease)  ? versionRelease:"")));
		buffer.append(String.format("<toRelease>%s</toRelease>\n", toRelease));
		buffer.append("</dependency>\n");		
		
		return buffer.toString();
	}
	
	
}
