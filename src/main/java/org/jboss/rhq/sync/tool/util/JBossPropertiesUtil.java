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
public final class JBossPropertiesUtil {

    protected static final String OUTPUT_FILENAME = "user.export.filename";

    public static final String JON_SETTINGS_PROPERTYNAME = "jbprops";
    @Deprecated
    private static final String LEGACY_PROPERTY_PATHNAME =  "importAndConfigure/jbprops";
    private static Logger logger = Logger.getLogger(AbstractJONAction.class);

    private static final String DEFAULT_PROPERTIES_VALUES = "jboss.properties";
    private static final String INSTANCE_PROPERTIES = "/var/opt/jboss/jboss-env.properties";

    /**
     * Utility class should NOT be instantiated
     */
    private JBossPropertiesUtil() {
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
    	debug("Second, look for a \"special\" file " + INSTANCE_PROPERTIES + " in the file system - can be set up by infrastructure team or admin");
      	properties = loadPropertiesFromFile(System.getProperty(INSTANCE_PROPERTIES), properties);
    	debug("Also look for a \"legacy\" file " + INSTANCE_PROPERTIES + " in the file system.");
      	if ( FileUtils.exists(LEGACY_PROPERTY_PATHNAME) )
      		properties = loadPropertiesFromFile(LEGACY_PROPERTY_PATHNAME, properties);
      	String overrideFile = System.getProperty(JON_SETTINGS_PROPERTYNAME);
      	debug("Third, look for user provided file " + overrideFile + ", to override any previously loaded values - allowing to tweak the previous settings at run time");
      	if ( overrideFile != null && ! "".equals(overrideFile) && FileUtils.exists(overrideFile) )
      		properties = loadPropertiesFromFile(overrideFile, properties);
      	debug("Finish searching for properties files");
      	if ( properties.isEmpty() ) {
      		logger.info("Error: No properties loaded. Stopping...");
      		throw new IllegalStateException("No properties were loaded, ");
      	} else {
      		debug("Log properties found");
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
    	return loadPropertiesFromFile(JBossPropertiesUtil.class.getResourceAsStream(jbossProperties),prop);
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

/*
    public static void main(String[] args) throws FileNotFoundException, IOException {
    	Properties props = new Properties();
    	props.load(new FileInputStream(new File("/home/rpelisse/Repositories/redhat/projects/jon-mig.git+svn/resources/jboss.properties")));
    	System.out.println(props.getProperty("jon.server.host"));
    	System.out.println(props.getProperty("jon.platform.name"));

    	props.load(new FileInputStream(new File("/tmp/dummy.properties")));
    	System.out.println(props.getProperty("jon.server.host"));
    	System.out.println(props.getProperty("jon.platform.name"));
    }
  */

}
