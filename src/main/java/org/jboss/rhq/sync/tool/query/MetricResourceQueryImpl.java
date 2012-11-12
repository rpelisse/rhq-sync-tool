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
import org.rhq.core.domain.criteria.MeasurementDefinitionCriteria;
import org.rhq.core.domain.criteria.MeasurementScheduleCriteria;
import org.rhq.core.domain.measurement.DataType;
import org.rhq.core.domain.measurement.MeasurementDefinition;
import org.rhq.core.domain.measurement.MeasurementSchedule;
import org.rhq.core.domain.resource.Resource;
import org.rhq.enterprise.server.measurement.MeasurementDefinitionManagerRemote;
import org.rhq.enterprise.server.measurement.MeasurementScheduleManagerRemote;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 6/24/11
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class MetricResourceQueryImpl extends BaseResourceQuery implements MetricResourceQuery {

    private MeasurementDefinitionManagerRemote measurementDefinitionManagerRemote;
    private MeasurementScheduleManagerRemote measurementScheduleManagerRemote;

    private static Logger logger = Logger
            .getLogger(MetricResourceQueryImpl.class);

    public MetricResourceQueryImpl() {
        measurementDefinitionManagerRemote = baseRemote.getMeasurementDefinitionManagerRemote();
        measurementScheduleManagerRemote = baseRemote.getMeasurementScheduleManagerRemote();
    }

    @Override
    public List<MeasurementDefinition> findAllJbossMetric(String platform) {
        MeasurementDefinitionCriteria measure = new MeasurementDefinitionCriteria();

        //  criteria.addF
        //        measure.
        //       measure.addFilterDataType(DataType.MEASUREMENT);
        measure.addFilterResourceTypeName(ResourceQueryImpl.ResourceType.JBOSSAS_RESOURCE_TYPE.resourceName);
        //  List<MeasurementDefinition> mylist = measurementDefinitionManagerRemote.findMeasurementDefinitionsByCriteria(this.getSubject(),measure);
        //return mylist;
        return null;
    }

    @Override
    public List<MeasurementSchedule> findAllSchedulesForResource(int resourceId) {
        MeasurementScheduleCriteria criteria = new MeasurementScheduleCriteria();
        //criteria.addFilterResourceId(resource.getSId());
        criteria.setPaging(0, 0);
        criteria.addFilterResourceId(resourceId);
        return measurementScheduleManagerRemote.findSchedulesByCriteria(baseRemote.getSubject(), criteria);
    }

    @Override
    public MeasurementSchedule findMeasurementSchedule(String name, String displayName, String dataType, int resourceid) {
        MeasurementScheduleCriteria criteria = new MeasurementScheduleCriteria();
        //criteria.addFilterResourceId(resource.getSId());
        criteria.setPaging(0, 0);
        criteria.addFilterResourceId(resourceid);
        Resource resource = getResource(resourceid);
        MeasurementDefinition measurementDefinition = getMeasurementDef(name, displayName, dataType, resource.getResourceType().getName());
        if (measurementDefinition == null) {
            logger.error("no measurement definition found");
            return null;
        }
        criteria.addFilterDefinitionIds(getMeasurementDef(name, displayName, dataType, resource.getResourceType().getName()).getId());
        List<MeasurementSchedule> measurementSchedules = measurementScheduleManagerRemote.findSchedulesByCriteria(baseRemote.getSubject(), criteria);
        if (measurementSchedules.size() > 1)
            throw new RuntimeException("more than one measurementschedule found");

        return measurementSchedules.get(0);
    }

    @Override
    public void updateMeasurementSchedule(MeasurementSchedule measurementSchedule) {
        measurementScheduleManagerRemote.updateSchedule(baseRemote.getSubject(), measurementSchedule);
    }

    private MeasurementDefinition getMeasurementDef(String name, String displayName, String type, String resourceTypeName) {
        logger.debug("get mesasurement definition for [" + name + "," + displayName + "," + type + "," + resourceTypeName + "]");
        MeasurementDefinitionCriteria measure = new MeasurementDefinitionCriteria();
        measure.addFilterDisplayName(displayName);
        measure.addFilterName(name);
        measure.setStrict(true);
        measure.addFilterResourceTypeName(resourceTypeName);
        measure.addFilterDataType(DataType.valueOf(type));
        List<MeasurementDefinition> mylist = measurementDefinitionManagerRemote.findMeasurementDefinitionsByCriteria(baseRemote.getSubject(), measure);

        if (mylist.size() > 1)
            throw new RuntimeException("found more than one  MeasurementDefinition [" + name + "] ");
        if (mylist.size() == 0) {
            logger.error("no schedule definition found [" + name + "," + displayName + "," + type + "," + resourceTypeName + "]");
            return null;

        } else
            return mylist.get(0);
    }
}
