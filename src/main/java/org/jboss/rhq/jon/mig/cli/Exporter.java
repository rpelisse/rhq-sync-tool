package org.jboss.rhq.jon.mig.cli;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.jon.mig.CLIParameters;
import org.jboss.rhq.jon.mig.actions.JONAction;
import org.jboss.rhq.jon.mig.actions.impl.impex.BasicResourceExporter;
import org.jboss.rhq.jon.mig.actions.impl.impex.templates.MetricTemplateExportAction;
import org.jboss.rhq.jon.mig.actions.impl.impex.templates.RolesExportAction;


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
