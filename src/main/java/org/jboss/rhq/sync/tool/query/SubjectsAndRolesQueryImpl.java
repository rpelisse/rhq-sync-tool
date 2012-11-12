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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.query.wrapper.RoleQueryWrapper;
import org.jboss.rhq.sync.tool.query.wrapper.SubjectsWrapper;
import org.jboss.rhq.sync.tool.util.LogUtils;
import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.authz.Role;
import org.rhq.core.domain.criteria.RoleCriteria;
import org.rhq.core.domain.criteria.SubjectCriteria;
import org.rhq.enterprise.server.auth.SubjectManagerRemote;
import org.rhq.enterprise.server.authz.RoleManagerRemote;


/**
 * 
 * @author Romain PELISSE - <belaran@redhat.com>
 * 
 */
public class SubjectsAndRolesQueryImpl extends BaseResourceQuery implements
		UserQuery {

	private SubjectManagerRemote subjectManager;
	private RoleManagerRemote roleManager;

	private static final Logger logger = Logger
			.getLogger(SubjectsAndRolesQueryImpl.class);

	public SubjectsAndRolesQueryImpl() {
		subjectManager = baseRemote.getSubjectManager();
		roleManager = baseRemote.getRoleManager();
	}

	public SubjectsAndRolesQueryImpl(BaseRemote baseRemote) {
		subjectManager = baseRemote.getSubjectManager();
		roleManager = baseRemote.getRoleManager();
	}

	public List<Subject> getAllUsers() {
		SubjectCriteria subjectCriteria = new SubjectCriteria();
		return subjectManager.findSubjectsByCriteria(baseRemote.getSubject(),
				subjectCriteria);
	}

	public List<Role> getallRoles() {
		RoleCriteria rc = new RoleCriteria();
		rc.fetchSubjects(true);
		return roleManager.findRolesByCriteria(baseRemote.getSubject(), rc);
	}

	public Collection<Role> findRolesByNames(Role role) {
		RoleCriteria criteria = new RoleCriteria();
		criteria.addFilterName(role.getName());
		criteria.setFiltersOptional(true);
		return new PagedResultsAssembler().gatherPaginatedResults(criteria, new RoleQueryWrapper(baseRemote.getRoleManager(), baseRemote.getSubject()));
	}
	
	@Override
	public Collection<Subject> retrievedExistingSubjectsAmong(
			Collection<Subject> subjects) {
		Collection<Subject> alreadyCreatedSubjects = new HashSet<Subject>(0);
		long startTime = System.currentTimeMillis();
		for (Subject subject : subjects)
			alreadyCreatedSubjects.addAll(new PagedResultsAssembler().gatherPaginatedResults(
					constructCriteria(subject.getName(), 0),
					new SubjectsWrapper(this.subjectManager, baseRemote
							.getSubject())));
		logger.warn("Poor querying implementation, watching execution time: "
				+ (System.currentTimeMillis() - startTime) + "ms, for "
				+ subjects.size() + " subjects.");
		LogUtils.logSubjects(logger, subjects);
		return alreadyCreatedSubjects;
	}

	@Override
	public Collection<Role> addAllRoles(Collection<Role> roles) {
		throw new UnsupportedOperationException("Not implemented.");
	}

	private SubjectCriteria constructCriteria(String username, int pageId) {
		SubjectCriteria criteria = new SubjectCriteria();
		// Simplification: we assume that username are unique
		criteria.addFilterName(username);
		criteria.setPaging(pageId, 20);
		criteria.fetchRoles(false);
		criteria.fetchConfiguration(false);
		return criteria;
	}

	private int[] gatherSubjectsIds(Collection<Subject> subjects) {
		int[] subjectIds = new int[subjects.size()];
		int i = 0;
		for ( Subject subject : subjects ) {
			subjectIds[i++] = subject.getId();
		}
		return subjectIds;
	}
	
	@Override
	public Role addRole(Role role) {
		return roleManager.createRole(baseRemote.getSubject(), role);
	}

	@Override
	public void addSubjectsToRole(Role role, Collection<Subject> subjects) {
		roleManager.addSubjectsToRole(baseRemote.getSubject(), role.getId(), gatherSubjectsIds(subjects));
	}
	
	@Override
	public Collection<Subject> addAlllUsers(Collection<Subject> subjects) {
		if (subjects == null)
			throw new IllegalStateException("No subject manager configured");

		if (subjects == null || subjects.isEmpty())
			return new HashSet<Subject>(0);

		Set<Subject> addedSubject = new HashSet<Subject>(subjects.size());
		for (Subject subject : subjects) {
			subject.setId(0);
			addedSubject.add(subjectManager.createSubject(
					baseRemote.getSubject(), subject));
		}
		return addedSubject;
	}
}
