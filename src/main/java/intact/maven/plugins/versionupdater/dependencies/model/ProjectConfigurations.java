package intact.maven.plugins.versionupdater.dependencies.model;

import intact.maven.plugins.versionupdater.dependencies.ProjectConfigurationsException;
import intact.maven.plugins.versionupdater.utils.ContentViews;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@ObjectCreate(pattern = "project-configurations")
@JsonInclude(Include.NON_EMPTY)
public class ProjectConfigurations implements Iterable<ProjectConfiguration>, IPrintableSelection, IDelegableDetailView
{
	@JsonProperty("projectConfiguration") 
	private Map<String,ProjectConfiguration> projectConfigurations = new HashMap<String,ProjectConfiguration>();
	
	public ProjectConfiguration getProjectConfiguration(String pName) {
		return projectConfigurations.get(pName);
	}

	@SetNext
	public void addProjectConfiguration(ProjectConfiguration pProjectConfiguration) throws ProjectConfigurationsException
	{
		if (projectConfigurations.containsKey(pProjectConfiguration.getName()))
		{
			throw new ProjectConfigurationsException("ProjectConfiguration with name already exists: " + pProjectConfiguration.getName());
		}
		projectConfigurations.put(pProjectConfiguration.getName(), pProjectConfiguration);
	}

	@Override
	public Iterator<ProjectConfiguration> iterator() 
	{
		Iterator<ProjectConfiguration> configsIterator = Collections.<ProjectConfiguration>emptySet().iterator();
		
		if (null != projectConfigurations)
		{
			Collection<ProjectConfiguration> values = projectConfigurations.values();
			
			if (null != values)
			{
				configsIterator = values.iterator();
			}
		}
		
		return configsIterator;
	}
	
	@JsonView({ContentViews.Normal.class})
	public Collection<ProjectConfiguration> getProjectConfigurations()
	{
		return projectConfigurations.values();
	}
	
	public void setProjectConfigurations(Collection<ProjectConfiguration> pConfigs)
	{
		projectConfigurations.clear();
		for(ProjectConfiguration config:pConfigs)
		{
			if (null != config)
			{
				projectConfigurations.put(config.getName(), config);	
			}
			
		}
	}	

	@Override
	public String toString()	
	{
		return toString(null);
	}

	@Override
	public String toString(String[] pFieldsSelection) {

		StringBuffer buffer = new StringBuffer("");
		
		buffer.append("<project-configurations>\n");
		
        for(ProjectConfiguration entry:this)
        {
        	if (null != entry)
        	{
        		buffer.append(entry.toString(pFieldsSelection));	
        	}        	
        }
        
        buffer.append("</project-configurations>");
        
		return buffer.toString();
		
	}

	@Override
	public Class<? extends IDetailView> getDetailViewClass() 
	{
		return ProjectDependency.class;
	}
}
