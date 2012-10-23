package org.jboss.rhq.jon.mig.actions.impl.impex.templates;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jboss.rhq.jon.mig.BaseRemote;
import org.jboss.rhq.jon.mig.LoginConfiguration;
import org.jboss.rhq.jon.mig.actions.JonActionResult;
import org.jboss.rhq.jon.mig.actions.impl.AbstractJONExportAction;
import org.jboss.rhq.jon.mig.query.ResourceQueryImpl;
import org.jboss.rhq.jon.mig.query.SubjectsAndRolesQueryImpl;
import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.resource.Resource;


/**
 * @author Ivan McKinley  - <imckinle@redhat.com>
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class UserExportAction extends AbstractJONExportAction<List<Subject>> {

    public static final String USER_EXPORT_OUTPUT_FILENAME = "user.export.filename";
    private static Logger logger = Logger.getLogger(UserExportAction.class);

    public UserExportAction(LoginConfiguration loginConfiguration,
            BaseRemote baseRemote) {
        super(loginConfiguration, baseRemote);
        logger.debug("Connecting to JON Server using:" + loginConfiguration);
    }

    @Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
        setFilename(values, USER_EXPORT_OUTPUT_FILENAME);
        return createResultReport(saveOnFile(getAllUsers(), values));
    }

    //FIXME: Should be move into a more appropriate class
    public List<Resource> getAll() {
        return new ResourceQueryImpl().getResourceByFilter(null);
    }

    @Override
    protected JonActionResult.JonActionResultType createResultReport(List<Subject> subjects) {
        logger.debug("Exporting Subjects:");
        for ( Subject subject : subjects ) {
            logger.debug(subject);
        }
        return JonActionResult.JonActionResultType.SUCCESS;
    }

    private List<Subject> getAllUsers() {
        return new SubjectsAndRolesQueryImpl(baseRemote).getAllUsers();
    }
}
