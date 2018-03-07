package intact.maven.plugins.versionupdater;

import intact.maven.plugins.versionupdater.source.WikiPageManager;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "getWikiList", requiresProject = false, requiresOnline = true, requiresDirectInvocation = true)
public class AvailableWikiList extends AbstractMojo
{
	@Override
	public void execute()
	{		
		try
		{			
			getLog().info("Getting Wiki List");
						
			List<String> wikiList = WikiPageManager.getWikiList();
						
			if (null != wikiList)
			{
				for(String listId:wikiList)
				{
					System.out.println(listId);
				}
				
				getLog().info("Wiki List done.");				
			}
			else
			{
				getLog().info("Can't get Wiki List.");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		AvailableWikiList upPrj = new AvailableWikiList();
		
		upPrj.execute();		
	}
}
