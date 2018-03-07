package intact.maven.plugins.versionupdater.dependencies.model;

import intact.maven.plugins.versionupdater.GAVHolder;
import intact.maven.plugins.versionupdater.utils.ContentViews;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@ObjectCreate(pattern = "project-configurations/project-configuration/dependencies/dependency")
@JsonInclude(Include.NON_NULL)
public class ProjectDependency extends Dependency implements GAVHolder, IPrintableSelection,IDetailView
{
	private static final long serialVersionUID = 4448498260229345404L;
	
	private String id;
	
	@BeanPropertySetter(pattern = "project-configurations/project-configuration/dependencies/dependency/groupId")
	@JsonView({ContentViews.Bom.class,ContentViews.BomRelease.class,ContentViews.Release.class})
    private String groupId;

	@BeanPropertySetter(pattern = "project-configurations/project-configuration/dependencies/dependency/artifactId")
	@JsonView({ContentViews.Bom.class,ContentViews.BomRelease.class,ContentViews.Release.class})
    private String artifactId;

	@BeanPropertySetter(pattern = "project-configurations/project-configuration/dependencies/dependency/version")
	@JsonView({ContentViews.Bom.class,ContentViews.BomRelease.class,ContentViews.Release.class})
    private String version;

	@BeanPropertySetter(pattern = "project-configurations/project-configuration/dependencies/dependency/type")
	@JsonView({ContentViews.Bom.class})
    private String type;

	@BeanPropertySetter(pattern = "project-configurations/project-configuration/dependencies/dependency/scope")
	@JsonView({ContentViews.Bom.class})
    private String scope;
	
	@BeanPropertySetter(pattern = "project-configurations/project-configuration/dependencies/dependency/release")
	@JsonView({ContentViews.BomRelease.class,ContentViews.Release.class})
	@JsonProperty(defaultValue="")
    private String release;
	
	@BeanPropertySetter(pattern = "project-configurations/project-configuration/dependencies/dependency/toRelease")
	@JsonView({ContentViews.BomRelease.class,ContentViews.Release.class})
	@JsonProperty(defaultValue="false")
    private boolean toRelease;	
	
	@BeanPropertySetter(pattern = "project-configurations/project-configuration/dependencies/dependency/includedInBOMRelease")
	@JsonView(ContentViews.BomRelease.class)
	@JsonProperty(defaultValue="false")
    private String includedInBOMRelease;
	
	public ProjectDependency() {super();}
	
	
	public ProjectDependency(Dependency dependency) throws IllegalAccessException, InvocationTargetException 
	{
		BeanUtils.copyProperties(this, dependency.clone());
	}

	public void setArtifactId(String artifactId) {
		super.setArtifactId(artifactId);
	}
	
	public void setGroupId(String groupId) {
		super.setGroupId(groupId);
	}

	public void setVersion(String version) {
		super.setVersion(version);
	}	

	public void setType(String type) {
		super.setType(type);
	}

	public void setScope(String scope) {
		super.setScope(scope);
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String pRelease) 
	{		
		this.release = pRelease;
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

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	
	
	
	@Override
	@JsonIgnore
	public void setOptional(boolean optional) {
		super.setOptional(optional);
	}

	@SetNext
    public void addExclusion( Exclusion exclusion )
    {
        super.addExclusion(exclusion);
    } 
	
	public boolean hasExclusions()
	{
		return CollectionUtils.isNotEmpty(getExclusions());
	}

	@Override
	public String toString()	
	{
		return toString(null);
	}
	
	private void appendIfSelected(StringBuilder pBuffer,String pFieldsSelectionStr,String pFieldName,String pFieldValue)
	{
		boolean toPrintFlag = (null == pFieldsSelectionStr) || (pFieldsSelectionStr.contains(pFieldName.toUpperCase()));
		
		if (toPrintFlag)
		{
			String elementToAppend = StringUtils.isNotBlank(pFieldValue)  ? String.format("<%s>%s</%s>\n",pFieldName, pFieldValue,pFieldName):"";
			
			pBuffer.append(elementToAppend);			
		}
	}

	@Override
	public String toString(String[] pFieldsSelection) 
	{		
		StringBuilder buffer = new StringBuilder("");
		
		String groupId = getGroupId();
		String artifactId = getArtifactId();
		String version = getVersion();
		String versionRelease = getRelease();
		boolean toRelease = isToRelease();
		String type = getType();
		String scope = getScope();
		
		String fieldsSelectionStr = null;
		
		if (null != pFieldsSelection)
		{
			fieldsSelectionStr = StringUtils.join(pFieldsSelection," ").toUpperCase();
		}
		
		buffer.append("<dependency>\n");
		
		appendIfSelected(buffer, fieldsSelectionStr, "groupId", groupId);
		appendIfSelected(buffer, fieldsSelectionStr, "artifactId", artifactId);
		appendIfSelected(buffer, fieldsSelectionStr, "version", version);
		appendIfSelected(buffer, fieldsSelectionStr, "release", versionRelease);
		appendIfSelected(buffer, fieldsSelectionStr, "toRelease", String.valueOf(toRelease));
		appendIfSelected(buffer, fieldsSelectionStr, "type", type);
		appendIfSelected(buffer, fieldsSelectionStr, "scope", scope);
		
		if (hasExclusions())
		{
			buffer.append("<exclusions>\n");
			
			for(Exclusion exclusion:getExclusions())
			{
				buffer.append(exclusion);
				buffer.append("\n");
			}
			
			buffer.append("</exclusions>\n");
		}
		
		buffer.append("</dependency>\n");
		
		return buffer.toString();		
	}
}
