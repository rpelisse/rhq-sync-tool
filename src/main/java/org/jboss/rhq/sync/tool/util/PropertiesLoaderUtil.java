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

package org.jboss.rhq.sync.tool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.actions.impl.AbstractJONAction;


/**
 * @author Romain PELISSE - <belaran@redhat.com>
 * @author Ivan McInkle - <imcinkle@redhat.com>
 */
public final class PropertiesLoaderUtil {

    protected static final String OUTPUT_FILENAME = "user.export.filename";
    
    private static Logger logger = Logger.getLogger(AbstractJONAction.class);

    public static final String USER_OVERRIDE_PROPERTIES_FILE = "rhq.sync.tool.user.override";
    private static final String DEFAULT_PROPERTIES_VALUES = "rhq.sync.tool.default.properties";
    private static final String INSTANCE_OVERRIDE_PROPERTIES = "rhq.sync.tool.instance.properties";

    /**
     * Utility class should NOT be instantiated
     */
    private PropertiesLoaderUtil() {
    }


    /**
     * <p>Load properties from different places, according to the following order (<em>Note
     * that each property source override, if some property are duplicated, the previous ones):
     * <ol>
     *   <li>Search CLASSPATH for DEFAULT_PROPERTIES_VALUES.</li>
     *   <li>Search file system for INSTANCE_PROPERTIES, and then LEGACY_PROPERTY_PATHNAME.</li>
     *   <li>Search file provided with system property JON_SETTINGS_PROPERTYNAME  - if any.</li>
     * </ol>
     *
     * @return
     */
    public static Properties getProperties() {
        logger.info("Loading properties");
        Properties properties = new Properties();
        try {
        	loadProperties(properties);
        } catch (FileNotFoundException e) {
            logger.error("Error while getting properties : " + e);
        } catch (IOException e) {
            logger.error("Error while getting properties : " + e);
        }
        return properties;
    }

    private static void loadProperties(Properties properties) throws FileNotFoundException, IOException {
    	debug("First, look CLASSPATH for " + DEFAULT_PROPERTIES_VALUES + " - which contains all default values");
    	properties = searchInClasspath("/" + DEFAULT_PROPERTIES_VALUES,properties);
    	debug("Second, look for a \"special\" file " + INSTANCE_OVERRIDE_PROPERTIES + " in the file system - can be set up by infrastructure team or admin");
      	properties = loadPropertiesFromFile(System.getProperty(INSTANCE_OVERRIDE_PROPERTIES), properties);
      	String overrideFile = System.getProperty(USER_OVERRIDE_PROPERTIES_FILE);
      	debug("Third, look for user provided file " + overrideFile + ", to override any previously loaded values - allowing to tweak the previous settings at run time");
      	if ( overrideFile != null && ! "".equals(overrideFile) && FileUtils.exists(overrideFile) )
      		properties = loadPropertiesFromFile(overrideFile, properties);
      	debug("Finish searching for properties files");
      	if ( properties.isEmpty() ) {
      		logger.debug("No properties loaded. Stopping...");
      	} else {
      		debug("Properties loaded:");
      		for ( Entry<Object,Object> property : properties.entrySet() ) {
      			debug(property.getKey() + ":"  + property.getValue());
      		}
      	}
    }

    private static void debug(String message) {
    	if ( logger.isDebugEnabled() ) {
    		logger.debug(message);
    	}
    }

    private static Properties searchInClasspath(String jbossProperties,
			Properties prop) throws FileNotFoundException, IOException {
    	return loadPropertiesFromFile(PropertiesLoaderUtil.class.getResourceAsStream(jbossProperties),prop);
	}

	private static Properties loadPropertiesFromFile(InputStream fis, Properties prop) throws FileNotFoundException, IOException {
        if (fis != null ) {
	        prop.load(fis);
	        fis.close();
        }
        return prop;
    }

	private static Properties loadPropertiesFromFile(String filename, Properties prop) throws FileNotFoundException, IOException {
        if (filename != null && ! "".equals(filename) ) {
	        File file = new File(filename);
	        logger.debug("Reading properties from file: " + file.getAbsolutePath());
	        FileInputStream fis  = new FileInputStream(file);
	        loadPropertiesFromFile(fis, prop);
	        logger.info("system property 'jbprops' found. loading property file :" + file.getAbsolutePath());
        }
        return prop;
    }
}
