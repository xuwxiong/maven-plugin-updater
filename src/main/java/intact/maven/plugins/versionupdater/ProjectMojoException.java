package intact.maven.plugins.versionupdater;

public class ProjectMojoException extends RuntimeException
{
	private static final long serialVersionUID = -9177208897630574243L;

	public ProjectMojoException(String pMessage)
	{
		super(pMessage);
	}
}
