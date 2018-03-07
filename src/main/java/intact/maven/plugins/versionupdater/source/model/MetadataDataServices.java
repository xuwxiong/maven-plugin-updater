package intact.maven.plugins.versionupdater.source.model;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;

@ObjectCreate(pattern = "edmx:Edmx/edmx:DataServices/Schema")
public class MetadataDataServices implements Iterable<MetadataEntry>
{
	private Map<String,MetadataEntry> entries = new HashMap<String,MetadataEntry>();
	
	@SetNext
	public void addEntry(MetadataEntry pEntry) {
		entries.put(pEntry.getEntryId(), pEntry);
	}
	
	public Set<String> getEntriesName()
	{
		return entries.keySet();
	}
	
	public MetadataEntry getEntry(String pEntryId)
	{
		return entries.get(pEntryId);
	}	

	@Override
	public Iterator<MetadataEntry> iterator() 
	{
		Iterator<MetadataEntry> entriesIterator = Collections.<MetadataEntry>emptySet().iterator();
		
		if (null != entries)
		{
			Collection<MetadataEntry> values = entries.values();
			
			if (null != values)
			{
				entriesIterator = values.iterator();
			}
		}
		
		return entriesIterator;
	}
}
