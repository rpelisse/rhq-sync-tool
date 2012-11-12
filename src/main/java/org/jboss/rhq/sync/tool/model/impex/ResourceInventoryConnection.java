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

package org.jboss.rhq.sync.tool.model.impex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/12/11
 * Time: 12:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceInventoryConnection {

    //todo define object to holder to define the parents name. this will give an added bit of control
    List<String> resourceName;
    /**
     * http://docs.redhat.com/docs/en-US/JBoss_Operations_Network/2.4/html/API_Guides/domain/index.html?org/rhq/core/domain/measurement/package-summary.html
     * See org.rhq.core.domain.resource.ResourceType
     * JBossAS Server,RHQ Agent,Linux
     */
    String resourceType;

    List<BasicProperty> resourceInventoryProperties;
    List<BasicProperty> resourceConfiguration;
    List<Schedule> schedules;

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<BasicProperty> getResourceConfiguration() {
        return resourceConfiguration;
    }

    public void setResourceConfiguration(List<BasicProperty> resourceConfiguration) {
        this.resourceConfiguration = resourceConfiguration;
    }

    public ResourceInventoryConnection() {
        resourceInventoryProperties = new ArrayList<BasicProperty>();
        resourceConfiguration = new ArrayList<BasicProperty>();
        schedules = new ArrayList<Schedule>();
        resourceName = new ArrayList<String>();
    }

    public void addName(String name) {
        resourceName.add(name);
    }

    public List<BasicProperty> getResourceInventoryProperties() {
        return resourceInventoryProperties;
    }

    public void setResourceInventoryProperties(List<BasicProperty> resourceInventoryProperties) {
        this.resourceInventoryProperties = resourceInventoryProperties;
    }

    public List<String> getResourceName() {
        return resourceName;
    }

    public void setResourceName(List<String> resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void addInventoryConnectionProperty(BasicProperty basicProperty) {
        resourceInventoryProperties.add(basicProperty);
    }

    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
    }

    public void addConfigurationProperty(BasicProperty basicProperty) {
        if (basicProperty instanceof MapProperty)
            resourceConfiguration.add((MapProperty) basicProperty);
        else if (basicProperty instanceof ListProperty)
            resourceConfiguration.add((ListProperty) basicProperty);
        else
            resourceConfiguration.add(basicProperty);

    }


}
