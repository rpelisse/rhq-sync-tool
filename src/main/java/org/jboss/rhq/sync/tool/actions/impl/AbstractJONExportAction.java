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

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.LoginConfiguration;
import org.jboss.rhq.sync.tool.actions.JONAction;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.actions.JonActionResult.JonActionResultType;
import org.jboss.rhq.sync.tool.actions.impl.impex.JsonIO;


/**
 *  @author Romain PELISSE - <belaran@redhat.com>
 *  
 */
public abstract class AbstractJONExportAction<T> extends AbstractJONAction implements JONAction {

    protected static final String OUTPUT_FILENAME = "export.filename";
	public static final String WORKING_DIRECTORY_PROPERTY = "export.directory";

    private static Logger logger = Logger.getLogger(AbstractJONExportAction.class);

    private String filename;
    
    public AbstractJONExportAction(LoginConfiguration loginConfiguration,BaseRemote baseRemote) {
    	super(loginConfiguration,baseRemote);
    }
    
    public AbstractJONExportAction() {
    	super();
    }

	protected JonActionResult.JonActionResultType createResultReport(T t) {
		return JonActionResultType.SUCCESS;
	}
    
    protected T saveOnFile(T type, Map<String, String> values) {
        if ( type instanceof Collection ) {
            @SuppressWarnings("unchecked")
            Collection<Object> c = (Collection<Object>)type;
            for ( Object o : c )
            	logger.debug(o);
        } else
            logger.debug(type);
        new JsonIO(true).saveAsJson(type, values.get(OUTPUT_FILENAME));
        return type;
    }

	protected void setFilename(Map<String,String> values,String propertyFilename) {
		this.filename = values.get(WORKING_DIRECTORY_PROPERTY) + values.get(propertyFilename);
		values.put(OUTPUT_FILENAME, "");
		values.put(OUTPUT_FILENAME,values.get(WORKING_DIRECTORY_PROPERTY) + values.get(propertyFilename));
	}

	protected String getFilename() {
		return filename;
	}
}
