package intact.maven.plugins.versionupdater.dependencies;

public class ProjectConfigurationsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProjectConfigurationsException()
	{
		super();
	}

	public ProjectConfigurationsException(String pMessage)
	{
		super(pMessage);
	}
	
	public ProjectConfigurationsException(String pMessage, Throwable pThrowable)
	{
		super(pMessage, pThrowable);
	}
}
