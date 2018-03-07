package intact.maven.plugins.versionupdater;

import intact.maven.plugins.versionupdater.dependencies.model.ProjectConfiguration;
import intact.maven.plugins.versionupdater.dependencies.model.ProjectDependency;
import intact.maven.plugins.versionupdater.utils.Consts;
import intact.maven.plugins.versionupdater.utils.PomUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Scm;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.MojoExecutionException;

public class Pom
{
	private static final Pattern PATTERN = Pattern.compile("(\\$\\{)(.*)(\\})");
	private static final String PATTERN_MODULE = "%s\\%s";
	
	private final Model model;
	private final List<Pom> subPoms = new ArrayList<Pom>();
	private Pom parentPom = null;
	
	private Pom(Parent pParent)
	{
		this.model = new Model();
		
		this.model.setArtifactId(pParent.getArtifactId());
		this.model.setGroupId(pParent.getGroupId());
	}
	
	public Pom(String pArtifactId,String pPomXMlContent,List<String> pPomModulesNames,List<String> pPomModulesContent) throws MojoExecutionException
	{
		try {
			model = read(pArtifactId,pPomXMlContent);
			
			if (null == pPomModulesNames)
			{
				return;
			}
			
			int index=0;
			
			for (String pPomModuleName : pPomModulesNames)
			{
				subPoms.add(new Pom(String.format(PATTERN_MODULE, pArtifactId,pPomModuleName),pPomModulesContent.get(index++),null,null));
			}
			
		} catch (Exception e) {
			throw new MojoExecutionException("Error reading pom content.", e);
		}
	}	
	
	public Pom(File pFolder) throws MojoExecutionException
	{
		try {
			model = read(new File(pFolder, "pom.xml"));
			
			for (String module : model.getModules())
			{
				subPoms.add(new Pom(new File(pFolder, module), this));
			}
			
		} catch (Exception e) {
			throw new MojoExecutionException(String.format("Error reading project configuration in folder %s.", pFolder.getAbsolutePath()), e);
		}
	}
	
	
	
	public Pom(File pFolder,boolean newFile) throws MojoExecutionException
	{
		try {
			if(newFile)
			{
				model = read(new File(pFolder, "pom.xml"));
			}
			else
			{
				model = read(pFolder);
			}

		} catch (Exception e) {
			throw new MojoExecutionException(String.format("Error reading project configuration in folder %s.", pFolder.getAbsolutePath()), e);
		}
	}
	
	
	
	
	public Pom(String pBomType,ProjectConfiguration projectConfig) throws Exception
	{
		model = new Model();
		
		model.setDependencies(new ArrayList<Dependency>());
		
		for(ProjectDependency dependency:projectConfig)
		{
			addDependency(dependency);
		}
		
		write(pBomType);
	}	
	
	
	private Pom(File pFolder, Pom pParentPom) throws MojoExecutionException
	{
		this(pFolder);
		parentPom = pParentPom;
	}

	

	public Model getModel()
	{
		return model;
	}
	
	
	public File getPomFile()
	{
		return model.getPomFile();
	}
	
	public String getVersion()
	{
		return model.getVersion();
	}
	
	public String getGroupId()
	{
		return model.getGroupId();
	}
	
	public String getArtifactId()
	{
		return model.getArtifactId();
	}
	
	public Properties getProperties()
	{
		return model.getProperties();
	}
	
	public List<Dependency> getDependencies()
	{
		return model.getDependencies();
	}
	
	public DependencyManagement getDependencyManagement()
	{
		return model.getDependencyManagement();
	}
	
	protected Pom getParent()
	{
		return parentPom;
	}
	
	public List<Pom> getSubPoms()
	{
		return subPoms;
	}
	
	private static Model read(String pArtifactId,String pPomXMlContent) throws PomException
	{
		return PomUtil.buildModel(pArtifactId,pPomXMlContent);
	}
	
