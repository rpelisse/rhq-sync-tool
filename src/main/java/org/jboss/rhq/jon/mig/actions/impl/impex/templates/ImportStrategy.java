package org.jboss.rhq.jon.mig.actions.impl.impex.templates;

import java.util.Map;

public interface ImportStrategy<T> {
	
	public Map<String,T> retrieveExistingItems(Map<String, T> providedItems);

	public Map<String,T> determineItemsToCreate(Map<String, T> providedItems, Map<String,T> retrieveExistingItems);
	
	public Map<String,T> importItem( Map<String,T> itemsToCreate);
}
