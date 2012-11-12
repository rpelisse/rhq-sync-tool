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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.LoginConfiguration;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.actions.impl.AbstractJONExportAction;
import org.jboss.rhq.sync.tool.actions.impl.impex.ConfigurationRepo;
import org.jboss.rhq.sync.tool.actions.impl.impex.JsonIO;
import org.jboss.rhq.sync.tool.model.impex.MetricTemplate;
import org.jboss.rhq.sync.tool.model.impex.SimpleResourceType;
import org.jboss.rhq.sync.tool.query.ResourceTypeQuery;
import org.jboss.rhq.sync.tool.query.ResourceTypeQueryImpl;
import org.rhq.core.domain.measurement.MeasurementDefinition;
import org.rhq.core.domain.resource.ResourceType;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 1/17/12
 * Time: 10:35 PM
 * CLASS dumps metric templates out to file
 */
public class MetricTemplateExportAction extends AbstractJONExportAction<List<SimpleResourceType>> {
    private static Logger logger = Logger.getLogger(MetricTemplateExportAction.class);

    public static final String METRICTEMPLATE_EXPORT_OUTPUT_FILENAME = "METRICTEMPLATE.export.filename";

    public MetricTemplateExportAction(LoginConfiguration loginConfiguration, BaseRemote baseRemote) {
        super(loginConfiguration, baseRemote);
    }

    public MetricTemplateExportAction() {
    }

    @Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
        setFilename(values, METRICTEMPLATE_EXPORT_OUTPUT_FILENAME);

        ResourceTypeQuery resourceTypeQuery = new ResourceTypeQueryImpl();
        List<ResourceType> resourceTypeList = resourceTypeQuery.findAllResourceTypes();
        List<SimpleResourceType> simpleResourceTypeList = new ArrayList<SimpleResourceType>();
        for (ResourceType resourceType : resourceTypeList) {
            SimpleResourceType simpleResourceType = new SimpleResourceType();
            simpleResourceType.setResourceTypeName(resourceType.getName());
            simpleResourceType.setResourceTypePlugin(resourceType.getPlugin());
            simpleResourceType.setResourceCategory(resourceType.getCategory().getName());

            for (MeasurementDefinition t : resourceType.getMetricDefinitions()) {

                MetricTemplate metricTemplate = new MetricTemplate();
                metricTemplate.setName(t.getName());
                metricTemplate.setDescription(t.getDescription());
                metricTemplate.setDefaultOn(t.isDefaultOn());
                metricTemplate.setDefaultInterval(t.getDefaultInterval());
                metricTemplate.setPerMinute(t.isPerMinute());

                metricTemplate.setMeasurementCategory(t.getCategory().getName());
                simpleResourceType.addMetricTemplate(metricTemplate);

            }
            simpleResourceTypeList.add(simpleResourceType);
        }
        ConfigurationRepo repo = new JsonIO(true);
        try {
            repo.saveCollection(simpleResourceTypeList, getFilename());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void main(String[] args) {
        System.out.println("starting");
        MetricTemplateExportAction t = new MetricTemplateExportAction();
        Map<String, String> values = new HashMap<String, String>(1);
        values.put(METRICTEMPLATE_EXPORT_OUTPUT_FILENAME, MetricTemplateExportAction.class.getSimpleName() + ".output.json");
        t.doAction(values);

        System.out.println("ending");
        System.out.println();
    }

}
