package intact.maven.plugins.versionupdater.github.model;

import java.util.ArrayList;
import java.util.List;

import intact.maven.plugins.versionupdater.utils.ContentViews;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pom {
	
	private String name;
	private String path;
	private String sha;
	private String content;
	private String encoding;
	
	private String contentURL;
	
	
	private List<Pom> modules = new ArrayList<Pom>();

	@JsonProperty
	@JsonView({ContentViews.Normal.class})
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty
	@JsonView({ContentViews.Normal.class})
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@JsonProperty
	@JsonView({ContentViews.Normal.class})	
	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	@JsonProperty
	@JsonView({ContentViews.Normal.class})
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@JsonProperty
	@JsonView({ContentViews.Normal.class})
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public void addModule(Pom module) {
		this.modules.add(module);
	}

	public List<Pom> getModules() {
		return modules;
	}

	public String getContentURL() {
		return contentURL;
	}

	public void setContentURL(String contentURL) {
		this.contentURL = contentURL;
	}
}
