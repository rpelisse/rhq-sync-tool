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

package org.jboss.rhq.sync.tool.model.impex;

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
