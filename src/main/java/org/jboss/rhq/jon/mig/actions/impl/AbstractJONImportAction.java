package org.jboss.rhq.jon.mig.actions.impl;

import java.util.Map;

import org.jboss.rhq.jon.mig.BaseRemote;
import org.jboss.rhq.jon.mig.LoginConfiguration;
import org.jboss.rhq.jon.mig.actions.JONAction;
import org.jboss.rhq.jon.mig.actions.JonActionResult.JonActionResultType;
import org.jboss.rhq.jon.mig.actions.impl.impex.templates.RolesExportAction;


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
	
    /* (non-Javadoc)
	 * @see com.allianz.amos.autoimport.actions.impl.AbstractJONAction#perform(java.util.Map)
	 */
	@Override
	protected JonActionResultType perform(Map<String, String> values) {
		return doImport(loadFromFile(values.get(AbstractJONExportAction.WORKING_DIRECTORY_PROPERTY).concat(values.get(RolesExportAction.ROLES_EXPORT_OUTPUT_FILENAME))));		
	}
	
}
