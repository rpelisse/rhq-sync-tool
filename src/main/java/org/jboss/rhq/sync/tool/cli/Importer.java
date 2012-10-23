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
