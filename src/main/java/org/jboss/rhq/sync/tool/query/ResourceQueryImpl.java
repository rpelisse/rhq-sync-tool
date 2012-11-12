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

import org.apache.log4j.Logger;
import org.rhq.core.domain.criteria.ResourceCriteria;
import org.rhq.core.domain.criteria.ResourceTypeCriteria;
import org.rhq.core.domain.resource.InventoryStatus;
import org.rhq.core.domain.resource.Resource;
import org.rhq.core.domain.util.PageList;
import org.rhq.core.domain.util.PageOrdering;
import org.rhq.enterprise.server.resource.ResourceTypeManagerRemote;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 3/9/11
 * Time: 4:40 PM
 * General ResourceQuery class to find anything related to a resource, eg server platform service  . currently support rhq query and jboss server.
 */
public class ResourceQueryImpl extends BaseResourceQuery implements JbossAsResourceQuery, AgentResoucreQuery {

    private static Logger logger = Logger.getLogger(ResourceQueryImpl.class);

    public enum ResourceType {
        JBOSSAS_RESOURCE_TYPE("JBossAS Server", "JBossAS"),
        RHQAGENT_RESOURCE_TYPE("RHQ Agent", "RHQAgent"),
        CPU_RESOURCE_TYPE("CPU", "CPU"),
        LINUX_RESOURCE_TYPE("Linux", "Platforms");
        public final String resourceName;
        final String pluginName;

        private ResourceType(String resourceName, String pluginName) {
            this.resourceName = resourceName;
            this.pluginName = pluginName;
        }


        @Override
        public String toString() {
            return "name:" + resourceName + ",plugin:" + pluginName;
        }
    }

    private final ResourceTypeManagerRemote resourceTypeManagerRemote;


    public ResourceQueryImpl() {
        super();

        resourceTypeManagerRemote = baseRemote.getResourceTypeManagerRemote();

    }

    /**
     * util to show all resource types
     */
    @SuppressWarnings("unused")
	private void printAllResourceTypes() {
        PageList<org.rhq.core.domain.resource.ResourceType> gg = resourceTypeManagerRemote.findResourceTypesByCriteria(baseRemote.getSubject(), new ResourceTypeCriteria());
        for (org.rhq.core.domain.resource.ResourceType resourceType : gg) {
            logger.debug("name:" + resourceType.getName() + ", plugin:" + resourceType.getPlugin());
        }
    }

    private List<Resource> executeQuery(ResourceCriteria resourceCriteria) {
        return resourceManagerRemote.findResourcesByCriteria(baseRemote.getSubject(), resourceCriteria);
    }


    @Override
    public List<Resource> getAllNewJBossAS(String agentName, String profileName, String version) {
        ResourceCriteria resourceCriteria = new ResourceCriteria();
        resourceCriteria.addFilterInventoryStatus(InventoryStatus.NEW);
        resourceCriteria.addSortParentResourceName(PageOrdering.ASC);
        resourceCriteria.addSortName(PageOrdering.ASC);
        addJbossDetailsToCriteria(agentName, profileName, version, resourceCriteria);
        return executeQuery(resourceCriteria);

    }


    @Override
    public List<Resource> getJBossAS(String platformName, String profileName, String version) {
        ResourceCriteria resourceCriteria = new ResourceCriteria();
        resourceCriteria.addSortParentResourceName(PageOrdering.ASC);
        resourceCriteria.addSortName(PageOrdering.ASC);
        addJbossDetailsToCriteria(platformName, profileName, version, resourceCriteria);
        return executeQuery(resourceCriteria);
    }


    public int getCpuCount(int linuxmachinbe) {
        ResourceCriteria resourceCriteria = new ResourceCriteria();
        resourceCriteria.setPaging(0, 0);
        resourceCriteria.addFilterParentResourceId(linuxmachinbe);
        resourceCriteria.addFilterResourceTypeName(ResourceType.CPU_RESOURCE_TYPE.resourceName);
        List<Resource> t = resourceManagerRemote.findResourcesByCriteria(baseRemote.getSubject(), resourceCriteria);
        return t.size();
    }


    @Override
    public List<Resource> getAllInventoriedJBossAS(String platformName, String profileName, String version) {
        ResourceCriteria resourceCriteria = new ResourceCriteria();
        resourceCriteria.setPaging(0, 0);
        resourceCriteria.addFilterInventoryStatus(InventoryStatus.COMMITTED);
        resourceCriteria.addSortParentResourceName(PageOrdering.ASC);
        resourceCriteria.addSortName(PageOrdering.ASC);

        addJbossDetailsToCriteria(platformName, profileName, version, resourceCriteria);
        return executeQuery(resourceCriteria);

    }

    private void addJbossDetailsToCriteria(String platformName, String profileName, String version, ResourceCriteria resourceCriteria) {
        resourceCriteria.addFilterResourceTypeName(ResourceType.JBOSSAS_RESOURCE_TYPE.resourceName);
        resourceCriteria.addFilterVersion(version);
        if (platformName != null && platformName.length() > 0)
            resourceCriteria.addFilterParentResourceName(platformName);
        if (profileName != null && profileName.length() > 0)
            resourceCriteria.addFilterName(profileName);
    }

    @Override
    public List<Resource> getRHQAgent(String platformName) {


        ResourceCriteria resourceCriteria = new ResourceCriteria();
        resourceCriteria.addFilterResourceTypeName(ResourceType.RHQAGENT_RESOURCE_TYPE.resourceName);
        resourceCriteria.addFilterPluginName(ResourceType.RHQAGENT_RESOURCE_TYPE.pluginName);
        resourceCriteria.addFilterParentResourceName("" + platformName);
        resourceCriteria.setStrict(true);


        //resourceTypeManagerRemote.getResourceTypeByNameAndPlugin(re)
        return executeQuery(resourceCriteria);
    }

    public List<Resource> getAllPlatforms() {
        ResourceCriteria resourceCriteria = new ResourceCriteria();
        // resourceCriteria.setPaging(0, 0);

        // resourceCriteria.addFilterInventoryStatus(InventoryStatus.COMMITTED);
        resourceCriteria.addSortParentResourceName(PageOrdering.ASC);
        resourceCriteria.addSortName(PageOrdering.ASC);

        resourceCriteria.addFilterResourceTypeName(ResourceType.LINUX_RESOURCE_TYPE.resourceName);
        // resourceCriteria.setStrict(true);
        return executeQuery(resourceCriteria);

    }

    @Override
    public List<Resource> getAllInventoriedRHQAgents(String platformName) {
        ResourceCriteria resourceCriteria = new ResourceCriteria();
        resourceCriteria.setPaging(0, 0);

        resourceCriteria.addFilterInventoryStatus(InventoryStatus.COMMITTED);
        resourceCriteria.addSortParentResourceName(PageOrdering.ASC);
        resourceCriteria.addSortName(PageOrdering.ASC);
        resourceCriteria.fetchParentResource(true);

        addRHQDetailsToCriteria(platformName, resourceCriteria);
        resourceCriteria.setStrict(true);
        return executeQuery(resourceCriteria);

    }

    private void addRHQDetailsToCriteria(String platformName, ResourceCriteria resourceCriteria) {
        resourceCriteria.addFilterResourceTypeName(ResourceType.RHQAGENT_RESOURCE_TYPE.resourceName);
        //  resourceCriteria.addFilterVersion(version);
        if (platformName != null && platformName.length() > 0)
            resourceCriteria.addFilterParentResourceName(platformName);
    }


}
