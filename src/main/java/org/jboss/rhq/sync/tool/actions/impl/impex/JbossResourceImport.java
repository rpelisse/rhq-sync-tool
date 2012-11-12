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

package org.jboss.rhq.sync.tool.actions.impl.impex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.rhq.sync.tool.actions.JONAction;
import org.rhq.core.domain.resource.Resource;


/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/22/11
 * Time: 1:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class JbossResourceImport extends AbstractResourceImporter {

    public JbossResourceImport() {
        super();
        registerPropertyOverride(new PropertyValueOverrideLogPath("logFilePath"));
    }

    @Override
    public List<Resource> filterResourceToUpdate(List<Resource> resourceToUpdate) {
        return resourceToUpdate;
    }

    /**todo
     * we need to define a super class called get resources.
     * we can then override this and filter based on the resource;s parent platform.
     * and return a cleaned list of resources that will be updated
     *
     *
     * also. we need to add to the list of property pre processors. those things than change the value before its commited
     *
     * @param args
     */

    public static void main(String[] args) {
        JONAction rs = new JbossResourceImport();
        Map<String,String> map = new HashMap<String, String>();
        map.put("fileName", "json/jbossRHQ_EXPORT.json");
        rs.doAction(map);
    }

}
