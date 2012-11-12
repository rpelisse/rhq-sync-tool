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

import java.util.List;

import org.rhq.core.domain.resource.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/3/11
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ResourceQuery {
    /**
     * a string sperated by the following ->
     * example=   imckinle.csb-embedded=>RHQ Agent=>JVM
     *
     * @param string
     * @return
     */
    public Resource getResourceByParents(String string);

    public Resource getResource(int id);

    public List<Resource> getResourceByNameAndParentID(String name, int parentid);

    public Resource getResourceAll(int id);

    public List<Resource> getResourceByName(String name);
    
    public List<Resource> getResourceByFilter(String filter);

    public List<Resource> getResourceByNameAndType(String resourceName, String resourceType);

    List<Resource> getResourceByParentsAndType(String resourceName, String resourceType);

}
