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

import static org.jboss.rhq.sync.tool.util.PasswordUtil.decode;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.actions.AgentDiscoveryListener;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.query.JbossAsResourceQuery;
import org.jboss.rhq.sync.tool.query.ResourceQueryImpl;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.Property;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.resource.InventoryStatus;
import org.rhq.core.domain.resource.Resource;
import org.rhq.core.util.collection.ArrayUtils;
import org.rhq.enterprise.server.configuration.ConfigurationManagerRemote;

/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 20.04.11
 * Time: 16:48
 * To change this template use File | Settings | File Templates.
 */
public class MassConnectionUpdateAction extends AbstractJONAction implements AgentDiscoveryListener {
    private final ConfigurationManagerRemote configurationManagerRemote;
    private static Logger logger = Logger.getLogger(MassConnectionUpdateAction.class);
    private static final String[] LOG_PLACEHOLDERS = {"log.enabled", "log.logFilePath", "log.minimumSeverity", "log.includesPattern", "log.dateFormat"};
    /**
     * Action. describe if something should be imported or just updated
     * todo. split the action into two seperate interfaces methods. no need to have this configurable
     */

    private final String version;
    private Map<String, String> excludedPlatforms;

    protected JonActionResult actionResult;
    protected Map<String, PropertyMap> logCongfigurations = new HashMap<String, PropertyMap>();

    public MassConnectionUpdateAction() {
        super();
        this.configurationManagerRemote = baseRemote.getConfigurationManagerRemote();
        actionResult = new JonActionResult();
        /**
         * get the version to manage
         */
        version = (String) props.get("jon.jboss.version");
    }

    @Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
        String platformName = (String) values.get(PLATFORM_NAME);
        String profileName = (String) values.get(PROFILE_NAME);
        String action = (String) values.get(ACTION);

        logger.info(" configuring platform [" + platformName + "] profileName=" + profileName);
        if (action == null || action.length() == 0) {
            throw new IllegalArgumentException("NO ACTION DEFINED. property 'jon.jboss.action'  should be either IMPORT  or  UPDATE");
        }


        String[] platformNames = determinePlatforms(platformName);
        for (String platformName1 : platformNames) {
            // the exclude list takes priority
            if (!isExcludedPlatform(platformName1)) {
                logger.info("####################### Configuring Platform: " + platformName1 + " ######################");
                perform(platformName1.trim(), profileName, action);
            } else
                logger.warn("Platform " + platformName1 + " is found in excluded list. Will not update. ");
        }

