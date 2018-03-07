package intact.maven.plugins.versionupdater;

import intact.maven.plugins.versionupdater.utils.Consts;
import intact.maven.plugins.versionupdater.utils.PomUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class UpdateBranch extends AbstractMojo {

    // Set to protected so that tests can set it. Yes.. it should have been loaded with plexus instead...
    @Parameter(property = "targetBranch")
    protected String targetBranch;
    @Parameter(property = "repairPom", defaultValue = "true")
    protected Boolean repairPom;
    @Parameter(property = "project.basedir", readonly = true)
    protected File baseDir;

    private Pom pom;
    private PomUtil util = new PomUtil();

    public PomUtil getUtil() {
        return util;
    }

    public void setUtil(PomUtil util) {
        this.util = util;
    }

    public Pom getPom() {
        return pom;
    }

    public void setPom(Pom pom) {
        this.pom = pom;
    }

    @Override
    public void execute() {
        try {
            getLog().debug("Starting execute method.");
            if (this.getPom() == null) {
                Pom pom = new Pom(new File(baseDir.getAbsolutePath() +File.separator+"pom.xml"), false);
                this.setPom(pom);
                if (this.getPom() == null) {
                    throw new UpdateProjectMojoException(String.format(
                            "%s does not exists,", "pom does not exist!!!"));
                }
            }
            if (repairPom) {
                checkProperties(pom.getProperties());
                checkForUpdateScmConnection(pom);
                if (targetBranch != null) {
                    retrieveBranchFromBranchLine(pom, targetBranch);
                } else {
                    String branch = getBranchOnTheVersion(pom.getVersion());
                    retrieveBranchFromVersion(branch);
                }
            } else {
                validProperties(pom.getProperties());
                validScm(pom);
                if (null == targetBranch) {
                    targetBranch = getBranchOnTheVersion(pom.getVersion());
                    validPropertyBranch(targetBranch);
                } else {
                    checkIsRelease(targetBranch, pom.getVersion());
                }
            }
            pom.save();

        } catch (UpdateProjectMojoException e) {
            getLog().error(e.getMessage());
            throw new UpdateProjectMojoException(e.getMessage());

        } catch (Exception e) {
            getLog().error(e.getMessage());
        } finally {
            getLog().debug("End execute method.");
        }

    }

    private void validProperties(Properties properties) {
        getLog().debug("Starting validProperties method.");
        checkShortGroupIdProperty(properties);
        if (null != properties.getProperty(Consts.SCMREPOSITORY))
            if (!properties.getProperty(Consts.SCMREPOSITORY).isEmpty()) {
                throw new UpdateProjectMojoException("Remove property scmRepositoryWorkspace to fix the pom.");
            }
        getLog().debug("End validProperties method.");

    }


    private void retrieveBranchFromBranchLine(Pom pom, String branch) {
        if (Consts.MASTER.equals(branch))
            populateMaster(pom, branch);
        else {
            retrieveVersion(pom, branch);
        }
    }


    private void populateMaster(Pom pom, String branch) {
        String version = pom.getVersion();
        String[] listString = StringUtils.split(version, '-');
        version = listString[0] + "-" + Consts.SNAPSHOT;

        getLog().info("Forcing branch to: " + branch);
        getLog().info("Updating version to: " + version);
        pom.getModel().setVersion(version);
        pom.getModel().getProperties().setProperty(Consts.BRANCH, branch);

    }

    private void checkForUpdateScmConnection(Pom pom) {
        getLog().debug("Starting checkForUpdateScmConnection method.");
        if (pom.getModel().getScm() != null) {
            if (!getUtil().validateScm(pom.getModel().getScm().getConnection())) {
                pom.updateConnection();
                getLog().info("Updating scm/connection tag to: " + pom.getModel().getScm().getConnection());
            }
        } else {
            getLog().info("Missing scm tag");
            pom.addScm();
            getLog().info("Updating scm/connection tag to: " + pom.getModel().getScm().getConnection());
        }
        getLog().debug("End checkForUpdateScmConnection method.");
    }


    private void validScm(Pom pom) {
        getLog().debug("Starting validScm method");
        if (pom.getModel().getScm() != null) {
            if (!getUtil().validateScm(pom.getModel().getScm().getConnection())) {
                throw new UpdateProjectMojoException("connection in the scm tag is " + pom.getModel().getScm()
                        .getConnection() + " and should be " + Consts.CONNECTION);
            }
        } else {
            throw new UpdateProjectMojoException("Missing connection in scm tag. Set scm/connection to: " + Consts.CONNECTION);
        }
        getLog().debug("End validScm method.");
    }

    private void retrieveBranchFromVersion(String branch) {
        getLog().debug("Starting updatePomModel method.");
        if (pom.getModel().getProperties().containsKey(Consts.BRANCH)) {
            if (!pom.getModel().getProperties().get(Consts.BRANCH).equals(branch))
                getLog().info("Mismatch between properties branch name (" + pom.getModel().getProperties().get(Consts.BRANCH) + ") and version branch: "+branch+".");
            pom.getModel().getProperties().setProperty(Consts.BRANCH, branch);
        } else {
            getLog().info("Adding a "+Consts.BRANCH+" tag in the pom properties with this value: " + branch);
            pom.getModel().getProperties().setProperty(Consts.BRANCH, branch);
        }
        getLog().debug("End updatePomModeBranch method for.");
    }

    private void validPropertyBranch(String branch) {
        getLog().debug("Starting validPropertyBranch method.");
        if (pom.getModel().getProperties().containsKey(Consts.BRANCH)) {
            if (!pom.getModel().getProperties().get(Consts.BRANCH).equals(branch)) {
                getLog().info("Mismatch between properties branch name (" + pom.getModel().getProperties().get(Consts.BRANCH) + ") and requested branch: " + branch + ".");
                throw new UpdateProjectMojoException("Update the "+Consts.BRANCH+" property to " + branch);
            }
        } else {
            if (!branch.equals(Consts.MASTER))
                getLog().info("Add a "+Consts.BRANCH+" tag in the pom properties with this value: "+branch);
        }
        getLog().debug("End validPropertyBranch method for.");
    }


    private void validateTargetBranch(String branch) {
        getLog().debug("Starting validateTargetBranch method.");
        getLog().info("Forcing branch name: " + branch);
        if (pom.getModel().getProperties().containsKey(Consts.BRANCH)) {
            if (!pom.getModel().getProperties().get(Consts.BRANCH).equals(branch))
                getLog().info("Current "+Consts.BRANCH+" property is set to: " + pom.getModel().getProperties().get(Consts.BRANCH) + "\n"
                        + "Forcing to: " + branch + "\n"
                        + "Mismatch between "+Consts.BRANCH+" property and the branch suffix in the project version.");
            throw new UpdateProjectMojoException("Change this project version to include \""+branch+"\" in its version.");
        } else {
            if (!branch.equals(pom.getVersion())) {
                getLog().info("Add a tag branch in properties and for value " + branch + " for fix the pom.");
                throw new UpdateProjectMojoException("Add a tag "+Consts.BRANCH+" in the pom properties with this value: "+branch);
            }

        }
        getLog().debug("End validateTargetBranch method.");
    }


    private void checkIsRelease(String branch, String version) {
        Boolean isRelease = StringUtils.isNumericSpace(StringUtils.replace(version, ".", "").trim());
        if (!isRelease)
            validateTargetBranch(branch);
        else {
            validateRelease(version);
        }
    }

    private void validateRelease(String version) {
        version = this.getUtil().incrementVersion(version);
        getLog().info("The new version id " + version + " for fix the pom.");
        getLog().info("Update the version for fix the pom.");
        throw new UpdateProjectMojoException("Update the version for " + version + " fix the pom.");
    }

    private void checkProperties(Properties properties) {
        getLog().debug("Starting checkProperties method.");
        checkShortGroupIdProperty(properties);
        if (null != properties.getProperty(Consts.SCMREPOSITORY))
            if (!properties.getProperty(Consts.SCMREPOSITORY).isEmpty()) {
                properties.remove(Consts.SCMREPOSITORY);
                getLog().info("Removing property scmRepositoryWorkspace.");
            }
        getLog().debug("End checkProperties method.");
    }

    private boolean checkShortGroupIdProperty(Properties properties) {
        if (!getUtil().validateProperties(properties))
            throw new UpdateProjectMojoException(
                    "Invalid POM: missing \"shortGroupId\" property.");
        return true;
    }

    private String getBranchOnTheVersion(String version) {
        return getUtil().validateBranch(version);
    }


    private void retrieveVersion(Pom pom, String targetBranch) {
        getLog().info("Current version is: " + pom.getVersion());

        ArrayList<String> tokens = new ArrayList<String>();
        Collections.addAll(tokens, pom.getVersion().split(Consts.SEPARATORS_REGEXP));
        ArrayList<String> separators = new ArrayList<String>();
        Collections.addAll(separators, pom.getVersion().split(Consts.VERSION_TOKEN_REGEXP));
        if (separators.size()> 0 && separators.size() == tokens.size()){
            separators.remove(0);
        }

        // branchIdx at -1 actually means the version has no branch name. The default branch name is MASTER.
        int branchIdx = -1;
        for (int i = tokens.size()-1 ; branchIdx == -1 && i > 0 ; i--){
            if (!StringUtils.isNumeric(tokens.get(i)) && !tokens.get(i).equals(Consts.SNAPSHOT)){
                branchIdx = i;
            }
        }
        if (tokens.get(tokens.size()-1).equals(Consts.SNAPSHOT)){
            tokens.remove(tokens.size()-1);
            separators.remove(separators.size()-1);
        }
        if (branchIdx == -1){
            tokens.add(targetBranch);
            separators.add(".");
        } else {
            tokens.set(branchIdx, targetBranch);
        }
        tokens.add(Consts.SNAPSHOT);
        separators.add("-");
        String version = "";
        for (int i = 0 ; i < separators.size() ; i++){
            version += tokens.get(i) + separators.get(i);
        }
        // Separators should be between tokens. Thus already smaller by exactly 1.
        version += tokens.get(separators.size());

        pom.getModel().setVersion(version);
        pom.getModel().getProperties().setProperty(Consts.BRANCH, targetBranch);
        getLog().info("Target branch is \"" + targetBranch + "\" and the version has changed to \"" + version + "\"");
    }
}
