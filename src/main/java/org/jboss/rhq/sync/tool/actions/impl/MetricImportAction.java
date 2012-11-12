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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.actions.JONAction;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.actions.impl.impex.ConfigurationRepo;
import org.jboss.rhq.sync.tool.actions.impl.impex.JsonIO;
import org.jboss.rhq.sync.tool.model.impex.MetricSchedule;
import org.jboss.rhq.sync.tool.model.impex.Schedule;
import org.jboss.rhq.sync.tool.query.JbossAsResourceQuery;
import org.jboss.rhq.sync.tool.query.MetricResourceQuery;
import org.jboss.rhq.sync.tool.query.MetricResourceQueryImpl;
import org.jboss.rhq.sync.tool.query.ResourceQueryImpl;
import org.rhq.core.domain.measurement.MeasurementSchedule;
import org.rhq.core.domain.resource.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/3/11
 * Time: 10:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricImportAction extends AbstractJONAction {
    private static Logger logger = Logger
            .getLogger(MetricImportAction.class);

    @Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {

        String filename = (String) values.get("fileName");
        if (filename == null)
            throw new IllegalArgumentException("No filename defined");
        ConfigurationRepo repo = new JsonIO();
        List<MetricSchedule> metricSchedules = null;
        try {
            metricSchedules = repo.getMetric(filename);

        } catch (IOException e) {
            logger.error("could not load file");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //MeasurementScheduleManagerRemote measurementScheduleManagerRemote= baseRemote.getMeasurementScheduleManagerRemote();
        MetricResourceQuery metricResourceQuery = new MetricResourceQueryImpl();
        JbossAsResourceQuery query = new ResourceQueryImpl();
        if (metricSchedules != null)
            for (MetricSchedule metricSchedule : metricSchedules) {
                List<String> resourceNames = metricSchedule.getResourceName();
                for (String resourceName : resourceNames) {
                    List<Resource> resourceToUpdate = query.getResourceByNameAndType(resourceName, metricSchedule.getResourceType());
                    //Resources
                    for (Resource resource : resourceToUpdate) {

                        List<Schedule> schedulesToUpdate = metricSchedule.getSchedules();
                        for (Schedule scheduleToUpdate : schedulesToUpdate) {
                            MeasurementSchedule measurementSchedule = metricResourceQuery.findMeasurementSchedule(scheduleToUpdate.getScheduleName(), scheduleToUpdate.getScheduleDisplayName(), scheduleToUpdate.getDataType(), resource.getId());
                            if (measurementSchedule != null) {
                                measurementSchedule.setEnabled(scheduleToUpdate.isEnabled());
                                measurementSchedule.setInterval(scheduleToUpdate.getUpdateInterval());
                                logger.debug("updating metric schedule " + measurementSchedule.toString());
                                metricResourceQuery.updateMeasurementSchedule(measurementSchedule);
                            } else {
                                logger.error("could not update measurement schedule for resource" + resource.getName() + " " + scheduleToUpdate.getScheduleDisplayName() + "," + scheduleToUpdate.getScheduleName() + "," + scheduleToUpdate.getDataType());
                            }
                        }

                        //             measurementScheduleManagerRemote.updateSchedule();
                    }
                }

            }


        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public static void main(String[] args) {

        JONAction metricExportAction = new MetricImportAction();
        Map<String, String> vals = new HashMap<String, String>();
        // vals.put("resourceName","jbossAgent1");
        // vals.put("resourceName","localhost.localdomain:1099 default");
        // vals.put("resourceName","localhostdddd.localdomain:1099 default");
        if (args.length == 0) {
            logger.error("NO FILE ARGUMENT DEFINED. PLEASE PASS A FILENAME TO IMPORT SCHEDULES");
            System.exit(-1);
        }
        vals.put("fileName", args[0]);

        try {
            metricExportAction.doAction(vals);
        } catch (RuntimeException e) {
            logger.error("A error occured on IMPORT. could not perform IMPORT:[ " + e.getMessage() + "]");
            e.printStackTrace();
            System.exit(-1);

        }
    }

}
