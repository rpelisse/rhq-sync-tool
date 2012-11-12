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

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.CLIParameters;
import org.jboss.rhq.sync.tool.actions.JONAction;
import org.jboss.rhq.sync.tool.actions.impl.impex.templates.MetricTemplateImportAction;
import org.jboss.rhq.sync.tool.actions.impl.impex.templates.RolesImportAction;


/**
 *
 * <p>Implements a "complete import" of data into an RHQ server, using a set of {@link JONAction}.</p>
 *
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class Importer extends AbstractImportExportAction implements CLIAction {

    private static Logger logger = Logger.getLogger(Importer.class);
    
	@Override
	protected void registerActions(CLIParameters parameters,List<JONAction> actions, Map<String,String> values) {
	    logger.debug("Registering import actions:");
		if ( isQualifierSpecified(QualifierType.ROLES, parameters) ) {
    		values.put(RolesImportAction.IMPORT_FILENAME,"/roles.json");
            actions.add(new RolesImportAction(loginConfiguration, baseRemote));
            logger.debug("roles import action registered");
		}
		
		if ( isQualifierSpecified(QualifierType.METRICS, parameters) ) {
            values.put(MetricTemplateImportAction.IMPORT_FILENAME,parameters.getFile().getAbsolutePath());
            actions.add(new MetricTemplateImportAction(loginConfiguration, baseRemote));

            logger.warn(QualifierType.METRICS + "Not implemented");
		}
		logger.debug("End registering actions, action registered:" + actions.size());
	}
}
