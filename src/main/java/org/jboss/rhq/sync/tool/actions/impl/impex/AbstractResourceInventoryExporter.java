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

package org.jboss.rhq.sync.tool.actions.impl.impex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.LoginConfiguration;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.actions.impl.AbstractJONAction;
import org.jboss.rhq.sync.tool.actions.impl.AbstractJONExportAction;
import org.jboss.rhq.sync.tool.model.impex.BasicProperty;
import org.jboss.rhq.sync.tool.model.impex.ListProperty;
import org.jboss.rhq.sync.tool.model.impex.MapProperty;
import org.jboss.rhq.sync.tool.model.impex.ResourceInventoryConnection;
import org.jboss.rhq.sync.tool.model.impex.Schedule;
import org.jboss.rhq.sync.tool.query.MetricResourceQuery;
import org.jboss.rhq.sync.tool.query.MetricResourceQueryImpl;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.Property;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.measurement.MeasurementSchedule;
import org.rhq.core.domain.resource.Resource;
import org.rhq.enterprise.server.configuration.ConfigurationManagerRemote;


/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/11/11
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class
        AbstractResourceInventoryExporter extends AbstractJONAction {

	protected final String exportFilename;

    private final ConfigurationManagerRemote configurationManagerRemote;
    private static Logger logger = Logger.getLogger(AbstractResourceInventoryExporter.class);

    protected abstract List<Resource> findResourcesToExport(Map<String, String> values);

    public AbstractResourceInventoryExporter() {
        super();
        exportFilename = null;
        this.configurationManagerRemote = baseRemote.getConfigurationManagerRemote();
    }

    public AbstractResourceInventoryExporter(LoginConfiguration loginConfiguration, BaseRemote baseRemote, String exportFilename) {
    	super(loginConfiguration,baseRemote);
    	configurationManagerRemote = baseRemote.getConfigurationManagerRemote();
    	this.exportFilename = exportFilename;
	}

    private String getExportFilename(Map<String, String> values) {
        String filename = (String) values.get("fileName");
        if (filename == null) {
        	if ( exportFilename != null )
                filename = values.get(AbstractJONExportAction.WORKING_DIRECTORY_PROPERTY) + "/" + exportFilename;
        	else
        		throw new IllegalArgumentException("Missing filename to export resource");
        }
        return filename;
    }

	@Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
        List<Resource> resources = findResourcesToExport(values);
        if (resources.size() == 0) {
            logger.error("exiting: NO Resources found. Cannot export");
            return null;
        }
        String exportFilename = getExportFilename(values);
        try {
            saveResourcesConnectionInventory(resources, exportFilename);
        } catch (IOException e) {

            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException("could not save inventory for query. ");
        }
        return JonActionResult.JonActionResultType.SUCCESS;
    }

    /**
     * create amapProperty
     *
     * @param propertyMap
     * @return
     */
    private MapProperty genMapProperty(PropertyMap propertyMap) {
        MapProperty mapProperty = new MapProperty(propertyMap.getName());
        for (Map.Entry<String, Property> stringPropertyEntry : propertyMap.getMap().entrySet()) {
            mapProperty.put(stringPropertyEntry.getKey(), determineProperty(stringPropertyEntry.getValue()));
        }
        return mapProperty;
    }

    /**
     * create a listProperty
     *
     * @param propertyList
     * @return
     */
    private ListProperty genListProperty(PropertyList propertyList) {
        logger.debug("generating property list");
        List<Property> properties = propertyList.getList();
        ListProperty listProperty = new ListProperty(propertyList.getName());

        for (Property property : properties) {
            listProperty.add(determineProperty(property));
        }
        return listProperty;
    }

    /**
     * firgures out and returns a full load object of type basicproperty based on the param property
     *
     * @param property
     * @return
     */
    private BasicProperty determineProperty(Property property) {
        if (property.getName().equals("runtimeSettings")) {
            System.out.println("ddddd");
        }
        if (property instanceof PropertyList) {
            return genListProperty((PropertyList) property);
        } else if (property instanceof PropertyMap) {
            return genMapProperty((PropertyMap) property);
        } else if (property instanceof PropertySimple) {
            PropertySimple propertySimple = (PropertySimple) property;
            return new BasicProperty(propertySimple.getName(), propertySimple.getStringValue());
        } else {
            //throw new InvalidClassException(" property of unknow class found ");
        }
        return null;
    }

    private void saveResourcesConnectionInventory(List<Resource> resourceList, String filename) throws IOException {
        logger.debug(" saving resources to filename [" + filename + "]"
        );
        List<ResourceInventoryConnection> resourceInventoryConnectionList = new ArrayList<ResourceInventoryConnection>();
        MetricResourceQuery metricResourceQuery = new MetricResourceQueryImpl();

        for (Resource resource : resourceList) {
            ResourceInventoryConnection resourceInventoryConnection = new ResourceInventoryConnection();
            resourceInventoryConnection.addName(resource.getName());
            resourceInventoryConnection.setResourceType(resource.getResourceType().getName());
            logger.debug("fetching plugin configuration for resource with id[" + resource.getId() + "], name=" + resource.getName());
            Configuration pluginConfiguration = configurationManagerRemote.getPluginConfiguration(getSubject(), resource.getId());
            logger.debug("fetching resource configuration for resource with id[" + resource.getId() + "], name=" + resource.getName());
            logger.debug("============= determining properties for plugin config =========================");
            for (Property property : pluginConfiguration.getProperties()) {

                resourceInventoryConnection.addInventoryConnectionProperty(determineProperty(property));
            }
            Configuration reConfiguration = configurationManagerRemote.getResourceConfiguration(getSubject(), resource.getId());

            logger.debug("============= determining properties resource configuration =========================");
            for (Property property : reConfiguration.getProperties()) {

                resourceInventoryConnection.addConfigurationProperty(determineProperty(property));
            }
            List<MeasurementSchedule> measurementSchedules = metricResourceQuery.findAllSchedulesForResource(resource.getId());

            for (MeasurementSchedule measurementSchedule : measurementSchedules) {
                // only add measurement types
                // if(  measurementSchedule.getDefinition().getDataType().equals(DataType.MEASUREMENT))
                resourceInventoryConnection.addSchedule(new Schedule(measurementSchedule.getDefinition().getDataType().toString(), measurementSchedule.isEnabled(), measurementSchedule.getDefinition().getDisplayName(), measurementSchedule.getDefinition().getName(), measurementSchedule.getInterval()));
            }
            resourceInventoryConnectionList.add(resourceInventoryConnection);

        }
        JsonIO j = new JsonIO(true);
        j.saveCollection(resourceInventoryConnectionList, filename);
    }


}
