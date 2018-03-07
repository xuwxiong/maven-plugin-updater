package intact.maven.plugins.versionupdater;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import intact.maven.plugins.versionupdater.utils.Consts;
import intact.maven.plugins.versionupdater.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class UpdateBranchMojoTest {

	@Test
	public void testPerfectPomMaster() throws Exception {
		File workingFolder = new File("scmPomSamples/perfectPom-master.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),Consts.MASTER);
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),"/project/properties/branch"),Consts.MASTER);
		assertEquals(checkInPomFile(workingFolder.getName(),"/project/scm/connection"),Consts.CONNECTION);
	}
	

	@Test
	public void testPerfectPomBranch01() throws Exception {
		File workingFolder = new File("scmPomSamples/perfectPom-branch-01.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"branch02");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"branch02");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
	}

	@Test
	public void testPerfectPomBranch02() throws Exception {
		File workingFolder = new File("scmPomSamples/perfectPom-branch-02.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"branch02");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"branch02");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
	}

	@Test
	public void testBranchMismatchPom01() throws Exception {
		File workingFolder = new File("scmPomSamples/branchMismatchPom-01.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"branch01");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"branch01");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
	}

	@Test
	public void testBranchMismatchPom02() throws Exception {
		File workingFolder = new File("scmPomSamples/branchMismatchPom-02.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"branch02");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"branch02");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
	}

	@Test
	public void testWorkingWrongPomBranch() throws Exception {
		File workingFolder = new File(
				"scmPomSamples/workingWrongPom-branch.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"branch");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"branch");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
	}
	
	
	@Test
	public void testWorkingWrongPomBranchSample2() throws Exception {
		File workingFolder = new File(
				"scmPomSamples/workingWrongPom-branch-sample2.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"branch");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"branch");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
		assertNull(pom.getProperties().get(Consts.SCMREPOSITORY));
		assertNull(checkInPomFile(workingFolder.getName(),"/project/properties/scmRepositoryWorkspace"));
	}

	@Test
	public void testWorkingWrongPomMaster() throws Exception {
		File workingFolder = new File(
				"scmPomSamples/workingWrongPom-master.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),Consts.MASTER);
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),Consts.MASTER);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
	}

	@Test
	public void testBranchMissingPom() throws Exception {
		File workingFolder = new File("scmPomSamples/branchMissingPom.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),Consts.MASTER);
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),Consts.MASTER);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
	}

	@Test
	public void testMissingSCMPomBranch02() throws Exception {
		File workingFolder = new File(
				"scmPomSamples/missingSCMPom-branch-02.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"branch02");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"branch02");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
	}
	

	@Test(expected = UpdateProjectMojoException.class)
	public void testNonWorkingPomBranch01() throws MojoExecutionException, IOException {
		File workingFolder = new File(
				"scmPomSamples/nonWorkingPom-branch-01.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
			@SuppressWarnings("unused")
			Pom pom = executeTest(workingFolder,updateProjectMojo);
	        fail();
	}
	
	
	@Test
	public void testCvsRulesPersonal() throws Exception {
		File workingFolder = new File(
				"scmPomSamples/cvs-rules-personal.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"D11PRODFIX");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"D11PRODFIX");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
	}
	
	
	@Test
	public void testElevationServiceConsumer() throws Exception {
		File workingFolder = new File(
				"scmPomSamples/elevationServiceConsumer.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"D11PRODFIX");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"D11PRODFIX");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
	}
	
	
	
	
	
	
	@Test
	public void testPerfectPomMasterWithCommandBranc() throws Exception {
		File workingFolder = new File("scmPomSamples/perfectPom-master.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = true;
		updateProjectMojo.targetBranch = "BUGFIX";
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"BUGFIX");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
        assertEquals(pom.getModel().getVersion(),"1.0.0.BUGFIX-SNAPSHOT");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"BUGFIX");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),"/project/version"),"1.0.0.BUGFIX-SNAPSHOT");
	}
	
	
	
	@Test
	public void testPerfectPomMasterWithCommandBrancVieilleBranche6() throws Exception {
		File workingFolder = new File("scmPomSamples/vieilleBranche-6.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "D11BUGFIX";
		updateProjectMojo.repairPom = true;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"D11BUGFIX");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
        assertEquals(pom.getModel().getVersion(),"1.0.1-D11BUGFIX-6-SNAPSHOT");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"D11BUGFIX");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),"/project/version"),"1.0.1-D11BUGFIX-6-SNAPSHOT");
	}
	
	
	
	@Test
	public void testPerfectPomMasterWithCommandBrancVieilleBranche() throws Exception {
		File workingFolder = new File("scmPomSamples/vieilleBranche.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "D11BUGFIX";
		updateProjectMojo.repairPom = true;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"D11BUGFIX");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
        assertEquals(pom.getModel().getVersion(),"1.0.1-D11BUGFIX-SNAPSHOT");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"D11BUGFIX");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),"/project/version"),"1.0.1-D11BUGFIX-SNAPSHOT");
	}
	
	
	
	@Test
	public void testBranchLineMaster() throws Exception {
		File workingFolder = new File("scmPomSamples/branchMASTER.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "MASTER";
		updateProjectMojo.repairPom = true;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"MASTER");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
        assertEquals(pom.getModel().getVersion(),"1.0.1-SNAPSHOT");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"MASTER");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),"/project/version"),"1.0.1-SNAPSHOT");
		assertNull(pom.getProperties().get(Consts.SCMREPOSITORY));
