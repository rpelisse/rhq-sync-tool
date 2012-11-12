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
 * Date: 7/3/11
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Schedule {
    public String scheduleName;
    public String scheduleDisplayName;

    public long updateInterval;
    public boolean enabled;
    public String dataType;


    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Schedule() {
    }

    public String getScheduleDisplayName() {
        return scheduleDisplayName;
    }

    public void setScheduleDisplayName(String scheduleDisplayName) {
        this.scheduleDisplayName = scheduleDisplayName;
    }

    public Schedule(String dataType, boolean enabled, String scheduleDisplayName, String scheduleName, long updateInterval) {
        this.dataType = dataType;
        this.enabled = enabled;
        this.scheduleDisplayName = scheduleDisplayName;
        this.scheduleName = scheduleName;
        this.updateInterval = updateInterval;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public long getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(long updateInterval) {
        this.updateInterval = updateInterval;
    }
}
