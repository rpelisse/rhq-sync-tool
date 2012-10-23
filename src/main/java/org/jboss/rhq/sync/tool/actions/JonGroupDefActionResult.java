package org.jboss.rhq.sync.tool.actions;

import java.util.HashSet;
import java.util.Set;

import org.rhq.core.domain.resource.group.GroupDefinition;

public class JonGroupDefActionResult extends JonActionResult {

	private Set<GroupDefinition> groupDefinitions = new HashSet<GroupDefinition>(0);

	public Set<GroupDefinition> getGroupDefinitions() {
		return groupDefinitions;
	}

	public void setGroupDefinitions(Set<GroupDefinition> groups) {
		this.groupDefinitions = groups;
	}
	
}
