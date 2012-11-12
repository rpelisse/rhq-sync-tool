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

package org.jboss.rhq.sync.tool.actions.impl.impex.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.LoginConfiguration;
import org.jboss.rhq.sync.tool.actions.JONAction;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.actions.impl.AbstractJONImportAction;
import org.jboss.rhq.sync.tool.actions.impl.impex.ConfigurationRepo;
import org.jboss.rhq.sync.tool.actions.impl.impex.JsonIO;
import org.jboss.rhq.sync.tool.model.impex.MetricTemplate;
import org.jboss.rhq.sync.tool.model.impex.SimpleResourceType;
import org.jboss.rhq.sync.tool.query.ResourceTypeQuery;
import org.jboss.rhq.sync.tool.query.ResourceTypeQueryImpl;
import org.rhq.core.domain.measurement.MeasurementDefinition;
import org.rhq.core.domain.resource.ResourceType;
import org.rhq.core.util.collection.ArrayUtils;
import org.rhq.enterprise.server.measurement.MeasurementScheduleManagerRemote;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 1/23/12
 * Time: 11:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricTemplateImportAction extends AbstractJONImportAction<List<SimpleResourceType>> implements ImportStrategy<List<SimpleResourceType>> {
    MeasurementScheduleManagerRemote measurementScheduleManager;

    public MetricTemplateImportAction() {
        measurementScheduleManager = baseRemote.getMeasurementScheduleManagerRemote();
    }

    public MetricTemplateImportAction(LoginConfiguration loginConfiguration, BaseRemote baseRemote) {
        super(loginConfiguration,baseRemote);
    }

    @Override
    protected List<SimpleResourceType> loadFromFile(String filename) {
        return getModel(filename);
    }

    @Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) {
        return doImport(loadFromFile(values.get(IMPORT_FILENAME)));
    }

    @Override
    protected JonActionResult.JonActionResultType doImport(List<SimpleResourceType> simpleResourceTypes) {
        List<Integer> listofMetricsToDisable = new ArrayList<Integer>();
        List<Integer> listofMetricsToEnable = new ArrayList<Integer>();
        Map<Long, List<Integer>> schedulesToUpdate = new HashMap<Long, List<Integer>>();
        for (SimpleResourceType simpleResourceType : simpleResourceTypes) {
            System.out.println(simpleResourceType.getResourceTypeName());
            ResourceTypeQuery q = new ResourceTypeQueryImpl();
            ResourceType resourceType = q.findResourceType(simpleResourceType.getResourceTypeName(), simpleResourceType.getResourceTypePlugin(), simpleResourceType.getResourceCategory());
            List<MetricTemplate> metricTemplates = simpleResourceType.getMetricTemplates();
            for (MeasurementDefinition measurementDefinition : resourceType.getMetricDefinitions()) {
                for (MetricTemplate metricTemplate : metricTemplates) {
                    if (measurementDefinition.getName().equals(metricTemplate.getName()) && measurementDefinition.getCategory().getName().equals(metricTemplate.getMeasurementCategory()) && measurementDefinition.isPerMinute() == metricTemplate.isPerMinute()) {

                        if (measurementDefinition.isDefaultOn() == metricTemplate.isDefaultOn()) {
                            logger.debug("Nothing to do. Current defaultOn is same as input [" + measurementDefinition.isDefaultOn() + "] for measurementDefintion " + measurementDefinition.getId());
                            //Break out of loop

                        } else if (metricTemplate.isDefaultOn()) {
                            logger.debug("Adding measurementDefinition id to enable [" + measurementDefinition.getId() + "]");
                            listofMetricsToEnable.add(measurementDefinition.getId());
                        } else {
                            logger.debug("Adding measurementDefinition id to disable [" + measurementDefinition.getId() + "]");
                            listofMetricsToDisable.add(measurementDefinition.getId());
                        }

                        // we still update the schedule
                        if (measurementDefinition.getDefaultInterval() == metricTemplate.getDefaultInterval()) {
                            logger.debug("schedules are equal. no need to update for measurementdefintion id " + measurementDefinition.getId());

                        } else {
                            logger.debug("update schedule for measurementDefintion with id " + measurementDefinition.getId() + " to " + metricTemplate.getDefaultInterval() + "ms");
                            if (schedulesToUpdate.containsKey(metricTemplate.getDefaultInterval())) {

                                schedulesToUpdate.get(metricTemplate.getDefaultInterval()).add(measurementDefinition.getId());
                            } else {
                                List<Integer> t = new ArrayList<Integer>();
                                t.add(measurementDefinition.getId());
                                schedulesToUpdate.put(metricTemplate.getDefaultInterval(), t);
                            }
                        }
                    }
                }
                logger.debug("Hello, I'm a lonely log output thats only use is as a break point. ");

            }
        }
        if (listofMetricsToDisable.size() > 0) {
            logger.info("Disabling " + listofMetricsToDisable.size() + " measurementDefinitions ");
            measurementScheduleManager.disableMeasurementTemplates(baseRemote.getSubject(), ArrayUtils.unwrapCollection(listofMetricsToDisable));
        } else
            logger.info("No changes detect for disabling metrics. No upda6te will be performed ");
        if (listofMetricsToEnable.size() > 0) {
            logger.info("Enabling " + listofMetricsToEnable.size() + " measurementDefinitions ");
            measurementScheduleManager.enableMeasurementTemplates(baseRemote.getSubject(), ArrayUtils.unwrapCollection(listofMetricsToEnable));
        } else
            logger.info("No changes detect for enabling metrics. No upda6te will be performed ");
        logger.info("Updating  " + schedulesToUpdate.size() + " measurementDefinitions schedules ");


        for (Map.Entry<Long, List<Integer>> next : schedulesToUpdate.entrySet()) {
            logger.info("Updating " + next.getValue().size() + " measurementDefinitions with the schedule " + next.getKey() + "ms");
            measurementScheduleManager.updateMeasurementTemplates(baseRemote.getSubject(), ArrayUtils.unwrapCollection(next.getValue()), next.getKey());
        }
        return JonActionResult.JonActionResultType.SUCCESS;
    }



    private void addToScheduleHashMap() {

    }

    private List<SimpleResourceType> getModel(String filename) {
        ConfigurationRepo repo = new JsonIO(false);
        return repo.getSimpleResourceType(filename);
    }

    private static Logger logger = Logger
            .getLogger(MetricTemplateImportAction.class);

    public static void main(String[] args) {

        JONAction metricExportAction = new MetricTemplateImportAction();
        Map<String, String> vals = new HashMap<String, String>();

        if (args.length == 0) {
            logger.error("NO FILE ARGUMENT DEFINED. PLEASE PASS A FILENAME TO IMPORT Metric templates");
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

    @Override
    public Map<String, List<SimpleResourceType>> retrieveExistingItems(Map<String, List<SimpleResourceType>> providedItems) {
        throw new UnsupportedOperationException("Not implemented.  Is not performant to get all items ");
    }

    @Override
    public Map<String, List<SimpleResourceType>> determineItemsToCreate(Map<String, List<SimpleResourceType>> providedItems, Map<String, List<SimpleResourceType>> retrieveExistingItems) {
        throw new UnsupportedOperationException("Metric templates do not support creation");
    }

    @Override
    public Map<String, List<SimpleResourceType>> importItem(Map<String, List<SimpleResourceType>> itemsToCreate) {
        throw new UnsupportedOperationException("Not implemented.");

    }
}
