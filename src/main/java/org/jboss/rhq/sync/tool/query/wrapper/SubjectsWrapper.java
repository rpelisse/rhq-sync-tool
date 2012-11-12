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

package org.jboss.rhq.sync.tool.query.wrapper;

import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.criteria.Criteria;
import org.rhq.core.domain.criteria.SubjectCriteria;
import org.rhq.core.domain.util.PageList;
import org.rhq.enterprise.server.auth.SubjectManagerRemote;


public class SubjectsWrapper extends AbstractQueryWrapper<Subject> {

    private SubjectManagerRemote subjectManager;
	
	public SubjectsWrapper(SubjectManagerRemote subjectManagerRemote, Subject subject) {
		super(subject);
		this.subjectManager = subjectManagerRemote;
	}
	
	@Override
	public PageList<Subject> doPagedQuery(Criteria criteria) {
		return ( criteria instanceof SubjectCriteria ) ? subjectManager.findSubjectsByCriteria(this.subject, (SubjectCriteria)criteria) : invalidCriteriaInstance(criteria);
	}
}
