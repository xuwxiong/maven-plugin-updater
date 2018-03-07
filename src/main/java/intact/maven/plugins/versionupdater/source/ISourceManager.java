package intact.maven.plugins.versionupdater.source;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfigurations;
import intact.maven.plugins.versionupdater.utils.ViewOutputFormatEnum;

import java.util.List;


public interface ISourceManager 
{	
	void updateSourceEntries(String pVersion,ViewOutputFormatEnum pOutputFormat,ProjectConfiguration pProjectConfiguration) throws Exception;
	
	ProjectConfiguration buildProjectConfig(ProjectConfigurations pProjectConfigurations,String pVersion) throws Exception;	
	
	ProjectConfiguration buildProjectConfig(ProjectConfiguration  pProjectConfig) throws Exception;
	
	ProjectConfiguration buildProjectConfig(String pVersion) throws Exception;
	
	ProjectConfiguration buildProjectConfig(String pVersion,List<String> pListArtifacts) throws Exception;
	
	ProjectConfiguration buildProjectConfig(String pVersion,String pCriteria) throws Exception;
	
	ProjectConfiguration buildBOM(String pVersion,String pBomType) throws Exception;
	
	List<String> getSourceList() throws Exception;
}
