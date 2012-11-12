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

import static org.jboss.rhq.sync.tool.util.PasswordUtil.decode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.LoginConfiguration;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.actions.impl.AbstractJONAction;
import org.jboss.rhq.sync.tool.model.impex.BasicProperty;
import org.jboss.rhq.sync.tool.model.impex.ListProperty;
import org.jboss.rhq.sync.tool.model.impex.MapProperty;
import org.jboss.rhq.sync.tool.model.impex.ResourceInventoryConnection;
import org.jboss.rhq.sync.tool.model.impex.Schedule;
import org.jboss.rhq.sync.tool.query.JbossAsResourceQuery;
import org.jboss.rhq.sync.tool.query.MetricResourceQuery;
import org.jboss.rhq.sync.tool.query.MetricResourceQueryImpl;
import org.jboss.rhq.sync.tool.query.ResourceQueryImpl;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.Property;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.measurement.MeasurementSchedule;
import org.rhq.core.domain.resource.InventoryStatus;
import org.rhq.core.domain.resource.Resource;
import org.rhq.enterprise.server.configuration.ConfigurationManagerRemote;


/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/14/11
 * Time: 11:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractResourceImporter extends AbstractJONAction {
    private static Logger logger = Logger.getLogger(AbstractResourceImporter.class);
    private ConfigurationManagerRemote configurationManagerRemote;
    private List<PropertyValueOverride> propertyValueOverrideList;

    public AbstractResourceImporter() {
        super();
        propertyValueOverrideList = new ArrayList<PropertyValueOverride>();
        this.configurationManagerRemote = baseRemote.getConfigurationManagerRemote();
    }

    public AbstractResourceImporter(LoginConfiguration login,BaseRemote remote) {
        super(login, remote);
        propertyValueOverrideList = new ArrayList<PropertyValueOverride>();
    }
    
    private Map<String, String> excludedPlatforms;

    private List<ResourceInventoryConnection> getModel(String filename) throws IOException {
        ConfigurationRepo repo = new JsonIO(false);
        return repo.getInventory(filename);

    }

    /**
     * list of resource that need to be updated. this is defined by model object but can be filtered by subclasses
     */
    public abstract List<Resource> filterResourceToUpdate(List<Resource> resourceToUpdate);

    @Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
        {
            String filename = (String) values.get("fileName");

            if (filename == null)
                throw new IllegalArgumentException("No filename defined");


            try {
                List<ResourceInventoryConnection> list = getModel(filename);

                JbossAsResourceQuery query = new ResourceQueryImpl();

                for (ResourceInventoryConnection resourceInventoryConnection : list) {
                    List<String> resources = resourceInventoryConnection.getResourceName();

                    for (String resourceName : resources) {

                        /**
                         * not optimal here but it works, it could be queried by optimizing the query.
                         */
                        List<Resource> resourcesToUpdate = null;
                        if (resourceName.contains(">")) {
                            resourcesToUpdate = filterResourceToUpdate(query.getResourceByParentsAndType(resourceName, resourceInventoryConnection.getResourceType()));
                        } else {
                            resourcesToUpdate = filterResourceToUpdate(query.getResourceByNameAndType(resourceName, resourceInventoryConnection.getResourceType()));
                        }
                        updateResources(resourcesToUpdate, resourceInventoryConnection);

                    }

                }
                System.out.println("dddd");
            } catch (IOException e) {
                logger.error("could not load file");
                e.printStackTrace();
                throw new RuntimeException(e);
            }


            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    /**
     * update all theses resource with defined spec
     *
     * @param resourceList
     * @param resourceInventoryConnection
     */
    protected void updateResources(List<Resource> resourceList, ResourceInventoryConnection resourceInventoryConnection) {
        for (
                Resource jbossResource
                : resourceList)

        {
            jbossResource = baseRemote.getResourceManager().getResource(getSubject(), jbossResource.getId());
            Resource parentPlatformResource = baseRemote.getResourceManager().getResource(getSubject(), jbossResource.getParentResource().getId());
            logger.info("Checking if this platform is to be excluded from processing.");
            if (isExcludedPlatform(parentPlatformResource.getName())) {
                logger.warn("Oh no, Platform[" + parentPlatformResource.getName() + "] is to be excluded from  update");
                logger.warn(" -- Excluding: profile=" + jbossResource.getName() + " on platform=" + parentPlatformResource.getName());
            } else {
                logger.info(" platform[" + parentPlatformResource.getName() + "] has not been excluded. Proceeding with inventory configuration update");
                logger.debug(" updating inventory configuration:" + jbossResource.getName() + ", platform " + parentPlatformResource.getName());
                alterInventoryConfigurations(jbossResource, resourceInventoryConnection);
                logger.debug("Finished inventory configuration update: profile=" + jbossResource.getName() + ", platform=" + parentPlatformResource.getName());
            }
        }

    }

    /**
     * method alters the configurartion of the passed jboss profile. Each profile must already be imported in jon otherwise it will do nothing (apart from a logging a warning)
     *
     * @param jboss
     * @param resourceInventoryConnection
     */
    private void alterInventoryConfigurations(Resource jboss, ResourceInventoryConnection resourceInventoryConnection) {
        /**
         * ONLY ALTER COMMITED DATA.
         */
        try {
            logger.debug(" ... alterInventoryConfigurations ");
            if (jboss.getInventoryStatus().equals(InventoryStatus.COMMITTED)) {
                logger.debug("inventory-status: committed");

                if (resourceInventoryConnection.getResourceInventoryProperties().size() > 0) {
                    logger.debug("update inventory connection configuration for resource with id[" + jboss.getId() + "], name= " + jboss.getName());
                    Configuration inventoryconfig = configurationManagerRemote.getPluginConfiguration(getSubject(), jboss.getId());

                    loadFromModel(jboss, inventoryconfig, resourceInventoryConnection.getResourceInventoryProperties());
                    configurationManagerRemote.updatePluginConfiguration(getSubject(), jboss.getId(), inventoryconfig);
                }
                if (resourceInventoryConnection.getResourceConfiguration().size() > 0) {
                    logger.debug("update configuration for resource with id[" + jboss.getId() + "], name= " + jboss.getName());
                    Configuration configuration = configurationManagerRemote.getResourceConfiguration(getSubject(), jboss.getId());

                    loadFromModel(jboss, configuration, resourceInventoryConnection.getResourceConfiguration());
                    configurationManagerRemote.updateResourceConfiguration(getSubject(), jboss.getId(), configuration);

                }
                if (resourceInventoryConnection.getSchedules().size() > 0) {

                    logger.debug("update metrics for resource with id[" + jboss.getId() + "], name= " + jboss.getName());
                    updateMetrics(jboss, resourceInventoryConnection.getSchedules());
                }
                logger.debug("Update successful finished");

            } else {
                logger.warn("cannot config resource. resource is not commited");
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            logger.error(exc);
        }

    }

    protected void updateMetrics(Resource resource, List<Schedule> schedulesToUpdate) {
        MetricResourceQuery metricResourceQuery = new MetricResourceQueryImpl();
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

    }

    protected void registerPropertyOverride(PropertyValueOverride propertyValueOverride
    ){
        propertyValueOverrideList.add(propertyValueOverride);
    }

    /**
     * @return a altered property depending if a override is registered. if not then the same val is returned
     */
    private String overRideValue(String propertyName, String propertyValue, Resource resource) {
        for (PropertyValueOverride propertyValueOverride : propertyValueOverrideList) {
            if (propertyValueOverride.getPropertyToOverride().equals(propertyName)) {
                return propertyValueOverride.overrideValue(propertyValue, resource);
            }
        }
        return propertyValue;
    }

    private Property determineAndSave(Resource resource, Configuration jbossConfig, BasicProperty basicProperty) {
        logger.debug("determining configuration to update for property.  " + basicProperty.toString());

        if (basicProperty instanceof ListProperty) {
            ListProperty listProperty = (ListProperty) basicProperty;
            logger.debug("type list propertyy discovered " + listProperty.toString());
            return determineListProperty(resource, jbossConfig, listProperty);
        } else if (basicProperty instanceof MapProperty) {
            MapProperty mapProperty = (MapProperty) basicProperty;
            logger.debug("type list propertyy discovered " + mapProperty.toString());
            return determineMapProperty(resource, jbossConfig, mapProperty);
        } else {
            System.out.println();
            PropertySimple propertySimple = new PropertySimple();
            propertySimple.setName(basicProperty.getKey());
            if (basicProperty.getValue() != null) {
                logger.debug("type basic Property discovered. Updating  " + basicProperty.toString());
                propertySimple.setStringValue(overRideValue(basicProperty.getKey(), basicProperty.getValue(), resource));
            } else {
                logger.warn("A basic property[" + basicProperty.getKey() + "] was found with nothing define. This will be by default ignored. Please define a value for " + basicProperty.getKey() + " or remove this from the import");
                return jbossConfig.get(basicProperty.getKey());
            }
            return propertySimple;
            //            saveProperty(jbossConfig,bp.getKey(),bp.getValue());
        }
    }

    @SuppressWarnings("rawtypes")
    private Property determineMapProperty(Resource resource, Configuration jbossConfig, MapProperty mapProperty) {
        logger.debug("Determine map Property " + mapProperty.toString());
        PropertyMap propertyMap = new PropertyMap();
        //todo here, try figure out if if can find the original property map. difficult as technically it could muliple leafs under the parent jboss config.
        propertyMap.setName(mapProperty.getKey());

        for (Map.Entry next : mapProperty.getPropertyMap().entrySet()) {

            Property prop = determineAndSave(resource, jbossConfig, (BasicProperty) next.getValue());

            if (prop != null)
                propertyMap.put(prop);


        }
        return propertyMap;

    }

    private void loadFromModel(Resource jboss, Configuration jbossConfig, List<BasicProperty> basicProperties) {
        logger.debug("=====================lOADING FROM MODEL=========================");
        /**
         * map to hold all properties
         */
        Map<String, Property> stringPropertyMap = new HashMap<String, Property>();
        Collection<Property> d = jbossConfig.getProperties();

        for (Property property : d) {
            // add props to map
            stringPropertyMap.put(property.getName(), property);


        }
        for (Object o : basicProperties) {
            //    if(stringPropertyMap.get(o.getKey()) instanceof PropertyList )
            //TODO here, when a propertylist is empty then its its seens as a basicproperty.
            // basic check is to. do the following. if the value is null then we warn and ignore
            Property newProperty = null;
            newProperty = determineAndSave(jboss, jbossConfig, (BasicProperty) o);
            // overwrite the current property
            if (newProperty != null ){
                stringPropertyMap.put(newProperty.getName(),newProperty);
            }
        }
        //TODO here. when an export of logeventsources is made and the list is emptry then it assues this is an type of propertysimpl.
        //TODO form some mechanism to determin what time is a property is
        System.out.println();
        // only

        jbossConfig.setProperties(stringPropertyMap.values());

    }

    private Property determineListProperty(Resource resource, Configuration jbossConfig, ListProperty listProperty) {
        System.out.println();

        PropertyList propertyList = jbossConfig.getList(listProperty.getKey());
        if (propertyList == null) {
            logger.warn("no list property with the name " + listProperty.getKey() + " found, We'll ignore this for now but maybe you should kinda look into it. ");
            propertyList = new PropertyList();
            propertyList.setName(listProperty.getKey());

        } else {
            propertyList.getList().clear();

        }
        //   List logConfigs = jbossConfig.getList(listProperty.getKey()).getList();
        @SuppressWarnings("rawtypes")
        List listu = listProperty.getPropertyList();


        for (Object o : listu) {
            propertyList.add(determineAndSave(resource, jbossConfig, (BasicProperty) o));
        }


        /*    if (s.equals("log.logFilePath") && !value.startsWith("/")) {
          //user to be serverHomeDir. this was tested on fresh eap 5 installation. a test on eap 4.3 result in a null pointer exception. this property was not valid for 4.3 installations
          // debug shows the property configurationPath is also valid
          PropertySimple serverConfig = jbossConfig.getSimple("configurationPath");
          if (serverConfig != null) {
              // handles previous 2.3 jboss plugins
              value = jbossConfig.getSimple("configurationPath").getStringValue() + "/" + value;
          } else {
              // handles previous 2.4 jboss plugins
              value = jbossConfig.getSimple("serverHomeDir").getStringValue() + "/" + value;
          }
      }
      // todo add method here to dump the new logs into a hash map
      String myid = key.substring(s.length() + 1);
      String propertyName = key.substring(key.indexOf(".") + 1, key.lastIndexOf("."));
      addToLogConfigurations(myid, createSimpleLogProperty(propertyName, value));*/
        // setLogProperty(key, value, logConfigs);
        return propertyList;
    }

    @SuppressWarnings("unused")
	private void saveProperty(Configuration jbossConfig, String key, String value) {
        try {
            //          if (key.startsWith("jboss")) {
            String propertyName = key.substring(key.indexOf(".") + 1);
            if (propertyName.equals("credentials")) {
                value = decode(value);
            }
            if (jbossConfig.getSimple(propertyName) != null) {
                logger.info("Setting [" + propertyName + "] with value [" + value + "]");

                jbossConfig.getSimple(propertyName).setStringValue(value);
            } else {

                logger.warn("IGNORING PROPERTY: Property is not mapped in configuration property in jon.'" + propertyName + " does is not a configuration property.' property[" + propertyName + "], value[" + value + "]. ");
            }
            logger.debug("Saving properties finished");

        } catch (Exception e) {
            // neeedd to fail gracefully with a warning when something is null
            throw new RuntimeException("Could not save property:  key=" + key + ", value=" + value + ", jboss config [" + jbossConfig.getAllProperties() + "]", e);
        }
    }

    private boolean isExcludedPlatform(String platformName) {
        if (excludedPlatforms == null) {
            excludedPlatforms = new HashMap<String, String>();
            // get comman sperated eexcluded list
            String exludedString = getProperty("jon.platform.exclude");
            if (exludedString != null) {
                String[] platformlist = exludedString.split(",");
                //ad to map for quick access
                for (String aPlatformlist : platformlist) {
                    excludedPlatforms.put(aPlatformlist, aPlatformlist);
                }
            }
        }
        return excludedPlatforms.containsKey(platformName);

    }

}
