package intact.maven.plugins.versionupdater;

public class DependencyOutOfSequenceException extends UpdateProjectMojoException
{
	private static final long serialVersionUID = -4522776779171437649L;

	public DependencyOutOfSequenceException(Pom pPom, String pVersion)
	{
		super(buildMessage(pPom, pVersion));
	}
	
	private static String buildMessage(Pom pPom, String pVersion)
	{
		return String.format("Dependency out of sequence: project has version %s, configuration has %s", pPom.getVersion(), pVersion);
	}
}
