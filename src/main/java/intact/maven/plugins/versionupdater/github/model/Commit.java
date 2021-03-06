package intact.maven.plugins.versionupdater.github.model;

import intact.maven.plugins.versionupdater.utils.ContentViews;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {
	
	private String message = "DOM6.6 Migration";
	private String sha;
	private String content;
	private String branch;
	
	private Commiter commiter = new Commiter();
	
	@JsonProperty(value="commiter")
	@JsonView({ContentViews.Normal.class})
	public Commiter getCommiter() {
		return commiter;
	}

	@JsonProperty
	@JsonView({ContentViews.Normal.class})
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
	
	
}