	private static Model read(File pPomFile) throws PomException
	{
		try {
			if (null == pPomFile) {
				throw new PomException("pPomFile cannot be null");
			}
			if (!pPomFile.isFile()) {
				throw new PomException(String.format("%s is not a valid file,", pPomFile.toString()));
			}
			if (!pPomFile.exists()) {
				throw new PomException(String.format("%s does not exists,", pPomFile.toString()));
			}
			final MavenXpp3Reader mavenReader = new MavenXpp3Reader();
			final FileReader reader = new FileReader(pPomFile);
			final Model model = mavenReader.read(reader);
			model.setPomFile(pPomFile);
			
			return model;
		} catch (Exception e) {
			throw new PomException(String.format("Error reading pom file %s.", pPomFile.getAbsolutePath()), e);
		}
	}	
	
	public void update(ProjectConfiguration pProjectConfiguration) throws UpdateProjectMojoException
	{
		update(pProjectConfiguration, false);
	}
	
	public void update(ProjectConfiguration pProjectConfiguration,boolean pSync) throws UpdateProjectMojoException
	{
		updateVersion(pProjectConfiguration);
		updateConnection();
		update(this, pProjectConfiguration,pSync);
		for (Pom subPom : subPoms)
		{
			subPom.update(pProjectConfiguration,pSync);
		}
	}
	
	protected void updateProperty(String pPropertyName, String pPropertyValue)
	{
		if (null != model.getProperties() &&
			null != model.getProperties().getProperty(pPropertyName))
		{
			model.getProperties().setProperty(pPropertyName, pPropertyValue);
		}
		else
		{
			if (null != parentPom)
			{
				parentPom.updateProperty(pPropertyName, pPropertyValue);
			}
		}
	}
	
	private void updateVersion(ProjectConfiguration pProjectConfiguration) throws DependencyOutOfSequenceException
	{
		String modelVersion = null == parentPom ? model.getVersion() : model.getParent().getVersion();
		
		if (null != modelVersion)
		{
            Dependency dependency = pProjectConfiguration.getDependency(this);
            if (dependency !=null){
                if (null == parentPom)
                {
                    String version = dependency.getVersion();
                    model.setVersion(version);

                }
                else
                {
                    String version = dependency.getVersion();
                    model.getParent().setVersion(version);

                }
            }
		}
		
	}
	
	protected void updateConnection() throws DependencyOutOfSequenceException
	{
		// Security, add Variable ${scmRepositoryWorkspace}, if it is not done. 
		// TODO : put information in configuration file
        if (model.getScm() != null && model.getScm().getConnection() != null){
            String connection = model.getScm().getConnection();
            if (connection.contains("${scmRepositoryWorkspace}") == false) {
                setScm();
            }
        }
	}
	private void setScm() {
		String connection = Consts.CONNECTION;
		Scm scm = new Scm();
		scm.setConnection(connection);
		model.setScm(scm);
	}
	
	// Not necessary for Business process. To be analyzed for Services
	private static boolean hasToUpdateVersion(String pSourceModelVersion, String pDestModelVersion)
	{
		if (pSourceModelVersion.compareTo(pDestModelVersion) < 0)
		{
			return true;
		}

		// Check also for number comparison: 5 < 10 as number, but not as string
		String[] sourceModelVersions = pSourceModelVersion.replace("-SNAPSHOT", "").split("\\.");
		String[] destModelVersions = pDestModelVersion.replace("-SNAPSHOT", "").split("\\.");
		int count = Math.min(sourceModelVersions.length, destModelVersions.length);
		
		for (int i=0 ; i<count ; i++)
		{
			if (Integer.parseInt(sourceModelVersions[i]) < Integer.parseInt(destModelVersions[i]))
			{
				return true;
			}
		}
		
		// If still equality, return the one which has more digits as the more updated one
		return sourceModelVersions.length < destModelVersions.length;
	}

