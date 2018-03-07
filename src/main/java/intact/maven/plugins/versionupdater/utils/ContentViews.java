package intact.maven.plugins.versionupdater.utils;

public class ContentViews 
{
	public static class Normal{};
	
	public static class Release extends Normal{};	
	
	public static class Bom extends Normal{};	
	
	public static class BomRelease extends Normal{};
	
	public static class AddExclusionEntry extends Normal{};

}
