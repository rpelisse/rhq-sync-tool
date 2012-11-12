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

package org.jboss.rhq.sync.tool.actions.impl;

import java.util.List;
import java.util.Map;

import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.query.JbossAsResourceQuery;
import org.jboss.rhq.sync.tool.query.ResourceQueryImpl;
import org.rhq.core.domain.criteria.MeasurementDefinitionCriteria;
import org.rhq.core.domain.criteria.MeasurementScheduleCriteria;
import org.rhq.core.domain.measurement.DataType;
import org.rhq.core.domain.measurement.MeasurementSchedule;
import org.rhq.core.domain.resource.Resource;
import org.rhq.enterprise.server.measurement.MeasurementScheduleManagerRemote;


/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 6/23/11
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricCollectioAction extends AbstractJONAction {
    @Override

    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
        MeasurementDefinitionCriteria measure = new MeasurementDefinitionCriteria();
        JbossAsResourceQuery query = new ResourceQueryImpl();
        measure.addFilterDataType(DataType.MEASUREMENT);
        measure.addFilterResourceTypeName(ResourceQueryImpl.ResourceType.JBOSSAS_RESOURCE_TYPE.resourceName);

        MeasurementScheduleManagerRemote measurementScheduleManagerRemote = baseRemote.getMeasurementScheduleManagerRemote();

        JbossAsResourceQuery jboss = new ResourceQueryImpl();
        List<Resource> jbossas = jboss.getJBossAS("", "localhost.localdomain:1099 default", "");
        for (Resource resource : jbossas) {
            MeasurementScheduleCriteria criteria = new MeasurementScheduleCriteria();
            //criteria.addFilterResourceId(resource.getSId());
            criteria.setPaging(0, 0);
            criteria.addFilterResourceId(resource.getId());
            resource = query.getResource(resource.getId());

            List<MeasurementSchedule> l = measurementScheduleManagerRemote.findSchedulesByCriteria(baseRemote.getSubject(), criteria);
            int[] scheduler = new int[l.size()];
            for (int i = 0; i < l.size(); i++) {
                MeasurementSchedule measurementSchedule = l.get(i);
                //      measurementSchedule.set
                scheduler[i] = measurementSchedule.getDefinition().getId();
            }

            measurementScheduleManagerRemote.disableSchedulesForResource(baseRemote.getSubject(), resource.getId(), scheduler);
            System.out.println("");
        }

        System.out.println("hello");
        System.out.println("asdsadasddsa");
        return null;  //To change body of implemented methods use File | Settings | File Templates.

    }

    /**
     * todo  action shoul load a list of jboss servers. and apply the measurement schedules for that jboss instance
     *
     * @param args
     */
    public static void main(String[] args) {
        MetricCollectioAction co = new MetricCollectioAction();
        co.perform(null);
    }
}
