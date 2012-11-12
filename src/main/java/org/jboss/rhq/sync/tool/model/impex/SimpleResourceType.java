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
 * Date: 1/17/12
 * Time: 11:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleResourceType {
     private  String resourceTypeName;
    private String resourceTypePlugin;
    private String resourceCategory;
    private List<MetricTemplate> metricTemplates;

    public String getResourceCategory() {
        return resourceCategory;
    }

    public List<MetricTemplate> getMetricTemplates() {
        return metricTemplates;
    }

    public void setMetricTemplates(List<MetricTemplate> metricTemplates) {
        this.metricTemplates = metricTemplates;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public String getResourceTypePlugin() {
        return resourceTypePlugin;
    }

    public void setResourceTypePlugin(String resourceTypePlugin) {
        this.resourceTypePlugin = resourceTypePlugin;
    }

    public void addMetricTemplate(MetricTemplate metricTemplate) {
        if(metricTemplates==null){
            metricTemplates = new ArrayList<MetricTemplate>();
        }
           metricTemplates.add(metricTemplate);
    }

    public void setResourceCategory(String resourceCategory) {
        this.resourceCategory = resourceCategory;
    }
}
