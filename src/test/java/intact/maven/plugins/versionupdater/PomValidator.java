package intact.maven.plugins.versionupdater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

public class PomValidator
{
	public static void viewModule(Pom pPom)
	{		
		System.out.println(pPom.getVersion());
		System.out.println(pPom.getProperties().getProperty("version-with-property"));
		System.out.println(getDependencyVersion(pPom, "artifact-in-all-versions"));
		System.out.println(getDependencyVersion(pPom, "artifact-in-latest-version"));
		System.out.println(getDependencyVersion(pPom, "artifact-using-property"));
		System.out.println(getDependencyVersion(pPom, "artifact-in-previous-version"));
		System.out.println(getDependencyVersion(pPom, "simple-library"));
	}
	
	public static void validateSingleModule5_3(Pom pPom)
	{
		assertEquals("VERSION-5.3", pPom.getVersion());
		assertEquals("VERSION-5.3", pPom.getProperties().getProperty("version-with-property"));
		assertEquals("VERSION-5.3", getDependencyVersion(pPom, "artifact-in-all-versions"));
		assertEquals("VERSION-5.3", getDependencyVersion(pPom, "artifact-in-latest-version"));
		assertEquals("${version-with-property}", getDependencyVersion(pPom, "artifact-using-property"));
		assertEquals("VERSION-5.3", getDependencyVersion(pPom, "artifact-in-previous-version"));
		//assertEquals("UNKNOWN-VERSION", getDependencyVersion(pPom, "simple-library"));
	}
	
	public static void validateSingleModule5_4(Pom pPom)
	{
		// this should not be changed
		assertEquals("VERSION-5.4", pPom.getVersion());
		assertEquals("VERSION-5.4", pPom.getProperties().getProperty("version-with-property"));
		assertEquals("VERSION-5.4", getDependencyVersion(pPom, "artifact-in-all-versions"));
		// do not update this one
		assertEquals("VERSION-5.3", getDependencyVersion(pPom, "artifact-in-latest-version"));
		assertEquals("${version-with-property}", getDependencyVersion(pPom, "artifact-using-property"));
		assertEquals("VERSION-5.4", getDependencyVersion(pPom, "artifact-in-previous-version"));
		//assertEquals("UNKNOWN-VERSION", getDependencyVersion(pPom, "simple-library"));
	}
	
	public static void validateSingleModule5_5(Pom pPom)
	{
		assertEquals("VERSION-5.5", pPom.getVersion());
		assertEquals("VERSION-5.5", pPom.getProperties().getProperty("version-with-property"));
		assertEquals("VERSION-5.5", getDependencyVersion(pPom, "artifact-in-all-versions"));
		assertEquals("VERSION-5.5", getDependencyVersion(pPom, "artifact-in-latest-version"));
		assertEquals("${version-with-property}", getDependencyVersion(pPom, "artifact-using-property"));
		assertEquals("VERSION-5.4", getDependencyVersion(pPom, "artifact-in-previous-version"));
	}
	

	public static void validateSingleModule5_13(Pom pPom)
	{
		assertEquals("VERSION-5.5", pPom.getVersion());
		assertEquals("VERSION-5.13", pPom.getProperties().getProperty("version-with-property"));
		assertEquals("VERSION-5.13", getDependencyVersion(pPom, "artifact-in-all-versions"));
		assertEquals("VERSION-5.13", getDependencyVersion(pPom, "artifact-in-latest-version"));
		assertEquals("${version-with-property}", getDependencyVersion(pPom, "artifact-using-property"));
		assertEquals("VERSION-5.11", getDependencyVersion(pPom, "artifact-in-previous-version"));
	}
	
