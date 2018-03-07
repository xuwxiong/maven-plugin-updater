package intact.maven.plugins.versionupdater;

public class PomException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5685525309153122641L;

	public PomException()
	{
		super();

	}
	
	public PomException(String pMessage)
	{
		super(pMessage);
	}
	
	public PomException(String pMessage, Throwable pThrowable)
	{
		super(pMessage, pThrowable);
	}
}
