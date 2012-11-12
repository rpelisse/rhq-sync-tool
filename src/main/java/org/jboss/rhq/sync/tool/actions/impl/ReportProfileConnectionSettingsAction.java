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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.actions.JONAction;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.query.JbossAsResourceQuery;
import org.jboss.rhq.sync.tool.query.ResourceQueryImpl;
import org.jboss.rhq.sync.tool.util.ConnectionPropsTemplate;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.resource.Resource;
import org.rhq.enterprise.server.configuration.ConfigurationManagerRemote;

/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 20.04.11
 * Time: 14:40
 * To change this template use File | Settings | File Templates.
 */
public class ReportProfileConnectionSettingsAction extends AbstractJONAction {
    private static Logger logger = Logger.getLogger(ReportDisabledLogsAction.class);

    private String platformName;
    private String profileName;
    private String version;
    private final ConfigurationManagerRemote configurationManagerRemote;

    private ConnectionPropsTemplate htmlTemplate;

    public ReportProfileConnectionSettingsAction(String platformName, String profileName, String version) {
        super();
        this.configurationManagerRemote = baseRemote.getConfigurationManagerRemote();
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd_HH_mm");
        this.htmlTemplate = new ConnectionPropsTemplate("templates/topLevelProps.tmpl", "result2_" + df.format(new Date()) + ".html");


        this.platformName = platformName;
        this.profileName = profileName;
        this.version = version;
    }

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
            String principal = getConfigSimple("principal", jbossConfig);
            String javaHome = getConfigSimple("javaHome", jbossConfig);
            String javaHomePath = getConfigSimple("javaHomePath", jbossConfig);
            String shutdownMethod = getConfigSimple("shutdownMethod", jbossConfig);
            String shutdownScript = getConfigSimple("shutdownScript", jbossConfig);
            String credentials = getConfigSimple("credentials", jbossConfig);
            logger.debug("Adding config to template: " + resource.getName() + ", platform " + parentResource.getName());
            htmlTemplate.add(parentResource.getName(), resource.getName(), resource.getVersion(), principal, javaHome, javaHomePath, shutdownMethod, shutdownScript, credentials);


        }
        htmlTemplate.printTemplate();
    }

    /**
     * retruns non-null valu
     *
     * @param key
     * @param jbossConfig
     * @return
     */
    private String getConfigSimple(String key, Configuration jbossConfig) {
        PropertySimple val = jbossConfig.getSimple(key);
        if (val != null)
            return val.getStringValue();
        else
            return "";
    }

    public static void main(String[] args) {
        JONAction t = new ReportProfileConnectionSettingsAction(null, null, null);
        t.doAction(null);
    }

}
