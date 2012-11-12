/*
*
* RHQ Sync Tool
* Copyright (C) 2012-2013 Red Hat, Inc.
* All rights reserved.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License,
* version 2.1, as published by the Free Software Foundation.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License and the GNU Lesser General Public License
* for more details.
*
* You should have received a copy of the GNU General Public License
* and the GNU Lesser General Public License along with this program;
* if not, write to the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*
*/

package org.jboss.rhq.sync.tool.query;

import java.util.HashSet;
import java.util.Set;

import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.authz.Role;
import org.rhq.core.domain.criteria.ResourceGroupCriteria;
import org.rhq.core.domain.resource.group.GroupDefinition;
import org.rhq.core.domain.resource.group.ResourceGroup;
import org.rhq.core.domain.util.PageControl;
import org.rhq.core.domain.util.PageList;
import org.rhq.enterprise.server.resource.group.ResourceGroupManagerRemote;

/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class GroupsQueryImpl extends BaseResourceQuery implements GroupsDefinitionQuery {

	private ResourceGroupManagerRemote resourceGroupManager;	
	private static final int DEFAULT_PAGE_SIZE = 100;
	private static final int START_PAGE = 0;
	
	public GroupsQueryImpl() {
		resourceGroupManager = super.baseRemote.getResourceGroupManagerRemote();
	}
	
	private ResourceGroupManagerRemote getResourceGroupManager() {
		if ( resourceGroupManager == null ) {
			throw new IllegalStateException(this.getClass() + " has a null reference to " + ResourceGroupManagerRemote.class);
		}
		return resourceGroupManager;
	}

	// FIXME: never worked... wonder why.
	@SuppressWarnings("unused")
	private Set<GroupDefinition> getAllGroupsDefinitionByRole() {
		Subject subject = baseRemote.getSubject();
		int roleId = getRoleId(subject);
		// Is list 
		Set<GroupDefinition> definitions = new HashSet<GroupDefinition>();
		PageList<ResourceGroup> resources = null;
		do {
			resources = getResourceGroupManager().findResourceGroupsForRole(subject, roleId , new PageControl(START_PAGE,DEFAULT_PAGE_SIZE));
			for ( ResourceGroup group : resources ) {
				definitions.add(group.getGroupDefinition());
			}
		} while ( resources != null && ! resources.isEmpty() );
		return definitions;
	}
	
	private int getRoleId(Subject subject) {
		int roleId = 0;
		for ( Role role : subject.getRoles() ) {
			roleId = role.getId();
		}
		return roleId;
	}
	
	private Set<GroupDefinition> getAllGroupsByCriteria() {
		ResourceGroupCriteria criteria = new ResourceGroupCriteria();
		criteria.fetchGroupDefinition(true);
		
		Set<GroupDefinition> definitions = new HashSet<GroupDefinition>();
		PageList<ResourceGroup> resources = null;
		int pageId = START_PAGE;
		do {		
			criteria.setPaging(pageId, DEFAULT_PAGE_SIZE);
			resources = getResourceGroupManager().findResourceGroupsByCriteria(baseRemote.getSubject(), criteria);
			for ( ResourceGroup resource : resources ) {
				definitions.add(resource.getGroupDefinition());
			}
			pageId++;
		} while ( resources != null && ! resources.isEmpty() );
	
		return definitions;		
	}
	
	@Override
	public Set<GroupDefinition> exportAllGroupDefinitions() {
		// return getAllGroupsByRole(); // alternate implementation (not working)
		return getAllGroupsByCriteria();
	}

	@Override
	public void importAllGroups(Set<GroupDefinition> groupDefinitions) {
		ResourceGroup resGroup = new ResourceGroup("dummy2");
		for ( GroupDefinition groupDefinition : groupDefinitions )
			resGroup.setGroupDefinition(groupDefinition);
		baseRemote.getResourceGroupManagerRemote().createResourceGroup(baseRemote.getSubject(), resGroup);	
	}

}