//		assertNull(checkInPomFile(workingFolder.getName(),"/project/properties/scmRepositoryWorkspace"));
	}
	
	
	@Test
	public void testBranchLineMaster2() throws Exception {
		File workingFolder = new File("scmPomSamples/branchMASTER2.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "MASTER";
		updateProjectMojo.repairPom = true;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"MASTER");
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
        assertEquals(pom.getModel().getVersion(),"1.0.1-SNAPSHOT");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"MASTER");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),"/project/version"),"1.0.1-SNAPSHOT");
		assertNull(pom.getProperties().get(Consts.SCMREPOSITORY));
	}

	@Test
	public void testRelease2WithCommandBrancMASTER() throws Exception {
		File workingFolder = new File("scmPomSamples/release2.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "DOM5";
		updateProjectMojo.repairPom = true;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
        assertEquals(pom.getModel().getVersion(),"5.13.3.0.0.0.DOM5-SNAPSHOT");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),"/project/version"),"5.13.3.0.0.0.DOM5-SNAPSHOT");
	}
	
	
	@Test
	public void testReleaseWithCommandBranc() throws Exception {
		File workingFolder = new File("scmPomSamples/release.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "DOM5";
		updateProjectMojo.repairPom = true;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
        assertEquals(pom.getModel().getVersion(),"5.2.4.DOM5-SNAPSHOT");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),"/project/version"),"5.2.4.DOM5-SNAPSHOT");
	}
	
	
	@Test
	public void testRelease2WithCommandBranc() throws Exception {
		File workingFolder = new File("scmPomSamples/release2.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "DOM5";
		updateProjectMojo.repairPom = true;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getModel().getScm().getConnection(),Consts.CONNECTION);
        assertEquals(pom.getModel().getVersion(),"5.13.3.0.0.0.DOM5-SNAPSHOT");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/scm/connection"),Consts.CONNECTION);
		assertEquals(checkInPomFile(workingFolder.getName(),"/project/version"),"5.13.3.0.0.0.DOM5-SNAPSHOT");
	}
	

	
	@Test
	public void testPerfectPomMasterRepairFalse() throws Exception {
		File workingFolder = new File("scmPomSamples/perfectPom-master.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertNull(pom.getProperties().get(Consts.BRANCH));
		assertNull(checkInPomFile(workingFolder.getName(),"/project/properties/branch"));
	}
	
	@Test(expected = UpdateProjectMojoException.class)   
	public void testPerfectPomBranch01RepairFalse() throws Exception {
		File workingFolder = new File("scmPomSamples/perfectPom-branch-01.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		fail();
	}
	
	
	@Test
	public void testPerfectPomBranch02RepairFalse() throws Exception {
		File workingFolder = new File("scmPomSamples/perfectPom-branch-02.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"branch02");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"branch02");
	}
	
	
	@Test(expected = UpdateProjectMojoException.class) 
	public void testBranchMismatchPom01RepairFalse()throws Exception {
		
		File workingFolder = new File("scmPomSamples/branchMismatchPom-01.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		fail();
	}
	
	
	
	@Test
	public void testBranchMismatchPom02RepairFalse() throws Exception {
		File workingFolder = new File("scmPomSamples/branchMismatchPom-02.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertEquals(pom.getProperties().get(Consts.BRANCH),"branch02");
		assertEquals(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"),"branch02");
	}
	
	
	@Test(expected = UpdateProjectMojoException.class)   
	public void testWorkingWrongPomBranchRepairFalse() throws Exception {
		File workingFolder = new File(
				"scmPomSamples/workingWrongPom-branch.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		 fail();
	}
	
	
	@Test(expected = UpdateProjectMojoException.class)
	public void testWorkingWrongPomMasterRepairFalse() throws Exception {
		File workingFolder = new File(
				"scmPomSamples/workingWrongPom-master.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		 fail();
	}
	
	
	@Test(expected = UpdateProjectMojoException.class)
	public void testBranchMissingPomRepairFalse() throws Exception {
		File workingFolder = new File("scmPomSamples/branchMissingPom.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		 fail();
	}
	
	
	@Test(expected = UpdateProjectMojoException.class)   
	public void testMissingSCMPomBranch02RepairFalse() throws Exception {
		File workingFolder = new File(
				"scmPomSamples/missingSCMPom-branch-02.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		 fail();
	}
	
	
	@Test
	public void testCvsRulesPersonalRepairFalse() throws Exception {
		File workingFolder = new File(
				"scmPomSamples/cvs-rules-personal.xml");
		
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		assertNull(pom.getProperties().get(Consts.BRANCH));
		assertNull(checkInPomFile(workingFolder.getName(),
				"/project/properties/branch"));
	}

	@Test(expected = UpdateProjectMojoException.class) 
	public void testPerfectPomMasterWithCommandBrancRepairFalse() throws Exception {
		File workingFolder = new File("scmPomSamples/perfectPom-master.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "BUGFIX";
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		fail();
	}

	@Test(expected = UpdateProjectMojoException.class)  
	public void testPerfectPomMasterWithCommandBrancVieilleBranche6RepairFalse() throws Exception {
		File workingFolder = new File("scmPomSamples/vieilleBranche-6.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "D11BUGFIX";
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		fail();
	}
	
	
	
	@Test(expected = UpdateProjectMojoException.class)
	public void testPerfectPomMasterWithCommandBrancVieilleBrancheRepairFalse() throws Exception {
		File workingFolder = new File("scmPomSamples/vieilleBranche.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "D11BUGFIX";
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		fail();
	}
	
	
	@Test(expected = UpdateProjectMojoException.class)   
	public void testWorkingWrongPomBranchSample2RepairFalse() throws Exception {
		File workingFolder = new File(
				"scmPomSamples/workingWrongPom-branch-sample2.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		 fail();
	}
	
	
	@Test(expected = UpdateProjectMojoException.class)  
	public void testBranchLineMasterRepairFalse() throws Exception {
		File workingFolder = new File("scmPomSamples/branchMASTER.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "MASTER";
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		fail();
	}
	
	@Test(expected = UpdateProjectMojoException.class)  
	public void testBranchLineMasterRepairFalse2() throws Exception {
		File workingFolder = new File("scmPomSamples/branchMASTER.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "MASTER";
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		fail();
	}
	
	
	@Test(expected = UpdateProjectMojoException.class)
	public void testReleaseWithCommandBrancRepairFalse() throws Exception {
		File workingFolder = new File("scmPomSamples/release.xml");
		UpdateBranch updateProjectMojo = new UpdateBranchProjectMojo();
		updateProjectMojo.targetBranch = "DOM5";
		updateProjectMojo.repairPom = false;
        updateProjectMojo.baseDir = new File(System.getProperty("user.dir"));
		@SuppressWarnings("unused")
		Pom pom = executeTest(workingFolder,updateProjectMojo);
		fail();
	}
	
	
	


	private String checkPomElementValue(String expression, File filePom) throws SAXException,
			IOException, ParserConfigurationException, XPathExpressionException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		Document document = domFactory.newDocumentBuilder().parse(
				filePom.getAbsolutePath());
		XPath xpath = XPathFactory.newInstance().newXPath();
		String  value = xpath.compile(expression).evaluate(document);
		if(StringUtils.isEmpty(value))
			return null;
		return value;
	}
	
	
	private String checkInPomFile(String nameFile, String expression)
			throws IOException, SAXException, ParserConfigurationException,
			XPathExpressionException {
		String filderPom = StringUtils.strip("data/branchFixTests/" + nameFile, ".xml");
		File filePom = new File(filderPom + "/pom.xml");
		return checkPomElementValue(expression, filePom);
	}

	private Pom executeTest(File workingFolder,UpdateBranch updateProjectMojo) throws IOException, MojoExecutionException {
		File fileExample = new File(StringUtils.strip("data/branchFixTests/"
					+ workingFolder.getName(), ".xml"));
		File filePom = FileUtils.cloneFileToPom(workingFolder,fileExample);
		Pom pom = new Pom(filePom, false);
		updateProjectMojo.setPom(pom);
		updateProjectMojo.execute();
		return pom;
	}
}
