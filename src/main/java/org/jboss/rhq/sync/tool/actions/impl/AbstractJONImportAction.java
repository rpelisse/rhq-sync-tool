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

import java.util.Map;

import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.LoginConfiguration;
import org.jboss.rhq.sync.tool.actions.JONAction;
import org.jboss.rhq.sync.tool.actions.JonActionResult.JonActionResultType;


/**
 *  @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public abstract class AbstractJONImportAction<T> extends AbstractJONAction implements JONAction {

    public static final String IMPORT_FILENAME = "export.filename";
	public static final String IMPORT_DIRECTORY_PROPERTY = "export.directory";

    public AbstractJONImportAction(LoginConfiguration loginConfiguration,BaseRemote baseRemote) {
    	super(loginConfiguration,baseRemote);
    }

    public AbstractJONImportAction() {
    	super();
    }

    protected abstract T loadFromFile(String filename);

    protected abstract JonActionResultType doImport(T valueToImport);

	@Override
	protected JonActionResultType perform(Map<String, String> values) {	    
		return doImport(loadFromFile(values.get(AbstractJONExportAction.WORKING_DIRECTORY_PROPERTY).concat(values.get(IMPORT_FILENAME))));
	}

}