        return JonActionResult.JonActionResultType.SUCCESS;


    }

    private boolean isExcludedPlatform(String platformName) {
        if (excludedPlatforms == null) {
            excludedPlatforms = new HashMap<String, String>();
            // get comman sperated eexcluded list
            String exludedString = props.getProperty("jon.platform.exclude");
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

    private void perform(String platformName, String profileName, String action) {
        JbossAsResourceQuery query = new ResourceQueryImpl();
        List<Resource> jbossList;
        if (ConstantPool.IMPORT.equalsIgnoreCase(action)) {
            logger.info("peforming import. only profiles in the auto discovery queue will be imported and configured");
            jbossList = query.getAllNewJBossAS(platformName, profileName, version);
            logger.info("found " + jbossList.size() + " jboss profiles to import");
            List<Integer> importIds = new ArrayList<Integer>();
            for (Resource jbossResource : jbossList) {
                logger.info(" IMPORTING jboss profile resource '" + jbossResource.getName() + "' to be imported ");
                addDependency(jbossResource, importIds);
                /**
                 * need to check its parent is added.only adding if its  new. other status could be ignored etc. we want and exception if its anything other than new
                 */
            }

            if (importIds.size() > 0) {
                logger.info(" Performing import of all new resources discovered ");
                baseRemote.getDiscoveryBossRemote().importResources(getSubject(), ArrayUtils.unwrapCollection(importIds));
            } else {
                logger.debug(" no resource to import ");
            }
        } else if (ConstantPool.UPDATE.equalsIgnoreCase(action))

        {
            logger.debug("performing update of resources ");
            jbossList = query.getAllInventoriedJBossAS(platformName, profileName, version);
        } else if (ConstantPool.DELETE.equalsIgnoreCase(action))

        {
            jbossList = query.getAllInventoriedJBossAS(platformName, profileName, version);

            int[] idArray = new int[jbossList.size()];

            for (int i = 0; i < jbossList.size(); i++) {
                Resource resource = jbossList.get(i);
                logger.debug("Delete resource " + resource.getName());
                int id = resource.getId();
                idArray[i] = id;
            }

            baseRemote.getResourceManager().uninventoryResources(getSubject(), idArray);
        } else
            throw new IllegalArgumentException("NO VALID ACTION DEFINED(" + action + "). property 'jon.jboss.action' should be either IMPORT  or  UPDATE");

        logger.info("The list of jboss profiles has been determined. Beginning reconfiguration of profiles. number of profiles[" + jbossList.size() + "]");
        for (
                Resource jbossResource
                : jbossList)

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
                alterInventoryConfigurations(jbossResource);
                logger.debug("Finished inventory configuration update: profile=" + jbossResource.getName() + ", platform=" + parentPlatformResource.getName());
            }
        }
    }

    private String[] determinePlatforms(String platformName) {
        if (platformName == null || platformName.trim().length() == 0) {
            logger.debug("No platfrom name defined: looking up system property:jon.platform.name");
            String systemAgent = System.getProperty("jon.platform.name");


            if (systemAgent == null || systemAgent.trim().length() == 0) {
                logger.debug("no system property defined: lookingup property file using key 'jon.platform.name' ");
                systemAgent = props.getProperty("jon.platform.name");
                if (systemAgent == null || systemAgent.trim().length() == 0) {
                    logger.debug("no value has been defined for jon.platform.name: using system getCanonicalHostName() ");
                    try {
                        logger.debug("Looking up host-name on system...");
                        InetAddress address = InetAddress.getLocalHost();
                        platformName = address.getCanonicalHostName();
                        logger.debug("Host-name is: " + platformName);
                        logger.debug("platform name found: " + platformName);
                    } catch (UnknownHostException e) {
                        // TODO Auto-generated catch block
                        logger.error(e);
                        logger.debug("Could not find correct host name, please try to start again with VM-parameter jon.platform.name");
                        throw new RuntimeException(e);
                    }
                } else {
                    logger.debug("Using property file value as parameter :" + systemAgent);
                    if (systemAgent.equals("ALL"))
                        platformName = "";
                    else
                        platformName = systemAgent.trim();
                    logger.debug("Host-name is: " + platformName);
                }
            } else {
                logger.debug("Using host-name from vm-parameter ... ");
                platformName = systemAgent.trim();
                logger.debug("Host-name is: " + platformName);
            }

        }

        return platformName.split(",");
    }


    /**
     * method alters the configurartion of the passed jboss profile. Each profile must already be imported in jon otherwise it will do nothing (apart from a logging a warning)
     *
     * @param jboss
     */
    private void alterInventoryConfigurations(Resource jboss) {
        /**
         * ONLY ALTER COMMITED DATA.
         */
        try {
            logger.debug(" ... alterInventoryConfigurations ");
            if (jboss.getInventoryStatus().equals(InventoryStatus.COMMITTED)) {
                logger.debug("inventory-status: committed");
                Configuration config = configurationManagerRemote.getPluginConfiguration(getSubject(), jboss.getId());

                /**
                 *
                 */
                loadFromProperties(config);
                logger.debug("trying to update ... ");
                configurationManagerRemote.updatePluginConfiguration(getSubject(), jboss.getId(), config);
                logger.debug("Update successful finished");

            } else {
                logger.warn("cannot config resource. resource is not commited");
            }
        } catch (Exception exc) {
            logger.error(exc);
        }

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void loadFromProperties(Configuration jbossConfig) {
        Enumeration<Object> keys = props.keys();
        List logConfigs = jbossConfig.getList("logEventSources").getList();
        // logConfigs.clear();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            saveProperty(jbossConfig, key, (String) props.get(key));
        }
        // only clear the log configuration if new ones are found
        if (logCongfigurations.size() > 0) {
            logConfigs.clear();
        }
        for (PropertyMap propertyMap : logCongfigurations.values()) {
            PropertyMap next = propertyMap;
            logConfigs.add(next);
        }

    }

    private void addDependency(Resource resource, List<Integer> ids) {
        logger.debug(" checking if resource has any parent resources that are required to be imported ");
        Resource parent = baseRemote.getResourceManager().getResource(getSubject(), resource.getId()).getParentResource();

        if (parent != null) {
            logger.debug(" parent resource found [" + parent.getName() + "]");
            parent = baseRemote.getResourceManager().getResource(getSubject(), parent.getId());
            if (parent.getInventoryStatus().equals(InventoryStatus.NEW)) {
                logger.info(" parent resource[" + resource.getName() + "] is uninventoried. perform parent check before import ");
                addDependency(parent, ids);
            }
        }
        logger.info(" adding resource to be imported [" + resource.getName() + "]");
        ids.add(resource.getId());
    }

    private void saveProperty(Configuration jbossConfig, String key, String value) {
        try {
            if (key.startsWith("jboss")) {
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

            } else if (key.startsWith("log")) {
                /**
                 * update the logs that will be monitored for events
                 */

                for (String s : LOG_PLACEHOLDERS) {
                    if (key.startsWith(s)) {
                        if (s.equals("log.logFilePath") && !value.startsWith("/")) {
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
                        addToLogConfigurations(myid, createSimpleLogProperty(propertyName, value));
                        // setLogProperty(key, value, logConfigs);
                    }
                }
                logger.debug("Saving properties finished");
            }
        } catch (Exception e) {
            // neeedd to fail gracefully with a warning when something is null
            throw new RuntimeException("Could not save property:  key=" + key + ", value=" + value + ", jboss config [" + jbossConfig.getAllProperties() + "]", e);
        }
    }

    private void addToLogConfigurations(String id, Property propertySimple) {
        PropertyMap map = logCongfigurations.get(id);
        if (map == null) {
            map = new PropertyMap();
            map.setName("logEventSource");
        }
        Map<String, Property> prpmap = map.getMap();
        if (prpmap == null) {
            prpmap = new HashMap<String, Property>();
        }
        prpmap.put(propertySimple.getName(), propertySimple);

        map.setMap(prpmap);
        logCongfigurations.put(id, map);

        //logCongfigurations.get(id).getMap().put(propertySimple.getName(),propertySimple);

    }


    @SuppressWarnings("unused")
	private void setLogProperty(String prop, String value, List<PropertyMap> logConfigs) {
        String logName = prop.substring(prop.lastIndexOf(".") + 1);
        String propertyName = prop.substring(prop.indexOf(".") + 1, prop.lastIndexOf("."));

        for (PropertyMap property : logConfigs) {
            property.setName("logEventSource");
            if (property.getSimple("logFilePath") != null && property.getSimple("logFilePath").getStringValue().endsWith("server.log")) {
                logger.debug("Log property name = " + property.getName());
                if (property.getName().equals(logName)) {
                    if (property.getSimple(propertyName) != null) {
                        logger.debug(propertyName + " setting " + property.toString());
                        property.getSimple(propertyName).setStringValue(value);
                        logger.debug(propertyName + " after" + property.toString());
                    }

                }
            }
        }

    }

    private PropertySimple createSimpleLogProperty(String name, String value) {
        PropertySimple simpleProp = new PropertySimple();
        simpleProp.setStringValue(value);
        simpleProp.setName(name);

        return simpleProp;
        // my.put("logFilePath",logFilePath);
    }

    @Override
    public void discoveredResources(String platformName) {
        logger.debug("Class back listener:" + this.getClass().getName());
        /**
         * we ha ve no idea the name of the profiles that were discoveryed so we can only scan the platform and pick up and profiles found.
         * as we are executing rom a discovery event we need to override 'operation'the default in the configuration (UPDATE OR IMPORT)
         * this must be set to IMPORT
         */
        Map<String, String> values = new HashMap<String, String>();
        values.put(ACTION, ConstantPool.IMPORT);
        values.put(PROFILE_NAME, platformName);
        perform(platformName, null, ConstantPool.IMPORT);
    }
}
