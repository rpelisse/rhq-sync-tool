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
