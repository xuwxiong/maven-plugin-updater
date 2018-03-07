package intact.maven.plugins.versionupdater.dependencies.model;

import intact.maven.plugins.versionupdater.Pom;
import intact.maven.plugins.versionupdater.utils.ContentViews;
import intact.maven.plugins.versionupdater.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.maven.model.Dependency;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@ObjectCreate(pattern = "project-configurations/project-configuration")
@JsonInclude(Include.NON_EMPTY)
public class ProjectConfiguration implements Iterable<ProjectDependency>, IPrintableSelection, IDelegableDetailView
{
	@BeanPropertySetter(pattern = "project-configurations/project-configuration/name")
	@JsonView(ContentViews.Normal.class)
	private String name;
	
	@BeanPropertySetter(pattern = "project-configurations/project-configuration/description")
	@JsonView(ContentViews.Normal.class)
	private String description;
	
	@BeanPropertySetter(pattern = "project-configurations/project-configuration/default-project-version")
	private String defaultVersion;
	
	@BeanPropertySetter(pattern = "project-configurations/project-configuration/parent")
	private String parentKey;
	
	@BeanPropertySetter(pattern = "project-configurations/project-configuration/wikiURL")
	private String wikiURL;
	
	private ProjectConfiguration parent;
	private Map<String,ProjectDependency> dependencies = new HashMap<String,ProjectDependency>();
	
	private final boolean ignoreTypeAndScope;
	
	public ProjectConfiguration() {
		super();
		this.ignoreTypeAndScope = false;
	}
	
	public ProjectConfiguration(boolean ignoreTypeAndScope) {
		super();
		this.ignoreTypeAndScope = ignoreTypeAndScope;
	}

	public boolean isIgnoreTypeAndScope() {
		return ignoreTypeAndScope;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String pName) {
		name = pName;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefaultVersion() {
		return defaultVersion;
	}

	
	public void setDefaultVersion(String defaultVersion) {
		this.defaultVersion = defaultVersion;
	}

	public String getParentKey() {
		return parentKey;
	}

	
	public void setParentKey(String pParentKey) {
		parentKey = pParentKey;
	}	
	
	public String getWikiURL() {
		return wikiURL;
	}

	public void setWikiURL(String wikiURL) {
		this.wikiURL = wikiURL;
	}

	public ProjectConfiguration getParent() {
		return parent;
	}

	public void setParent(ProjectConfiguration pParent) {
		parent = pParent;
	}
	
	@SetNext
	public void addDependency(ProjectDependency pDependency) 
	{
		if (isIgnoreTypeAndScope())
		{
			dependencies.put(Utils.getKeyIgnoringTypeAnsCope(pDependency), pDependency);
		}
		else
		{
			dependencies.put(Utils.getKey(pDependency), pDependency);	
		}		
	}
	
	private ProjectDependency getDependency(String pKey) {
		
		ProjectDependency dependency = dependencies.get(pKey);
		
		if (null != dependency)
		{
			return dependency;
		}
		
		if (null != parent)
		{
			return parent.getDependency(pKey);
		}
		
		return null;
	}	
	
	public ProjectDependency getDependency(Dependency pDependency) {
		String key = null;

		if (isIgnoreTypeAndScope())
		{
			key = Utils.getKeyIgnoringTypeAnsCope(pDependency);
		}
		else
		{
			key = Utils.getKey(pDependency);	
		}
		
		return getDependency(key);
	}
	
	public ProjectDependency getDependency(Pom pPom) {
		String key = null;
		
		if (isIgnoreTypeAndScope())
		{
			key = Utils.getKey(pPom);
		}
		else
		{
			key = Utils.getKeyPomAsDependency(pPom);	
		}
		
		return getDependency(key);
	}	
	
	public ProjectDependency getDependency(String pGroupId, String pArtifactId,String pType) {
		ProjectDependency dependency = new ProjectDependency();
		dependency.setGroupId(pGroupId);
		dependency.setArtifactId(pArtifactId);
		dependency.setType(pType);
		
		return getDependency(dependency);
	}
	
	@JsonView({ContentViews.Normal.class})
	public List<Dependency> getDependencies() {
		return new ArrayList<Dependency>(dependencies.values());
	}
	
	public void setDependencies(List<ProjectDependency> pDependencies)
	{
		dependencies.clear();
		for(ProjectDependency dependency:pDependencies)
		{
			if (null != dependency)
			{
				dependencies.put(Utils.getKey(dependency), dependency);	
			}			
		}
	}
	
	public String toXML(boolean bIncludeParentDependencies)
	{
		return toXML(null, bIncludeParentDependencies);
	}
	
	public String toXML(String[] pFieldsSelection,boolean bIncludeParentDependencies)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("<project-configuration>\n");
		builder.append("<name>").append(name).append("</name>\n");
		builder.append("<description>").append(description).append("</description>\n");
		if (null != parent)
		{
			builder.append("<parent>").append(parent.name).append("</parent>\n");
		}
		if (null != defaultVersion)
		{
			builder.append("<default-project-version>").append(defaultVersion).append("</default-project-version>\n");
		}
		
		Map<String,Dependency> dependencies = new TreeMap<String,Dependency>();
		
		if (bIncludeParentDependencies)
		{
			allInheritedDependencies(dependencies, this);
		}
		else
		{
			dependencies.putAll(this.dependencies);
		}
		
		builder.append("<dependencies>\n");
		for (Dependency dependency : dependencies.values())
		{
        	if (null != dependency)
        	{
        		if (dependency instanceof ProjectDependency)
        		{
        			builder.append(((ProjectDependency)dependency).toString(pFieldsSelection));
        		}
        		else
        		{
        			builder.append(dependency.toString());
        		}
        			
        	}
		}
		
		builder.append("</dependencies>\n");
		builder.append("</project-configuration>\n");
		return builder.toString();
	}
	
	protected void allInheritedDependencies(Map<String,Dependency> pDependencies, ProjectConfiguration pProjectConfiguration)
	{
		if (null != parent)
		{
			parent.allInheritedDependencies(pDependencies, parent);
		}
		pDependencies.putAll(this.dependencies);
	}

	@Override
	public Iterator<ProjectDependency> iterator() 
	{
		Iterator<ProjectDependency> dependenciesIterator = Collections.<ProjectDependency>emptySet().iterator();
		
		if (null != dependencies)
		{
			Collection<ProjectDependency> values = dependencies.values();
			
			if (null != values)
			{
				dependenciesIterator = values.iterator();
			}
		}
		
		return dependenciesIterator;
	}
	
	public boolean isWikiSource()
	{
		return (null != getWikiURL());
	}

	@Override
	public String toString()	
	{
		return toString(null);
	}

	@Override
	public String toString(String[] pFieldsSelection) 
	{
		return toXML(pFieldsSelection,false);		
	}

	@Override
	public Class<? extends IDetailView> getDetailViewClass() 
	{
		return ProjectDependency.class;
	}
}
