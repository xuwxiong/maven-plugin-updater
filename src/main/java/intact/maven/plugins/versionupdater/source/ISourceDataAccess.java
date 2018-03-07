package intact.maven.plugins.versionupdater.source;

import intact.maven.plugins.versionupdater.source.model.ISourceList;
import intact.maven.plugins.versionupdater.utils.ViewOutputFormatEnum;

import java.util.List;

public interface ISourceDataAccess 
{	
	void updateSourceEntries(String pVersion,ViewOutputFormatEnum pOutputFormat,ISourceList pISourceList) throws Exception;
	
	ISourceList findSourceEntries(String pVersion,ISourceList pISourceList) throws Exception;
	
	ISourceList getSelectedItems(String pVersion,List<String> pListArtifacts) throws Exception;
		
	ISourceList findEntries(String pVersion) throws Exception;
	
	ISourceList findEntries(String pVersion,String pCriteria) throws Exception;
	
	String getSelectPattern(String pVersion,String pCriteria) throws Exception;
}
