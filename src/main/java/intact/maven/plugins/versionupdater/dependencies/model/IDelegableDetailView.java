package intact.maven.plugins.versionupdater.dependencies.model;

public interface IDelegableDetailView 
{
	Class<? extends IDetailView> getDetailViewClass();
}
