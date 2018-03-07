package intact.maven.plugins.versionupdater.dependencies.model;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.maven.model.Exclusion;

@ObjectCreate(pattern = "project-configurations/project-configuration/dependencies/dependency/exclusions/exclusion")
public class ProjectDependencyExclusion extends Exclusion {
	
	private static final long serialVersionUID = 4448498260229345404L;
	
	private String id;
	
	@BeanPropertySetter(pattern = "project-configurations/project-configuration/dependencies/dependency/exclusions/exclusion/groupId")
    private String groupId;

	@BeanPropertySetter(pattern = "project-configurations/project-configuration/dependencies/dependency/exclusions/exclusion/artifactId")
    private String artifactId;

	
	public void setArtifactId(String artifactId) {
		super.setArtifactId(artifactId);
	}

	
	public void setGroupId(String groupId) {
		super.setGroupId(groupId);
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	@Override
	public String toString() {
		
		StringBuilder buffer = new StringBuilder("");
		
		String groupId = getGroupId();
		String artifactId = getArtifactId();
		
		buffer.append("<exclusion>\n");
		buffer.append(String.format("<groupId>%s</groupId>\n", groupId));	        
		buffer.append(String.format("<artifactId>%s</artifactId>\n", artifactId));
		buffer.append("</exclusion>\n");		
		
		return buffer.toString();
	}	
}
