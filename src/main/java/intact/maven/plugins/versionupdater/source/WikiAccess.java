package intact.maven.plugins.versionupdater.source;

import intact.maven.plugins.versionupdater.ProjectMojoException;
import intact.maven.plugins.versionupdater.UpdateProjectMojoException;
import intact.maven.plugins.versionupdater.source.model.ItemEntry;
import intact.maven.plugins.versionupdater.source.model.ItemExclusionEntry;
import intact.maven.plugins.versionupdater.source.model.ItemExtendedEntry;
import intact.maven.plugins.versionupdater.source.model.ItemsList;
import intact.maven.plugins.versionupdater.source.model.MetadataDataServices;
import intact.maven.plugins.versionupdater.source.model.MetadataEntry;
import intact.maven.plugins.versionupdater.utils.ContentViews;
import intact.maven.plugins.versionupdater.utils.DigesterUtils;
import intact.maven.plugins.versionupdater.utils.Utils;
import intact.maven.plugins.versionupdater.utils.ViewOutputFormatEnum;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.commons.digester3.Digester;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.WinHttpClients;
import org.apache.maven.plugin.logging.Log;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WikiAccess 
{	
	public static final String CRITERIA_BCO_RELEASE = "IncludedInBOMRelease%20eq%20true";
	
	private static final String VERSION_UPDATER_PROPERTIES = "/intact/maven/plugins/versionupdater/versionupdater.properties";
	
	private static final String PATH_METADATA = "$metadata";
	
	private static final String PROPERTY_WIKI_ROOT = "ROOT";
	private static final String PROPERTY_FIELDS_SELECT = "fields.select";
	private static final String PROPERTY_FIELDS_EXPAND = "fields.expand";
	
	
	private static final String PATTERN_URL_SELECT_WIKI = "?$select=%s&$expand=%s&$filter=%s";
	
	private static final String CRITERIA_TRUE_WIKI = "1%20eq%201";
	
	private static final String SEPARATOR_FIELDS = ",";
	
	
	private static WikiAccess instance = new WikiAccess();
	
	private static final Digester digester = DigesterUtils.getDigester();
	
	private static final ObjectMapper jsonMapper = new ObjectMapper();
	
	static
	{
		jsonMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
	}
	
	private Properties props;
	
	private Log log;
	
	public static WikiAccess getInstance()
	{
		return instance;
	}
	
	private WikiAccess()
	{		
        if (!WinHttpClients.isWinAuthAvailable()) {
            log("Integrated Win auth is not supported!!!");
            throw new UpdateProjectMojoException("Integrated Win auth is not supported!!!");
        }
	}
	
	public static void updateWikiEntriesForRelease(String pVersion,ItemsList pItemsList) throws Exception
	{
		updateWikiEntries(pVersion, ViewOutputFormatEnum.JSON_RELEASE, pItemsList);
	}
	
	
	public static void updateWikiEntries(String pVersion,ViewOutputFormatEnum pOutputFormat,ItemsList pItemsList) throws Exception
	{	
		CloseableHttpClient httpclient = null;		

        try 
        {        	
        	if (null == pItemsList)
        	{
        		return;
        	}
    		
    		String wikiVersion = Utils.normalizeVersion(pVersion);
        	
        	String wikiUrl = getURLForVersion(wikiVersion);
    	
	    	if (null == wikiUrl)
	    	{
	    		return;
	    	}
	    	
	    	ItemsList wikiEntries = findWikiEntries(wikiVersion,pItemsList);
	    	
	    	if ((null == wikiEntries) || (wikiEntries.isEmpty()))
	    	{
	    		return;
	    	}
	    	
	    	httpclient = WinHttpClients.createDefault();
	    	
	    	CloseableHttpResponse response = null;
	    	
	    	
	    	for(ItemEntry wikiEntry:wikiEntries)
	    	{	            
	            try 
	            {
		    		log("----------------------------------------");
		    		response = sendUpdateRequest(httpclient, pOutputFormat, wikiEntry);
		    		log(response.getStatusLine());                
	                
	            } 
	            finally 
	            {

	            	if (null != response)
	            	{
	            		response.close();
	            	}   
	            }
	    	}
        } 
        finally 
        {
        	if (null != httpclient)
        	{
        		httpclient.close();	
        	}            
        }		
	}
	
	private static CloseableHttpResponse sendUpdateRequest(CloseableHttpClient pHttpclient,ViewOutputFormatEnum pOutputFormat,ItemEntry pWikiEntry) throws Exception
	{
    	String wikiIdAsUrl = pWikiEntry.getId();
    	String wikiEtag = pWikiEntry.getEtag();
	
    	String wikiUrlWithId = wikiIdAsUrl;		
    	HttpPost httpPost = new HttpPost(wikiUrlWithId);
        
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("X-HTTP-Method", "MERGE");   
        httpPost.setHeader("If-Match", wikiEtag);
        
        
        String wikiInfoJsonFormat = pOutputFormat.printFormat(pWikiEntry);
        
        StringEntity  requestBody = new StringEntity(wikiInfoJsonFormat,Charset.forName("UTF-8"));    	
   	
    	httpPost.setEntity(requestBody);             

        log("Executing request " + httpPost.getRequestLine());
        
        CloseableHttpResponse response = pHttpclient.execute(httpPost);
        
        return response;		
	}
	
	private static CloseableHttpResponse sendAddExclusionEntryRequest(CloseableHttpClient pHttpclient,ItemExclusionEntry pExclusionEntry,String pVersion) throws Exception
	{
		String wikiVersion = Utils.normalizeVersion(pVersion);
    	
    	String wikiUrl = getURLForVersion(wikiVersion);
	
    	if (null == wikiUrl)
    	{
    		return null;
    	}
    	
    	HttpPost httpPost = new HttpPost(wikiUrl);
        
        httpPost.setHeader("Content-Type", "application/json");
        
        String wikiInfoJsonFormat = ViewOutputFormatEnum.JSON.printJSONFormat(pExclusionEntry,ContentViews.AddExclusionEntry.class);
        
        StringEntity  requestBody = new StringEntity(wikiInfoJsonFormat,Charset.forName("UTF-8"));    	
   	
    	httpPost.setEntity(requestBody);             

        log("Executing request " + httpPost.getRequestLine());
        
        CloseableHttpResponse response = pHttpclient.execute(httpPost);
        
        return response;		
	}
	
	public static ItemsList findWikiEntries(String pVersion,ItemsList pItemsList) throws Exception
	{
		ItemsList resultList = new ItemsList();
		
		ItemsList configList = findEntries(pVersion);
        
        if ((null != configList) && (!configList.isEmpty()))
        {
        	ItemExtendedEntry resultEntry = null;
       	 	for(ItemExtendedEntry pEntry:pItemsList)
       	 	{
       	 		resultEntry = configList.getEntryByKey(Utils.getKey(pEntry));
       	 		
       	 		if (null != resultEntry)
       	 		{
           	 		pEntry.setId(resultEntry.getId());
           	 		pEntry.setEtag(resultEntry.getEtag());
           	 		
       	 			resultList.addEntry(pEntry);
       	 		}
       	 	}
        }
		
		return resultList;
	}
	
	public static ItemsList findEntries(String pVersion) throws Exception
	{
		return findEntries(pVersion, null);
	}
	
	public static ItemsList findEntries(String pVersion,String pCriteria) throws Exception
	{
		ItemsList configList = null;
		CloseableHttpClient httpclient = null;
		
		String selectCriteria = ((null != pCriteria) ? pCriteria:CRITERIA_TRUE_WIKI);
		
		String wikiVersion = Utils.normalizeVersion(pVersion);
				
        // There is no need to provide user credentials
        // HttpClient will attempt to access current user security context through
        // Windows platform specific methods via JNI.
        try 
        {
        	String wikiUrl = getURLForVersion(wikiVersion);
        	
        	if (null == wikiUrl)
        	{
        		return null;
        	}
        	
        	String urlParameters = getSelectPattern(wikiVersion,selectCriteria);
        	
        	String wikiUrlWithParams = wikiUrl+urlParameters;
        	
        	httpclient = WinHttpClients.createDefault();
            InputStream responseEntityStream = null;
        	
            HttpGet httpget = new HttpGet(wikiUrlWithParams);

            log("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget);
            
            try 
            {
                log("----------------------------------------");
                log(response.getStatusLine());
                
                HttpEntity responseEntity = response.getEntity();
                
                responseEntityStream = responseEntity.getContent();
                
                configList = digester.parse( responseEntityStream);
            } 
            finally 
            {
            	if (null != responseEntityStream)
            	{
            		responseEntityStream.close();            		
            	}
            	
            	if (null != response)
            	{
                    response.close(); 
            	}
            }
        } 
        finally 
        {
        	if (null != httpclient)
        	{
        		httpclient.close();            		
        	}
        }
		
		return configList;
	}	
	
	public static String getSelectPattern(String pVersion,String pCriteria) throws Exception 
	{
		String selectWithCriteria = null;
		
		MetadataDataServices metadata = findMetadataEntries();
		
		MetadataEntry versionEntry = metadata.getEntry(pVersion);
		
		if (null != versionEntry)
		{
			String[] allFields = getInstance().getAllSelectableFields();
			String[] fieldsArray = null; 
			String selectFieldsAsStr = "";
			String expandFieldsAsStr = "";
			
			fieldsArray = versionEntry.getExistingFields(allFields);
			
			if (null != fieldsArray)
			{
				selectFieldsAsStr = StringUtils.join(fieldsArray, SEPARATOR_FIELDS);
			}
			
			allFields = getInstance().getAllExpandFields();
			fieldsArray = versionEntry.getExistingFields(allFields);
			
			if (null != fieldsArray)
			{
				expandFieldsAsStr = StringUtils.join(fieldsArray, SEPARATOR_FIELDS);
			}
			
			selectWithCriteria = String.format(PATTERN_URL_SELECT_WIKI, selectFieldsAsStr,expandFieldsAsStr,pCriteria);
		}
		else
		{
			throw new ProjectMojoException(String.format("No wiki entry found for [%s]",pVersion));
		}
		
		return selectWithCriteria;
	}

	public static MetadataDataServices findMetadataEntries() throws Exception
	{
		MetadataDataServices metadataSrvs = null;
		CloseableHttpClient httpclient = null;
				
        // There is no need to provide user credentials
        // HttpClient will attempt to access current user security context through
        // Windows platform specific methods via JNI.
        try 
        {
        	String wikiRootUrl = getInstance().getWikiRootURL();
        	
        	if (null == wikiRootUrl)
        	{
        		return null;
        	}
        	
        	String wikiMetadataUrl = wikiRootUrl+PATH_METADATA;
        	
        	httpclient = WinHttpClients.createDefault();
        	
            HttpGet httpget = new HttpGet(wikiMetadataUrl);

            log("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = null;
            InputStream responseEntityStream = null;            
            
            try 
            {
            	response = httpclient.execute(httpget);
            	
                log("----------------------------------------");
                log(response.getStatusLine());
                
                
                HttpEntity responseEntity = response.getEntity();
                
                responseEntityStream = responseEntity.getContent();
                
                metadataSrvs = digester.parse(responseEntityStream);
                
            } 
            finally 
            {
            	if (null != responseEntityStream)
            	{
            		responseEntityStream.close();            		
            	}
            	
            	if (null != response)
            	{
                    response.close();            		
            	}
            }
        } 
        finally 
        {
        	if (null != httpclient)
        	{
        		httpclient.close();            		
        	}
        }
		
		return metadataSrvs;
	}
	
	private static Log getLog() {
		return getInstance().log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
	
	private String getWikiRootURL()
	{
		String wikiRootUrl = null;
		
		if (null == props)
		{
			loadVersions();
		}
	
		wikiRootUrl = props.getProperty(PROPERTY_WIKI_ROOT);
		
		if (StringUtils.isNotBlank(wikiRootUrl))
		{
			if (!wikiRootUrl.trim().endsWith("/"))
			{
				wikiRootUrl += "/";
			}
		}
		
		return wikiRootUrl;
	}
	
	private String[] getAllListFields(String pPropertyName)
	{
		String[] allSelectableFIelds = null;
		String propertyAllSelectableFieldsValue = null;
		
		if (null == props)
		{
			loadVersions();
		}
	
		propertyAllSelectableFieldsValue = props.getProperty(pPropertyName);
		
		if (StringUtils.isNotBlank(propertyAllSelectableFieldsValue))
		{
			allSelectableFIelds = StringUtils.split(propertyAllSelectableFieldsValue, SEPARATOR_FIELDS);
		}
		
		return allSelectableFIelds;
	}		
	
	private String[] getAllSelectableFields()
	{
		return getAllListFields(PROPERTY_FIELDS_SELECT);
	}	

	private String[] getAllExpandFields()
	{
		return getAllListFields(PROPERTY_FIELDS_EXPAND);
	}		
	
	private void loadVersions()
	{
		props = new Properties();
		try {
			props.load(this.getClass().getResourceAsStream(VERSION_UPDATER_PROPERTIES));
			
		} catch (IOException e) {
			throw new UpdateProjectMojoException("Unable to load Updater properties.");
		}
	}
	
	private static void log(Object pMsg)
	{
		if (null == pMsg)
		{
			return;
		}
		
		Log currentLog = getLog();
		
		if (null != currentLog)
		{
			getLog().debug(pMsg.toString());
		}
		else
		{
			System.out.println(pMsg.toString());
		}
	}
	
	private static String getURLForVersion(String pVersion)
	{
		String urlResult = null;
		String wikiRootUrl = null;
		
		wikiRootUrl = getInstance().getWikiRootURL();
		
		urlResult = wikiRootUrl+pVersion;
		
		return urlResult;
	}
	
	@SuppressWarnings("unused")
	private static void addExclusionEntry()
	{
		
	}
	
	public static void main(String[] args) throws Exception {
//		MetadataDataServices metadata = findMetadataEntries();
//		
//		String versionD13 = "BCO513D11WRTY6";
//		String versionD14 = "BCO62D14";
//		
//		boolean bomTargetFlagD13 = metadata.getEntry(versionD13).isPropertyExist("BOMTarget");
//		boolean bomTargetFlagD14 = metadata.getEntry(versionD14).isPropertyExist("BOMTarget");
//		 
//		
//		System.out.println("If D13 include BOMTarget ["+bomTargetFlagD13+"]");
//		System.out.println("If D14 include BOMTarget ["+bomTargetFlagD14+"]");
		
		ItemsList itemsFromWiki = findEntries("BCO63D15");
		
	}
	
}
