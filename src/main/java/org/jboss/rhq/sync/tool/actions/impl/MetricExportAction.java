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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.actions.impl.impex.ConfigurationRepo;
import org.jboss.rhq.sync.tool.actions.impl.impex.JsonIO;
import org.jboss.rhq.sync.tool.model.impex.MetricSchedule;
import org.jboss.rhq.sync.tool.model.impex.Schedule;
import org.jboss.rhq.sync.tool.query.MetricResourceQuery;
import org.jboss.rhq.sync.tool.query.MetricResourceQueryImpl;
import org.jboss.rhq.sync.tool.query.ResourceQuery;
import org.jboss.rhq.sync.tool.query.ResourceQueryImpl;
import org.rhq.core.domain.measurement.MeasurementSchedule;
import org.rhq.core.domain.resource.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/3/11
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricExportAction extends AbstractJONAction {
    @Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
        String resourceName = (String) values.get("resourceName");
        if (resourceName == null) {
            throw new IllegalArgumentException("no resourcename defined as a parameter. cannot perform export");
        }

        ResourceQuery query = new ResourceQueryImpl();
        List<Resource> resources = query.getResourceByName(resourceName);
        if (resources.size() == 0) {
            logger.error("exiting: NO Resources found for resource name : " + resourceName);
            return null;
        }
        MetricResourceQuery q = new MetricResourceQueryImpl();
        for (Resource resource : resources) {
            List<MeasurementSchedule> tty = q.findAllSchedulesForResource(resource.getId());
            exportResourceSchedule(tty, resource);

            System.out.println("");
        }


        return null;
    }

    private void exportResourceSchedule(List<MeasurementSchedule> measurementSchedules, Resource resource) {
        ConfigurationRepo repo = new JsonIO();
        MetricSchedule metricScheduleExport = new MetricSchedule();

        List<String> resourceNames = new ArrayList<String>();
        resourceNames.add(resource.getName());
        metricScheduleExport.setResourceType(resource.getResourceType().getName());
        metricScheduleExport.setResourceName(resourceNames);


        List<Schedule> scheduleList = new ArrayList<Schedule>();
        for (MeasurementSchedule measurementSchedule : measurementSchedules) {
            // only add measurement types
            // if(  measurementSchedule.getDefinition().getDataType().equals(DataType.MEASUREMENT))
            scheduleList.add(new Schedule(measurementSchedule.getDefinition().getDataType().toString(), measurementSchedule.isEnabled(), measurementSchedule.getDefinition().getDisplayName(), measurementSchedule.getDefinition().getName(), measurementSchedule.getInterval()));
        }
        metricScheduleExport.setSchedules(scheduleList);


        List<MetricSchedule> list = new ArrayList<MetricSchedule>();
        list.add(metricScheduleExport);
        try {
            String filename = resource.getName().trim().replace(" ", "_").replace(":", "_");
            repo.saveMetric(list, "export_" + filename + ".json");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] args) {

        MetricExportAction metricExportAction = new MetricExportAction();
        Map<String, String> vals = new HashMap<String, String>();
        // vals.put("resourceName","jbossAgent1");
        // vals.put("resourceName","localhost.localdomain:1099 default");
        //  vals.put("resourceName","localhostdddd.localdomain:1099 default");
        if (args.length == 0) {
            logger.error("NO RESOURCE ARGUMENT DEFINED. PLEASE PASS A RESOURCE NAME TO EXPORT");
            System.exit(-1);
        }
        vals.put("resourceName", args[0]);

        try {
            metricExportAction.perform(vals);
        } catch (RuntimeException e) {
            logger.error("A error occured on export. could not perform export:[ " + e.getMessage() + "]");
            e.printStackTrace();
            System.exit(-1);

        }
    }

    private static Logger logger = Logger
            .getLogger(MetricExportAction.class);

}
