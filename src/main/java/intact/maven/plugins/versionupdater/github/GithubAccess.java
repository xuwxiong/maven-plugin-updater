package intact.maven.plugins.versionupdater.github;

import intact.maven.plugins.versionupdater.UpdateProjectMojoException;
import intact.maven.plugins.versionupdater.github.model.Branch;
import intact.maven.plugins.versionupdater.github.model.Commit;
import intact.maven.plugins.versionupdater.github.model.Pom;
import intact.maven.plugins.versionupdater.github.model.Repository;
import intact.maven.plugins.versionupdater.source.model.ItemEntry;
import intact.maven.plugins.versionupdater.utils.ContentViews;
import intact.maven.plugins.versionupdater.utils.PomUtil;
import intact.maven.plugins.versionupdater.utils.ViewOutputFormatEnum;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.WinHttpClients;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.logging.Log;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GithubAccess 
{	
	private static final String GITHUB_AUTH_FILE = System.getProperty("user.home") + System.getProperty("file.separator")+".github";
	
	private static final String VERSION_UPDATER_PROPERTIES = "/intact/maven/plugins/versionupdater/versionupdater.properties";
	
	private static final String GITHUB_PROPERTY_PATTERN_REPOSITORIES = "github.api.soa.repos";
	private static final String GITHUB_PROPERTY_PATTERN_CONTENT = "github.api.soa.pom";
	private static final String GITHUB_PROPERTY_AUTH = "oauth";
	
	private static final Map<String, Repository> MAP_REPOSITORIES = new HashMap<String, Repository>();
	
	
	private static GithubAccess instance = new GithubAccess();
	
	private static final ObjectMapper jsonMapper = new ObjectMapper();
	
	static
	{
		jsonMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
	}
	
	private static final Map<String, Integer> NO_MAP = new HashMap<String, Integer>();
	
	static
	{
		NO_MAP.put("client-webservice",0);
		NO_MAP.put("client-cif2-webservice",0);
		NO_MAP.put("communication-repository-service-webservice",0);
		NO_MAP.put("communication-service-webservice",0);
		NO_MAP.put("crs-hal-webservice",0);
		NO_MAP.put("data-utility-domain-service-webservice",0);
		NO_MAP.put("data-utility-domaincode-service-webservice",0);
		NO_MAP.put("data-utility-generic-service-webservice",0);
		NO_MAP.put("data-utility-message-service-webservice",0);
		NO_MAP.put("domain-values-service-webservice",0);
		NO_MAP.put("reference-control-service-webservice",0);
		NO_MAP.put("vendor-information-service-webservice",0);		
	}
	
	private Properties props;
	
	private String oAuth;
	
	private Log log;
	
	public static GithubAccess getInstance()
	{
		return instance;
	}
	
	private GithubAccess()
	{		

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
	
	private static List<Object> getRepositories(CloseableHttpClient httpclient,String getRequest) throws ClientProtocolException, IOException
	{
		List<Object> repositories = null;
		
        InputStream responseEntityStream = null;
        CloseableHttpResponse response = null;
    	
        HttpGet httpget = new HttpGet(getRequest);
        httpget.setHeader("Content-Type", "application/json");
        
        try
        {
	        log("Executing request " + httpget.getRequestLine());
	        response = httpclient.execute(httpget);      
	        
	        log("----------------------------------------");
	        log(response.getStatusLine());
	        
	        HttpEntity responseEntity = response.getEntity();
	        
	        responseEntityStream = responseEntity.getContent();
	        
	        repositories= jsonMapper.readerWithView(ContentViews.Normal.class).forType(Repository.class).readValues(responseEntityStream).readAll();
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
		
		return repositories;
	}
	
	private static Branch getBranch(CloseableHttpClient httpclient,String getRequest) throws ClientProtocolException, IOException
	{
		Branch branch = null;
		
        InputStream responseEntityStream = null;
        CloseableHttpResponse response = null;
    	
        HttpGet httpget = new HttpGet(getRequest);
        httpget.setHeader("Content-Type", "application/json");
        
        try
        {
	        log("Executing request " + httpget.getRequestLine());
	        response = httpclient.execute(httpget);      
	        
	        log("----------------------------------------");
	        log(response.getStatusLine());
	        
	        HttpEntity responseEntity = response.getEntity();
	        
	        responseEntityStream = responseEntity.getContent();
	        
	        branch= jsonMapper.readerWithView(ContentViews.Normal.class).forType(Branch.class).readValue(responseEntityStream);
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
		
		return branch;
	}
	
	private static Pom buildPom(String pBranch, String pRepoName) throws Exception {
		
		CloseableHttpClient httpclient = null;
		Pom pom = null;
				
        // There is no need to provide user credentials
        // HttpClient will attempt to access current user security context through
        // Windows platform specific methods via JNI.
        try 
        {
        	String getRequest = getURLForPomContent(pRepoName, null, pBranch);
        	
        	if (null == getRequest)
        	{
        		return pom;
        	}
        	
        	httpclient = WinHttpClients.createDefault();
            
        	pom= getPom(httpclient, getRequest);
        	
        	pom.setContentURL(getRequest);
        	
        	
        	List<String> modules = PomUtil.getPomModules(pRepoName,pom.getContent(), true);
        	Pom pomModule = null;
        	
        	for(String module:modules)
        	{
        		getRequest = getURLForPomContent(pRepoName, module, pBranch);
        		pomModule = getPom(httpclient, getRequest);
        		pomModule.setContentURL(getRequest);
        		
        		pom.addModule(pomModule);
        	}

        } 
        finally 
        {
        	if (null != httpclient)
        	{
        		httpclient.close();            		
        	}
        }		
		
		return pom;
	}	
	
	private static Pom getPom(CloseableHttpClient httpclient,String getRequest) throws ClientProtocolException, IOException
	{
		Pom pom = null;
		
        InputStream responseEntityStream = null;
        CloseableHttpResponse response = null;
    	
        HttpGet httpget = new HttpGet(getRequest);
        httpget.setHeader("Content-Type", "application/json");
        
        try
        {
	        log("Executing request " + httpget.getRequestLine());
	        response = httpclient.execute(httpget);      
	        
	        log("----------------------------------------");
	        log(response.getStatusLine());
	        
	        HttpEntity responseEntity = response.getEntity();
	        
	        responseEntityStream = responseEntity.getContent();
	        
	        pom= jsonMapper.readerWithView(ContentViews.Normal.class).forType(Pom.class).readValue(responseEntityStream);
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
		
		return pom;
	}	
	
	private static void updatePom(CloseableHttpClient httpclient,Commit commit,Pom pPom) throws ClientProtocolException, IOException
	{
        InputStream responseEntityStream = null;
        CloseableHttpResponse response = null;
        
        String requestURL = pPom.getContentURL();
        
        requestURL = requestURL.substring(0, requestURL.indexOf("?"));
    	
        HttpPut httpPutt = new HttpPut(requestURL);
        httpPutt.setHeader("Content-Type", "application/json");
        httpPutt.setHeader("Authorization", "Bearer "+getGithubAuthorisation());
        
        try
        {
	        log("Executing request " + httpPutt.getRequestLine());
	        
	        String jsonFormat = jsonMapper.writerWithView(ContentViews.Normal.class).forType(Commit.class).writeValueAsString(commit);
	        
	        StringEntity  requestBody = new StringEntity(jsonFormat,Charset.forName("UTF-8"));    	
	   	
	    	httpPutt.setEntity(requestBody);             

	        log("Executing request " + httpPutt.getRequestLine());
	        
	        response = httpclient.execute(httpPutt);      
	        
	        log("----------------------------------------");
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
	
	
	
	public static List<String> getProjectsOfGivenBranch(String pBranch) throws Exception
	{
		List<String> reposNames = new ArrayList<String>();
		CloseableHttpClient httpclient = null;
		Branch branch = null;
				
        // There is no need to provide user credentials
        // HttpClient will attempt to access current user security context through
        // Windows platform specific methods via JNI.
        try 
        {
        	String urlForRepositories= getURLForRepositories();
        	
        	if (null == urlForRepositories)
        	{
        		return reposNames;
        	}
        	
        	httpclient = WinHttpClients.createDefault();
            
            List<Object> repositories= getRepositories(httpclient, urlForRepositories);
            String repoName = null;
            int counter = 1;
            
            
            Repository currentRepository = null;
            Pom pom = null;
            String branchURL = null;
            
            while(!repositories.isEmpty())
            {
                for(Object repository:repositories)
                {
                	currentRepository = (Repository)repository;
                	repoName = currentRepository.getName();
                	
                	if (repoName.endsWith("-webservice"))
                	{
                		branchURL = currentRepository.getBranches_url();
                		branchURL = branchURL.replace("{/branch}", "/"+pBranch+"?access_token="+getGithubAuthorisation());
                		branch = getBranch(httpclient,branchURL);
                		
                		if (pBranch.equalsIgnoreCase(branch.getName()))
                		{
                			pom = buildPom(pBranch,repoName);
                			currentRepository.setPom(pom);
                			MAP_REPOSITORIES.put(repoName,currentRepository);
                			
                			reposNames.add(repoName);
                		}
                	}
                }
                
                repositories= getRepositories(httpclient, urlForRepositories+"&page="+String.valueOf((++counter)));
            }
            
            Collections.sort(reposNames);

        } 
        finally 
        {
        	if (null != httpclient)
        	{
        		httpclient.close();            		
        	}
        }
        
        return reposNames;
	}	
	

	private static Log getLog() {
		return getInstance().log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
	
	private void loadProperties()
	{
		props = new Properties();
		try {
			props.load(this.getClass().getResourceAsStream(VERSION_UPDATER_PROPERTIES));
			
		} catch (IOException e) {
			throw new UpdateProjectMojoException("Unable to load Updater properties.");
		}
	}
	
	private static String getGithubAuthorisation()
	{
		return getInstance().getAuthorisation();
	}
	
	private String getAuthorisation()
	{
		if (StringUtils.isNotBlank(oAuth))
		{
			return oAuth;
		}
		
		Properties authProps = new Properties();
		try {
			
			InputStream input = new FileInputStream(GITHUB_AUTH_FILE);
			
			authProps.load(input);
			
			oAuth = authProps.getProperty(GITHUB_PROPERTY_AUTH);
			
		} catch (IOException e) {
			throw new UpdateProjectMojoException("Unable to load OAuth Property.");
		}
		
		return oAuth;
	}
	
	private String getRepositoriesListURL()
	{
		String reposRootUrl = null;
		
		if (null == props)
		{
			loadProperties();
		}
	
		reposRootUrl = props.getProperty(GITHUB_PROPERTY_PATTERN_REPOSITORIES);
		
		return reposRootUrl;
	}
	
	private String getPomContentURL()
	{
		String pomContentUrl = null;
		
		if (null == props)
		{
			loadProperties();
		}
	
		pomContentUrl = props.getProperty(GITHUB_PROPERTY_PATTERN_CONTENT);
		
		return pomContentUrl;
	}
	
	private static String getURLForPomContent(String pRepoName,String pModuleName,String pBranch)
	{
		String moduleName = "";
		if (!StringUtils.isBlank(pModuleName))
		{
			moduleName = pModuleName + "/";
		}
		
		return String.format(getInstance().getPomContentURL(), pRepoName,moduleName,pBranch,getInstance().getAuthorisation());
	}	
	
	private static String getURLForRepositories()
	{
		return String.format(getInstance().getRepositoriesListURL(), getInstance().getAuthorisation());
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
	
	private static void migrateBCOVersion(CloseableHttpClient httpclient,String branch,String pArtifactId,Pom pPom) throws Exception
	{
		intact.maven.plugins.versionupdater.Pom effectivePom = PomUtil.mappEffectivePom(pArtifactId,pPom); 
		
		Commit commit = new Commit();
		commit.setBranch(branch);
		commit.setSha(pPom.getSha());
		
				
		Model model = effectivePom.getModel();
		
		String currentVersion = model.getVersion();
		String newVersion = "6.6.X"+currentVersion.substring(5);
		model.addProperty("bom-soa-version", "6.6.X.0-SNAPSHOT");
		
		model.setVersion(newVersion);
		
		effectivePom.save();
		
		PomUtil.buildCommit(commit, effectivePom);
		
		updatePom(httpclient, commit,pPom);
		
		Model subModel = null;
		
		List<Pom> subPoms = pPom.getModules();
		
		final Map<String, Pom> mapPoms = new HashMap<String, Pom>();
		
		for(Pom currentSubPom:subPoms)
		{
			mapPoms.put(PomUtil.extractArtifactId(currentSubPom), currentSubPom);
		}
		
		Pom pomToCommit = null;
		
		for(intact.maven.plugins.versionupdater.Pom effectiveSubPom : effectivePom.getSubPoms())
		{
			subModel = effectiveSubPom.getModel();
			pomToCommit = mapPoms.get(subModel.getArtifactId());
			
			subModel.getParent().setVersion(newVersion);
			
			effectiveSubPom.save();
			
			commit = new Commit();
			commit.setBranch(branch);
			commit.setSha(pomToCommit.getSha());
			
			PomUtil.buildCommit(commit, effectiveSubPom);
			
			updatePom(httpclient, commit,pomToCommit);
		}
		
		
		
	}
	
	public static void updateProjects(String branch) throws Exception
	{
		CloseableHttpClient httpclient = null;
				
        // There is no need to provide user credentials
        // HttpClient will attempt to access current user security context through
        // Windows platform specific methods via JNI.
        try 
        {
        	
        	httpclient = WinHttpClients.createDefault();
        	
    		Pom currentProjectPom = null;
    		
    		for(String project:MAP_REPOSITORIES.keySet())
    		{
				if (null == NO_MAP.get(project))
				{
	    			currentProjectPom = MAP_REPOSITORIES.get(project).getPom();
	    			
	    			migrateBCOVersion(httpclient,branch,project,currentProjectPom);
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
	
	public static void main(String[] args) throws Exception 
	{
//		List<String> projects = getProjectsOfGivenBranch("mig66-temp");
		
		List<String> projects = getProjectsOfGivenBranch("dev");
		
		updateProjects("dev");
		
		System.out.println("done");	
		
	}
	
}
