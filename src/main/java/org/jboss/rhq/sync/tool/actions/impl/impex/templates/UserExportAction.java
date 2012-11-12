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

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.LoginConfiguration;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.actions.impl.AbstractJONExportAction;
import org.jboss.rhq.sync.tool.query.ResourceQueryImpl;
import org.jboss.rhq.sync.tool.query.SubjectsAndRolesQueryImpl;
import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.resource.Resource;


/**
 * @author Ivan McKinley  - <imckinle@redhat.com>
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class UserExportAction extends AbstractJONExportAction<List<Subject>> {

    public static final String USER_EXPORT_OUTPUT_FILENAME = "user.export.filename";
    private static Logger logger = Logger.getLogger(UserExportAction.class);

    public UserExportAction(LoginConfiguration loginConfiguration,
            BaseRemote baseRemote) {
        super(loginConfiguration, baseRemote);
        logger.debug("Connecting to JON Server using:" + loginConfiguration);
    }

    @Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
        setFilename(values, USER_EXPORT_OUTPUT_FILENAME);
        return createResultReport(saveOnFile(getAllUsers(), values));
    }

    //FIXME: Should be move into a more appropriate class
    public List<Resource> getAll() {
        return new ResourceQueryImpl().getResourceByFilter(null);
    }

    @Override
    protected JonActionResult.JonActionResultType createResultReport(List<Subject> subjects) {
        logger.debug("Exporting Subjects:");
        for ( Subject subject : subjects ) {
            logger.debug(subject);
        }
        return JonActionResult.JonActionResultType.SUCCESS;
    }

    private List<Subject> getAllUsers() {
        return new SubjectsAndRolesQueryImpl(baseRemote).getAllUsers();
    }
}
