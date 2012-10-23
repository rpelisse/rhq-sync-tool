package org.jboss.rhq.jon.mig.util;

import java.util.Properties;

/**
 * 
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public final class SystemUtils {

	private SystemUtils() {}
	
	public static void setSystemProperty(String propertyName, String value) {
		 Properties p = new Properties(System.getProperties());
	     p.put(propertyName, value);
	    System.setProperties(p);
	}
}
