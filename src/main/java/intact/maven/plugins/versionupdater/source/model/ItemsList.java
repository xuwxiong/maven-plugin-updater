package intact.maven.plugins.versionupdater.source.model;
import intact.maven.plugins.versionupdater.dependencies.model.IDelegableDetailView;
import intact.maven.plugins.versionupdater.dependencies.model.IDetailView;
import intact.maven.plugins.versionupdater.utils.Utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.commons.digester3.annotations.rules.SetProperty;

/*<title type="text">BCO6D13</title>
<id>https://teamsites.iad.ca.inet/ifc/SOADevelopment/_vti_bin/listdata.svc/BCO6D13</id>
<updated>2016-01-12T20:47:40Z</updated>
<link rel="self" title="BCO6D13" href="BCO6D13" />*/

@ObjectCreate(pattern = "feed")
public class ItemsList implements Iterable<ItemExtendedEntry>, IDelegableDetailView
{
	@BeanPropertySetter( pattern = "feed/title" )
	private String title;
	
	@BeanPropertySetter(pattern = "feed/id")
	private String id;
	
	@BeanPropertySetter(pattern = "feed/updated")
	private String updated;
	
	@SetProperty(pattern = "feed/link",attributeName="title")
	private String link;
	
	private Map<String,ItemExtendedEntry> entries = new HashMap<String,ItemExtendedEntry>();
	
	private Map<String,ItemExtendedEntry> entriesByKey = new HashMap<String,ItemExtendedEntry>();
	
	public ItemsList() {}
	
	public ItemsList(ItemsList pItemsList,Collection<ItemExtendedEntry> pEntries) 
	{
		if (null != pItemsList)
		{
			setId(pItemsList.getId());
			setTitle(pItemsList.getTitle());
			setUpdated(pItemsList.getUpdated());
			setLink(pItemsList.getLink());
			
			if (CollectionUtils.isNotEmpty(pEntries))
			{
				for(ItemExtendedEntry entry:pEntries)
				{
					addEntry(entry);
				}
			}
			else
			{
				for(ItemExtendedEntry entry:pItemsList)
				{					
					addEntry(entry);
				}				
			}
			
		}
	}
	
	private void buildEntriesByKey()
	{
		for(ItemExtendedEntry entry:this)
		{					
			if (null != entry)
			{
				entriesByKey.put(Utils.getKey(entry), entry);
			}
		}
	}

	@Override
	public Iterator<ItemExtendedEntry> iterator() {
		return entries.values().iterator();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	
	public void setId(String id) {
		this.id = id;
	}

	public String getUpdated() {
		return updated;
	}

	
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	@SetNext
	public void addEntry(ItemExtendedEntry pEntry) 
	{
		if (null != pEntry)
		{
			entries.put(pEntry.getId(), pEntry);	
		}		
	}
	
	public ItemExtendedEntry getEntry(String pId)
	{
		return entries.get(pId);
	}
	
	public ItemExtendedEntry getEntryByKey(String pKey)
	{
		if (entriesByKey.isEmpty())
		{
			buildEntriesByKey();
		}
		
		return entriesByKey.get(pKey);
	}
	
	public boolean isEmpty()
	{
		return entries.isEmpty();
	}
	
	static class BOMPredicate implements Predicate
	{
		private final String bomTarget;
		
		public BOMPredicate(String pBomTarget)
		{
			this.bomTarget = pBomTarget;
		}

		@Override
		public boolean evaluate(Object object) 
		{
			boolean resultFlag = false;
			
			if (object instanceof ItemExtendedEntry)
			{
				ItemExtendedEntry entry = (ItemExtendedEntry) object;
				
				resultFlag = entry.isWithinBOM(bomTarget);
			}
			
			return resultFlag;
		}		
	}
	
	@SuppressWarnings("unchecked")
	public ItemsList getItemsWithinBOM(String pBomTarget)
	{
		ItemsList itemsWithinBOM = null;
		
		Collection<ItemExtendedEntry> entriesWithinBOM = CollectionUtils.select(entries.values(), new BOMPredicate(pBomTarget));
		
		itemsWithinBOM = new ItemsList(this, entriesWithinBOM);
		
		
		return itemsWithinBOM;
	}

	@Override
	public Class<? extends IDetailView> getDetailViewClass() 
	{
		return ItemProperties.class;
	}
}
