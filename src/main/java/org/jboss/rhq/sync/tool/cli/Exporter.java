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

package org.jboss.rhq.sync.tool.cli;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.CLIParameters;
import org.jboss.rhq.sync.tool.actions.JONAction;
import org.jboss.rhq.sync.tool.actions.impl.impex.BasicResourceExporter;
import org.jboss.rhq.sync.tool.actions.impl.impex.templates.MetricTemplateExportAction;
import org.jboss.rhq.sync.tool.actions.impl.impex.templates.RolesExportAction;


public class Exporter extends AbstractImportExportAction implements CLIAction {

    private static Logger logger = Logger.getLogger(Exporter.class);

    private static SimpleDateFormat sf = new  SimpleDateFormat("yyMMdd_HH_mm");

    @Override
    protected void registerActions(CLIParameters parameters,List<JONAction> actions, Map<String,String> values) {
        logger.debug("Registering export actions.");
        if ( isQualifierSpecified(QualifierType.ROLES, parameters) ) {
    		values.put(RolesExportAction.ROLES_EXPORT_OUTPUT_FILENAME,"/roles.json");
   	        actions.add(new RolesExportAction(loginConfiguration,baseRemote));
        }

        if ( isQualifierSpecified(QualifierType.METRICS, parameters) ) {
            values.put(MetricTemplateExportAction.METRICTEMPLATE_EXPORT_OUTPUT_FILENAME, "/metric_"+sf.format(new Date())+".json");
            actions.add(new MetricTemplateExportAction(loginConfiguration, baseRemote));

        }     else if (isQualifierSpecified(QualifierType.RESOURCES, parameters)){


            values.put(BasicResourceExporter.BASIC_RESOURCE_EXPORT_OUTPUT_FILENAME,"resource_"+parameters.getResourceName()+"_"+sf.format(new Date())+".json");
            actions.add(new BasicResourceExporter(loginConfiguration,baseRemote,parameters.getResourceName(),values.get(BasicResourceExporter.BASIC_RESOURCE_EXPORT_OUTPUT_FILENAME)));
        }
        logger.warn("Data exported in: " + parameters.getExportDir().getAbsolutePath());
	}
}
