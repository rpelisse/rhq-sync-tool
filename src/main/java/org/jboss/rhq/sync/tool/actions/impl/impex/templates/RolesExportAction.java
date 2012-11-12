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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.LoginConfiguration;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.actions.impl.AbstractJONExportAction;
import org.jboss.rhq.sync.tool.query.SubjectsAndRolesQueryImpl;
import org.jboss.rhq.sync.tool.query.UserQuery;
import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.authz.Role;


/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 11/1/11
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class RolesExportAction extends AbstractJONExportAction<List<Role>> {

    public static final String ROLES_EXPORT_OUTPUT_FILENAME = "roles.export.filename";

    public RolesExportAction(LoginConfiguration loginConfiguration,
			BaseRemote baseRemote) {
    	super(loginConfiguration,baseRemote);
	}

    /**
     * Default constructor - inherits connection to RHQ from parent class.
     */
    public RolesExportAction(){}

	@Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
    	setFilename(values, ROLES_EXPORT_OUTPUT_FILENAME);
        return createResultReport(saveOnFile(getAllRoles(), values));
    }

	private List<Role> getAllRoles() {
        UserQuery q = new SubjectsAndRolesQueryImpl();
        List<Role> resourceList = q.getallRoles();
        for (Role role : resourceList) {
            for (Subject sub : role.getSubjects()) {
                sub.setUserConfiguration(null);
            }
        }
        return resourceList;
	}

    public static void main(String[] args) {
        RolesExportAction a = new RolesExportAction();
        Map<String, String> values = new HashMap<String, String>(1);
        values.put(ROLES_EXPORT_OUTPUT_FILENAME, RolesExportAction.class.getSimpleName() + ".output.json");
        a.doAction(values);
        List<Role> roles = new ArrayList<Role>(1);
        Role role = new Role();
        role.setDescription("dummy role description");
        role.setName("dummy role" + System.currentTimeMillis());// Careful, has to be unique....
        roles.add(role);
        new SubjectsAndRolesQueryImpl().addAllRoles(roles);
    }

}
