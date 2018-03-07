package intact.maven.plugins.versionupdater.github.model;

import intact.maven.plugins.versionupdater.utils.ContentViews;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Branch {
	
	private String name;

	@JsonProperty
	@JsonView({ContentViews.Normal.class})
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