	public static void validateMultiModule5_3(Pom pPom)
	{
		assertEquals("VERSION-5.5", pPom.getVersion());
		assertEquals("VERSION-5.3", pPom.getProperties().getProperty("version-with-property"));
		assertEquals("VERSION-5.3", pPom.getProperties().getProperty("version-with-property-in-parent-pom"));
		assertEquals("VERSION-5.3", getDependencyVersion(pPom, "artifact-in-all-versions"));
		assertEquals("VERSION-5.3", getDependencyManagementVersion(pPom, "artifact-in-all-versions"));
		assertEquals("VERSION-5.3", getDependencyVersion(pPom, "artifact-in-latest-version"));
		assertEquals("VERSION-5.3", getDependencyManagementVersion(pPom, "artifact-in-latest-version"));
		assertEquals("${version-with-property}", getDependencyVersion(pPom, "artifact-using-property"));
		assertEquals("${version-with-property}", getDependencyManagementVersion(pPom, "artifact-using-property"));
		assertEquals("VERSION-5.3", getDependencyVersion(pPom, "artifact-in-previous-version"));
		assertEquals("VERSION-5.3", getDependencyManagementVersion(pPom, "artifact-in-previous-version"));
		assertEquals("VERSION-5.3", getDependencyVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		assertEquals("VERSION-5.3", getDependencyManagementVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		assertEquals("VERSION-5.3", getDependencyVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		assertEquals("VERSION-5.3", getDependencyManagementVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		
		for (Pom subPom : pPom.getSubPoms())
		{
			if (subPom.getPomFile().getAbsolutePath().contains("repository"))
			{
				assertEquals("VERSION-5.3", subPom.getProperties().getProperty("version-with-property"));
				
				assertNull(subPom.getProperties().getProperty("version-with-property-in-parent-pom"));
				assertNull(getDependencyVersion(subPom, "artifact-used-in-multi-module-version-in-parent"));
				
				assertEquals("${version-with-property-in-parent-pom}", getDependencyVersion(subPom, "artifact-used-in-multi-module-using-property"));
			}
		}
	}
	
	public static void validateMultiModule5_4(Pom pPom)
	{
		assertEquals("5.4.0.1-SNAPSHOT", pPom.getVersion());
		assertEquals("VERSION-5.4", pPom.getProperties().getProperty("version-with-property"));
		assertEquals("VERSION-5.4", pPom.getProperties().getProperty("version-with-property-in-parent-pom"));
		assertEquals("VERSION-5.4", getDependencyVersion(pPom, "artifact-in-all-versions"));
		assertEquals("VERSION-5.4", getDependencyManagementVersion(pPom, "artifact-in-all-versions"));
		// do not update this one
		assertEquals("VERSION-5.3", getDependencyVersion(pPom, "artifact-in-latest-version"));
		assertEquals("VERSION-5.3", getDependencyManagementVersion(pPom, "artifact-in-latest-version"));
		assertEquals("${version-with-property}", getDependencyVersion(pPom, "artifact-using-property"));
		assertEquals("${version-with-property}", getDependencyManagementVersion(pPom, "artifact-using-property"));
		assertEquals("VERSION-5.4", getDependencyVersion(pPom, "artifact-in-previous-version"));
		assertEquals("VERSION-5.4", getDependencyManagementVersion(pPom, "artifact-in-previous-version"));
		assertEquals("VERSION-5.4", getDependencyVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		assertEquals("VERSION-5.4", getDependencyManagementVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		assertEquals("VERSION-5.4", getDependencyVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		assertEquals("VERSION-5.4", getDependencyManagementVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		
		for (Pom subPom : pPom.getSubPoms())
		{
			assertEquals("5.4.0.1-SNAPSHOT", subPom.getParent().getVersion());
			if (subPom.getPomFile().getAbsolutePath().contains("repository"))
			{
				assertEquals("VERSION-5.4", subPom.getProperties().getProperty("version-with-property"));
				
				assertNull(subPom.getProperties().getProperty("version-with-property-in-parent-pom"));
				assertNull(getDependencyVersion(subPom, "artifact-used-in-multi-module-version-in-parent"));
				
				assertEquals("${version-with-property-in-parent-pom}", getDependencyVersion(subPom, "artifact-used-in-multi-module-using-property"));
			}
		}
	}
	
	public static void validateMultiModule5_5(Pom pPom)
	{
		assertEquals("5.5.0.0-SNAPSHOT", pPom.getVersion());
		assertEquals("VERSION-5.5", pPom.getProperties().getProperty("version-with-property"));
		assertEquals("VERSION-5.5", pPom.getProperties().getProperty("version-with-property-in-parent-pom"));
		assertEquals("VERSION-5.5", getDependencyVersion(pPom, "artifact-in-all-versions"));
		assertEquals("VERSION-5.5", getDependencyManagementVersion(pPom, "artifact-in-all-versions"));
		// do not update this one
		assertEquals("VERSION-5.5", getDependencyVersion(pPom, "artifact-in-latest-version"));
		assertEquals("VERSION-5.5", getDependencyManagementVersion(pPom, "artifact-in-latest-version"));
		assertEquals("${version-with-property}", getDependencyVersion(pPom, "artifact-using-property"));
		assertEquals("${version-with-property}", getDependencyManagementVersion(pPom, "artifact-using-property"));
		assertEquals("VERSION-5.4", getDependencyVersion(pPom, "artifact-in-previous-version"));
		assertEquals("VERSION-5.4", getDependencyManagementVersion(pPom, "artifact-in-previous-version"));
		assertEquals("VERSION-5.5", getDependencyVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		assertEquals("VERSION-5.5", getDependencyManagementVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		assertEquals("VERSION-5.5", getDependencyVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		assertEquals("VERSION-5.5", getDependencyManagementVersion(pPom, "artifact-used-in-multi-module-version-in-parent"));
		
		for (Pom subPom : pPom.getSubPoms())
		{
			if (subPom.getPomFile().getAbsolutePath().contains("repository"))
			{
				assertEquals("5.5.0.0-SNAPSHOT", subPom.getParent().getVersion());
				assertEquals("VERSION-5.5", subPom.getProperties().getProperty("version-with-property"));
				
				assertNull(subPom.getProperties().getProperty("version-with-property-in-parent-pom"));
				assertNull(getDependencyVersion(subPom, "artifact-used-in-multi-module-version-in-parent"));
				
				assertEquals("${version-with-property-in-parent-pom}", getDependencyVersion(subPom, "artifact-used-in-multi-module-using-property"));
			}
		}
	}
	
	public static String getDependencyVersion(Pom pPom, String pArtifactId)
	{
		for (Dependency dependency : pPom.getDependencies())
		{
			if (pArtifactId.equals(dependency.getArtifactId()))
			{
				return dependency.getVersion();
			}
		}
		return null;
	}

	private static String getDependencyVersion(Model pModel, String pArtifactId)
	{
		for (Dependency dependency : pModel.getDependencies())
		{
			if (pArtifactId.equals(dependency.getArtifactId()))
			{
				return dependency.getVersion();
			}
		}
		return null;
	}

	private static String getDependencyManagementVersion(Pom pPom, String pArtifactId)
	{
		for (Dependency dependency : pPom.getDependencyManagement().getDependencies())
		{
			if (pArtifactId.equals(dependency.getArtifactId()))
			{
				return dependency.getVersion();
			}
		}
		return null;
	}
}
