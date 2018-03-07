package intact.maven.plugins.versionupdater.source.model;
import intact.maven.plugins.versionupdater.GAVHolder;
import intact.maven.plugins.versionupdater.dependencies.model.IDelegableDetailView;
import intact.maven.plugins.versionupdater.dependencies.model.IDetailView;
import intact.maven.plugins.versionupdater.utils.ContentViews;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.commons.digester3.annotations.rules.SetProperty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_EMPTY)
public class ItemEntry implements GAVHolder, IDelegableDetailView
{
	@BeanPropertySetter(pattern = "feed/entry/id")
	private String id;
	
	@BeanPropertySetter(pattern = "feed/entry/title")
	private String title;
	
	@BeanPropertySetter(pattern = "feed/entry/updated")
	private String updated;
	
	@BeanPropertySetter(pattern = "feed/entry/author/name")
	private String author;

	@SetProperty(pattern = "feed/entry",attributeName="m:etag")
	private String etag;
		
	private ItemContent content;

	public String getId() {
		return id;
	}

	
	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getUpdated() {
		return updated;
	}

	
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		this.etag = etag;
	}

	public ItemContent getContent() {
		return content;
	}

	@SetNext
	public void setContent(ItemContent content) {
		this.content = content;
	}

	@Override
	public String getGroupId() {
		return (null != content) ? content.getGroupId():null;
	}

	@Override
	public String getArtifactId() {
		return (null != content) ? content.getArtifactId():null;
	}
	
	public String getType() {
		return (null != content) ? content.getType():null;
	}
	
	public String getScope() {
		return (null != content) ? content.getScope():null;
	}

	@Override
	@JsonView({ContentViews.Release.class,ContentViews.Bom.class})
	@JsonProperty(value="VersionId")
	public String getVersion() {
		return (null != content) ? content.getVersion():null;
	}
	
	@JsonView(ContentViews.Release.class)
	@JsonProperty(value="VersionRelease")
	public String getVersionRelease() {
		return (null != content) ? content.getVersionRelease():null;
	}

	@JsonView(ContentViews.Release.class)
	@JsonProperty(value="ToRelease")
	public boolean getToRelease() {
		return (null != content) ? content.isToRelease():false;
	}

	@JsonView(ContentViews.BomRelease.class)
	@JsonProperty(value="IncludedInBOMRelease")
	public String getIncludedInBOMRelease() {
		return (null != content) ? content.getIncludedInBOMRelease():null;
	}

	@Override
	public Class<? extends IDetailView> getDetailViewClass() 
	{
		return ItemProperties.class;
	}
}
