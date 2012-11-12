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

import org.jboss.rhq.sync.tool.BaseRemote;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.resource.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/22/11
 * Time: 12:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class PropertyValueOverrideLogPath implements PropertyValueOverride {


    BaseRemote baseRemote;


    /**
     * logFilePath
     */
    private String propertyName;

    public PropertyValueOverrideLogPath(String propertyName) {
        this.baseRemote = BaseRemote.getInstance(null);
        this.propertyName = propertyName;
    }

    @Override
    public String getPropertyToOverride() {
        return propertyName;
    }

    @Override()
    public String overrideValue(String value, Resource resource) {
        Configuration config = baseRemote.getConfigurationManagerRemote().getPluginConfiguration(baseRemote.getSubject(), resource.getId());
        if (!value.startsWith("/")) {
            //user to be serverHomeDir. this was tested on fresh eap 5 installation. a test on eap 4.3 result in a null pointer exception. this property was not valid for 4.3 installations
            // debug shows the property configurationPath is also valid
            assert config != null;
            PropertySimple serverConfig = config.getSimple("configurationPath");
            if (serverConfig != null) {
                // handles previous 2.3 jboss plugins
                value = serverConfig.getStringValue() + "/" + value;
            } else {
                // handles previous 2.4 jboss plugins
                if (config.getSimple("serverHomeDir") != null) {
                    value = config.getSimple("serverHomeDir").getStringValue() + "/" + value;


                } else {
                    value = "helloooo all" + value;
                }
            }

        }
        return value;
    }
}
