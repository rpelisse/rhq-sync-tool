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

package org.jboss.rhq.sync.tool.actions.impl.impex.templates;

import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.query.SubjectsAndRolesQueryImpl;
import org.jboss.rhq.sync.tool.util.DomainCollectionsUtils;
import org.jboss.rhq.sync.tool.util.LogUtils;
import org.rhq.core.domain.auth.Subject;


/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class SubjectImporter implements ImportStrategy<Subject> {

    private static Logger logger = Logger.getLogger(SubjectImporter.class);
	
	@Override
	public Map<String, Subject> retrieveExistingItems(
			Map<String, Subject> providedItems) {
		// 1. Gather the list of existing user, and send only the list of new user to identifiy 
		//    (paged operation, number of remote call will depends on the result)
		// 2. Indexed the result
		return DomainCollectionsUtils.indexSubjectByName(new SubjectsAndRolesQueryImpl().retrievedExistingSubjectsAmong(providedItems.values()));
	}

	@Override
	public Map<String, Subject> determineItemsToCreate(
			Map<String, Subject> providedSubjects,
			Map<String, Subject> existingSubjects) {
		Map<String,Subject> subjectsToCreate = DomainCollectionsUtils.removeExistingItemsFromProvided(existingSubjects,providedSubjects);
		if ( logger.isDebugEnabled()) {
			logger.debug("Subjects to create on JON server:");
			LogUtils.logSubjects(logger, subjectsToCreate.values());
	    }
		return subjectsToCreate;
	}

	@Override
	public Map<String, Subject> importItem(Map<String, Subject> itemsToCreate) {
		return DomainCollectionsUtils.indexSubjectByName(new SubjectsAndRolesQueryImpl().addAlllUsers(itemsToCreate.values()));
	}


}
