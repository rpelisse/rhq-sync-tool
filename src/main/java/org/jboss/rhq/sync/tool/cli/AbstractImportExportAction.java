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

import static org.jboss.rhq.sync.tool.util.PasswordUtil.encode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.CLIParameters;
import org.jboss.rhq.sync.tool.LoginConfiguration;
import org.jboss.rhq.sync.tool.actions.JONAction;
import org.jboss.rhq.sync.tool.actions.impl.AbstractJONExportAction;


public abstract class AbstractImportExportAction implements CLIAction {
    
    private static Logger logger = Logger.getLogger(AbstractImportExportAction.class);
    
    protected LoginConfiguration loginConfiguration;
    protected  BaseRemote baseRemote;
    
    @Override
    public void execute(CLIParameters parameters) {
        long startTimestamp = System.currentTimeMillis();
        logger.debug("Execute method called");
        Map<String,String> values = new HashMap<String, String>();
        values.put(AbstractJONExportAction.WORKING_DIRECTORY_PROPERTY,parameters.getExportDir().getAbsolutePath());

        loginConfiguration = new LoginConfiguration(parameters.getUsername(),encodePasswordIfNeeded(parameters), parameters.getServer().getHost(), parameters.getServer().getPort());
        baseRemote = BaseRemote.getInstance(loginConfiguration);

        List<JONAction> actions = new ArrayList<JONAction>();
        registerActions(parameters, actions, values);
        
        logger.debug("Executing actions:");
        for ( JONAction action : actions ) 
            action.doAction(values);    
        
        logger.warn("Execution time:" + (System.currentTimeMillis() - startTimestamp) + "ms.");
    }
    
    protected boolean isQualifierSpecified(QualifierType qualifier, CLIParameters parameters) {
        if ( parameters.getQualifiers() == null || parameters.getQualifiers().isEmpty() )
            return true; // No qualifier implies ALL
        return parameters.getQualifiers().contains(qualifier);
    }
    
    protected String encodePasswordIfNeeded(CLIParameters parameters) {
        if ( parameters == null )
            throw new IllegalStateException("No proper instance of " + CLIParameters.class + " provided (null)");

        if ( parameters.getPassword() == null || "".equals(parameters.getPassword()))
            throw new IllegalArgumentException("No password provided, but ");

        if (parameters.isEncodedPassword() ) {
            return encode(parameters.getPassword());
        }
        return parameters.getPassword();
    }
    
    protected abstract void registerActions(CLIParameters parameters, List<JONAction> actions,Map<String,String> values);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
