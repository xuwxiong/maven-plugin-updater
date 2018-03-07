package intact.maven.plugins.versionupdater.utils;

public final class Consts {
	
	public static final String CONNECTION = "scm:jazz:https://prod-rtc-b2e.iad.ca.inet/ccm:${scmRepositoryWorkspace}";
	public static final String BRANCH ="branch";
	public static final String MASTER ="MASTER";
	public static final String SNAPSHOT ="SNAPSHOT";
	public static final String SEPARATORS_REGEXP = "[.-]{1}";
	public static final String VERSION_TOKEN_REGEXP = "[a-zA-Z0-9_]+";
	public static Object SHORTGROUPID ="shortGroupId";
	public static String XPATHPROPERTIES ="/project/properties/";
	public static String SCMREPOSITORY="scmRepositoryWorkspace";
	public static String THECONNECTION="connection";

}
