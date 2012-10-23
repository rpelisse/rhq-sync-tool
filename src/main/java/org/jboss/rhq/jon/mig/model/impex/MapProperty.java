package org.jboss.rhq.jon.mig.model.impex;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/13/11
 * Time: 11:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapProperty extends BasicProperty {
    private Map<String, BasicProperty> propertyMap;

    public MapProperty() {
    }

    public MapProperty(String name) {
        super(name, null);
        propertyMap = new HashMap<String, BasicProperty>();
    }

    public Map<String, BasicProperty> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, BasicProperty> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public void put(String key, BasicProperty property) {
        propertyMap.put(key, property);
    }

    @Override
    public String toString() {
        return super.toString() + ",MapProperty{" +
                "propertyMap=" + propertyMap +
                '}';
    }
}
