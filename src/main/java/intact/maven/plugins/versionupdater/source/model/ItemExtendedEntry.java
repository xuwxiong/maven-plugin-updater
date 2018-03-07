package intact.maven.plugins.versionupdater.source.model;
import intact.maven.plugins.versionupdater.utils.ContentViews;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@ObjectCreate(pattern = "feed/entry")
@JsonInclude(Include.NON_EMPTY)
public class ItemExtendedEntry extends ItemEntry
{	
	@JsonView(ContentViews.Bom.class)
	@JsonProperty(value="BOMTarget")
	private List<ItemBomTarget> bomTargets = new ArrayList<ItemBomTarget>();
	
	@JsonView(ContentViews.Bom.class)
	@JsonProperty(value="Exclusions")
	private List<ItemExclusionEntry> exclusions = new ArrayList<ItemExclusionEntry>();
		
	@SetNext(fireOnBegin=false)
	public void addSubItem(SubItem item) {
		if("http://schemas.microsoft.com/ado/2007/08/dataservices/related/BOMTarget".equals(item.getRel()) && item.getValue() != null) {
			ItemBomTarget bomTarget = new ItemBomTarget();
			bomTarget.setValue(item.getValue());
			
			addBomTarget(bomTarget);
		} else if("http://schemas.microsoft.com/ado/2007/08/dataservices/related/Exclusions".equals(item.getRel()) && item.getArtifactId() != null) {
			ItemExclusionEntry exclusion = new ItemExclusionEntry();
			exclusion.setId(item.getId());
			exclusion.setArtifactId(item.getArtifactId());
			exclusion.setGroupId(item.getGroupId());
			
			addExclusion(exclusion);
		}
	}
	
	public void addBomTarget(ItemBomTarget pBomTarget) 
	{
		if (null != pBomTarget)
		{
			bomTargets.add(pBomTarget);	
		}		
	}
	
	public void addExclusion(ItemExclusionEntry pExclusion) 
	{
		if (null != pExclusion)
		{
			exclusions.add(pExclusion);	
		}		
	}
	
	public boolean isNoBOM()
	{
		return bomTargets.isEmpty();
	}
	
	public boolean isWithinBOM(String pBomTarget)
	{
		return bomTargets.contains(new ItemBomTarget(pBomTarget));
	}
	
	public boolean isNoExclusion()
	{
		return exclusions.isEmpty();
	}
	
	public Collection<ItemExclusionEntry> getExclusions() {
		return exclusions;
	}
}
