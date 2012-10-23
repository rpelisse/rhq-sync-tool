package org.jboss.rhq.jon.mig.cli;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.jon.mig.CLIParameters;
import org.jboss.rhq.jon.mig.actions.JONAction;
import org.jboss.rhq.jon.mig.actions.impl.AbstractJONImportAction;
import org.jboss.rhq.jon.mig.actions.impl.impex.templates.MetricTemplateImportAction;
import org.jboss.rhq.jon.mig.actions.impl.impex.templates.RolesExportAction;
import org.jboss.rhq.jon.mig.actions.impl.impex.templates.RolesImportAction;


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
