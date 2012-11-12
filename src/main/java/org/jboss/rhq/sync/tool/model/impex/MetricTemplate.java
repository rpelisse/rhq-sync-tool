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

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 12/3/11
 * Time: 10:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricTemplate {

    private boolean defaultOn;
    private String name;
    private long defaultInterval;
    private boolean perMinute;
    private String description;

    private String measurementCategory;

    public long getDefaultInterval() {
        return defaultInterval;
    }

    public boolean isDefaultOn() {
        return defaultOn;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public boolean isPerMinute() {
        return perMinute;
    }

    public String getMeasurementCategory() {
        return measurementCategory;
    }

    public void setMeasurementCategory(String measurementCategory) {
        this.measurementCategory = measurementCategory;
    }

    public void setDefaultOn(boolean defaultOn) {
        this.defaultOn = defaultOn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultInterval(long defaultInterval) {
        this.defaultInterval = defaultInterval;
    }

    public void setPerMinute(boolean perMinute) {
        this.perMinute = perMinute;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
