package intact.maven.plugins.versionupdater.source.model;
import intact.maven.plugins.versionupdater.utils.ContentViews;
import intact.maven.plugins.versionupdater.utils.Utils;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@ObjectCreate(pattern = "feed/entry/link/m:inline/feed/entry/id")
@JsonInclude(Include.NON_EMPTY)
public class ItemExclusionEntry
{
	private static final String BOMTARGET_TITLE = "BOMTarget";
	
	@BeanPropertySetter(pattern = "feed/entry/link/m:inline/feed/entry/id")	
	private String id;
	
	@JsonView(ContentViews.Bom.class)
	private Metadata __metadata;
	
	//If no Id.
	@JsonView(ContentViews.AddExclusionEntry.class)
    private String groupId;
	
	@JsonView(ContentViews.AddExclusionEntry.class)
    private String artifactId;
	
	@JsonView(ContentViews.AddExclusionEntry.class)
	@JsonProperty(value="CategoryValue")	
	public String getCategory()
	{
		return "exclusion";
	}
	
	public String getKey()
	{
		return Utils.getKey(getGroupId(), getArtifactId(), "", "");
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
		this.__metadata = new Metadata(id);
	}	
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	@JsonInclude(Include.NON_EMPTY)
	static class Metadata
	{
		@JsonView(ContentViews.Bom.class)
		private String uri;

		public Metadata(String uri) {
			super();
			this.uri = uri;
		}
	}
}