	private static void update(Pom pPom, ProjectConfiguration pProjectConfiguration,boolean pSync)
	{
		Model model = pPom.getModel();
		
		if (null == pPom.getParent()){
			updateParent(pPom, pProjectConfiguration);
		}
			
		if (null != model.getDependencyManagement())
		{
			for (Dependency dependency : pPom.getModel().getDependencyManagement().getDependencies())
			{
				updateDependency(pPom, dependency, pProjectConfiguration,pSync);
			}
		}
		for (Dependency dependency : model.getDependencies())
		{
			updateDependency(pPom, dependency, pProjectConfiguration,pSync);
		}
	}
	
	
	private  static void updateParent(Pom pPom, ProjectConfiguration pProjectConfiguration){
		Model model = pPom.getModel();
		Pom pomParent = new Pom(model.getParent());
		Dependency dependency = pProjectConfiguration.getDependency(pomParent);
		if (dependency != null){
			String version = dependency.getVersion();
			model.getParent().setVersion(version);
		}
	}
	
	private static void updateDependency(Pom pPom, Dependency pDependency, ProjectConfiguration pProjectConfiguration,boolean pSync)
	{
		if (null == pDependency.getVersion())
		{
			// In this case, the version is taken from the dependency management structure
			return;
		}
		ProjectDependency newDependency = pProjectConfiguration.getDependency(pDependency);
		
		if (null == newDependency)
		{			
			return;
		}
		
		Matcher matcher = PATTERN.matcher(pDependency.getVersion());
		
		if (matcher.matches())
		{
			String propertyName = matcher.group(2);
			String propertyValue = pPom.getModel().getProperties().getProperty(propertyName);
			if (null == propertyValue)
			{
				// In this case, the property is in parent pom
				if (null != pPom.getParent())
				{
					pPom.getParent().updateProperty(propertyName, newDependency.getVersion());
				}
			}
			else
			{
				pPom.getModel().getProperties().setProperty(matcher.group(2), newDependency.getVersion());
			}
		}
		else
		{
			pDependency.setVersion(newDependency.getVersion());
			pDependency.setType(newDependency.getType());
			pDependency.setScope(newDependency.getScope());
			pDependency.setExclusions(newDependency.getExclusions());
		}
	}
	
	public void save() throws Exception
	{
		final PomWriter pomWriter = new PomWriter(model.getPomFile());
		
		pomWriter.write(this);		
		
		for (Pom subPom : subPoms)
		{
			subPom.save();
		}
	}
	


	public void write(String pBomType) throws Exception
	{
		File currentFolder = new File(String.format("%s/",pBomType));
		FileUtils.forceMkdir(currentFolder);
		File pomFile = new File(currentFolder,"pom.xml");
		
		if (!pomFile.exists())
		{
			pomFile.createNewFile();
		}
		
		write(pomFile);
	}
	
	public void addDependency(ProjectDependency pDependency)
	{
		model.addDependency(pDependency);
	}
	
	public void addDependencyManagement(ProjectDependency pDependency)
	{
		getDependencyManagement().addDependency(pDependency);
	}
	
	public void removeDependency(ProjectDependency pDependency)
	{
		model.removeDependency(pDependency);
	}
	
	public void removeDependencyManagement(Dependency pDependency)
	{
		getDependencyManagement().removeDependency(pDependency);
	}
	
	public void removeDependencyManagement(List<Dependency> pDependenciesToRemove)
	{
		getDependencyManagement().getDependencies().removeAll(pDependenciesToRemove);
	}

	private void write(File pPomFile) throws PomException
	{
		try {
			if (null == pPomFile) {
				throw new PomException("pPomFile cannot be null");
			}
			if (!pPomFile.isFile()) {
				throw new PomException(String.format("%s is not a valid file,", pPomFile.toString()));
			}
			
			final MavenXpp3Writer mavenWriter = new MavenXpp3Writer();
			final FileWriter writer = new FileWriter(pPomFile);
			
			model.setPomFile(pPomFile);
			mavenWriter.write(writer, model);
		} catch (Exception e) {
			throw new PomException(String.format("Error writing pom file %s.", pPomFile.getAbsolutePath()), e);
		}
	}
	
	
	public void addScm() {
        setScm();
		
	}
	
}
