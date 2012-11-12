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

import org.rhq.core.domain.criteria.ResourceTypeCriteria;
import org.rhq.core.domain.resource.ResourceCategory;
import org.rhq.core.domain.resource.ResourceType;
import org.rhq.enterprise.server.resource.ResourceTypeManagerRemote;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 1/17/12
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceTypeQueryImpl extends BaseResourceQuery implements ResourceTypeQuery {
    ResourceTypeManagerRemote resourceTypeManagerRemote = null;

    public ResourceTypeQueryImpl() {
        resourceTypeManagerRemote = baseRemote.getResourceTypeManagerRemote();

    }

    @Override
    public List<ResourceType> findAllResourceTypes() {

        ResourceTypeCriteria resourceTypeCrit = new ResourceTypeCriteria();
        resourceTypeCrit.fetchMetricDefinitions(true);
        List<ResourceType> resourceTypeList = resourceTypeManagerRemote.findResourceTypesByCriteria(baseRemote.getSubject(), resourceTypeCrit);

        return resourceTypeList;
    }

    @Override
    public ResourceType findResourceType(String name, String pluginType, String resourceCategory) {
        ResourceTypeCriteria crit = new ResourceTypeCriteria();
        crit.addFilterName(name);
        crit.addFilterCategory(ResourceCategory.valueOf(resourceCategory));
        crit.addFilterPluginName(pluginType);
        crit.fetchMetricDefinitions(true);
        crit.setStrict(true);
        List<ResourceType>  resourceTypes = resourceTypeManagerRemote.findResourceTypesByCriteria(baseRemote.getSubject(),crit);
        if(resourceTypes.size()>1){
            throw new IllegalStateException("More than one resource type found. Expected only one resource type") ;

        }
        return resourceTypes.get(0);

    }



}
