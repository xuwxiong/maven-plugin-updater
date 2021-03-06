package intact.maven.plugins.versionupdater.github.model;

import intact.maven.plugins.versionupdater.utils.ContentViews;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {
	
	private String name;
	private String branches_url;
	
	private Pom pom;

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
	public String getBranches_url() {
		return branches_url;
	}

	public void setBranches_url(String branches_url) {
		this.branches_url = branches_url;
	}

	public Pom getPom() {
		return pom;
	}

	public void setPom(Pom pom) {
		this.pom = pom;
	}
	
}
