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

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.actions.JONAction;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.query.JbossAsResourceQuery;
import org.jboss.rhq.sync.tool.query.ResourceQueryImpl;
import org.jboss.rhq.sync.tool.util.HtmlLogFileTemplate;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.Property;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.resource.Resource;
import org.rhq.enterprise.server.configuration.ConfigurationManagerRemote;


/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 20.04.11
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */
public class ReportDisabledLogsAction extends AbstractJONAction {
    private static Logger logger = Logger.getLogger(ReportDisabledLogsAction.class);
    private String platformName;
    private String profileName;
    private String version;
    private final ConfigurationManagerRemote configurationManagerRemote;

    private HtmlLogFileTemplate htmlTemplate;

    public ReportDisabledLogsAction(String platformName, String profileName, String version) {
        this.configurationManagerRemote = baseRemote.getConfigurationManagerRemote();
        this.htmlTemplate = new HtmlLogFileTemplate("templates/logFileTemplate.tmpl");
        this.platformName = platformName;
        this.profileName = profileName;
        this.version = version;
    }

    /**
     * action
     *
     * @param values expects a simple map of values with the following keys
     * @return
     */
    @Override
    public JonActionResult.JonActionResultType perform(Map<String, String> values) {
        JbossAsResourceQuery query = new ResourceQueryImpl();
        List<Resource> jbossList = query.getAllInventoriedJBossAS(platformName, profileName, version);
        analyzeJbossProfile(jbossList);
        htmlTemplate.printTemplate();
        JonActionResult r = new JonActionResult();
        r.setResultType(JonActionResult.JonActionResultType.SUCCESS);
        return JonActionResult.JonActionResultType.SUCCESS;
    }

    void analyzeJbossProfile(List<Resource> jbossList) {
        for (Resource resource : jbossList) {

            resource = baseRemote.getResourceManager().getResource(getSubject(), resource.getId());
            Resource parentResource = baseRemote.getResourceManager().getResource(getSubject(), resource.getParentResource().getId());
            logger.debug(" getting inventory configuration:" + resource.getName() + ", platform " + parentResource.getName());
            Configuration jbossConfig = configurationManagerRemote.getPluginConfiguration(getSubject(), resource.getId());
            List<Property> logConfigs = jbossConfig.getList("logEventSources").getList();
            for (Property logConfig : logConfigs) {
                if (logConfig instanceof PropertyMap)
                    getLogConfigSettings(parentResource.getName(), resource.getName(), resource.getVersion(), (PropertyMap) logConfig);
                else
                    throw new IllegalStateException("This instance of" + Property.class + " was expected to be an instance of " + PropertyMap.class);
            }
            logger.debug(" getting inventory configuration:" + resource.getName() + ", platform " + resource.getParentResource().getName());
        }
    }

    private void getLogConfigSettings(String platformName, String profileName, String version, PropertyMap logConfig) {
        PropertyMap propertyMap = (PropertyMap) logConfig;
        String enabled = propertyMap.getSimple("enabled").getStringValue();
        String logFilePath = propertyMap.getSimple("logFilePath").getStringValue();
        String minimumSeverity = propertyMap.getSimple("minimumSeverity").getStringValue();
        System.out.println("enabled=" + enabled + ", file=" + logFilePath + ",minimumSeverity=" + minimumSeverity);
        htmlTemplate.add(platformName, profileName, version, logFilePath, enabled, minimumSeverity);

    }

    public static void main(String[] args) {
        JONAction t = new ReportDisabledLogsAction(null, null, null);

        t.doAction(null);
    }

}
