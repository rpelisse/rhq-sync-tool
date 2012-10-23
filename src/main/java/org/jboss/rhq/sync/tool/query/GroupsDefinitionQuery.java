/**
 * 
 */
package org.jboss.rhq.sync.tool.query;

import java.util.Set;

import org.rhq.core.domain.resource.group.GroupDefinition;

/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public interface GroupsDefinitionQuery {

	public Set<GroupDefinition> exportAllGroupDefinitions();
	
	public void importAllGroups(Set<GroupDefinition> groupDefinitions);
}
