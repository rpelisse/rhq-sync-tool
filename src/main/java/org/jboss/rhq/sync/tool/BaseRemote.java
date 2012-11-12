package org.jboss.rhq.sync.tool;

import org.apache.log4j.Logger;
import org.rhq.core.domain.auth.Subject;
import org.rhq.enterprise.clientapi.RemoteClient;
import org.rhq.enterprise.server.auth.SubjectManagerRemote;
import org.rhq.enterprise.server.authz.RoleManagerRemote;
import org.rhq.enterprise.server.configuration.ConfigurationManagerRemote;
import org.rhq.enterprise.server.discovery.DiscoveryBossRemote;
import org.rhq.enterprise.server.measurement.MeasurementDefinitionManagerRemote;
import org.rhq.enterprise.server.measurement.MeasurementScheduleManagerRemote;
import org.rhq.enterprise.server.operation.OperationManagerRemote;
import org.rhq.enterprise.server.resource.ResourceManagerRemote;
import org.rhq.enterprise.server.resource.ResourceTypeManagerRemote;
import org.rhq.enterprise.server.resource.group.ResourceGroupManagerRemote;


/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: Mar 7, 2011
 * Time: 11:37:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class BaseRemote {

    private static BaseRemote _instance;
    private final LoginConfiguration loginConfiguration;

    private ConfigurationManagerRemote configurationManager;
    private RemoteClient remoteClient;
    /* This represents the RHQ user that is logged in and making the remote calls. This user must
     * already exist.  For the work being done here the user must also have SECURITY_MANAGER permissions.
     */
    private Subject subject;
    private SubjectManagerRemote subjectManager;
    private OperationManagerRemote operationManager;
    private DiscoveryBossRemote discoveryBoss;
    private ResourceManagerRemote resourceManager;
    private ResourceTypeManagerRemote resourceTypeManagerRemote;
    private MeasurementScheduleManagerRemote measurementScheduleManagerRemote;
    private ResourceGroupManagerRemote resourceGroupManagerRemote;
    private MeasurementDefinitionManagerRemote measurementDefinitionManagerRemote;

    private static Logger logger = Logger.getLogger(LoginConfiguration.class);
    private RoleManagerRemote roleManager;

    private BaseRemote(LoginConfiguration loginConfig) {

        this.loginConfiguration = loginConfig;

        try {
            login();
            initRemoteManagers();
        } catch (Throwable throwable) {
            logger.error(throwable);
            throw new IllegalStateException(throwable);
        } finally {
            logout();
        }
    }

    private void login() throws Exception {
    	if ( loginConfiguration == null )
    		throw new IllegalStateException("Can't login to server as no login configuration is available.");
        this.remoteClient = new RemoteClient(loginConfiguration.getHost(), loginConfiguration.getPort());
        this.subject = remoteClient.login(loginConfiguration.getUserName(), loginConfiguration.getPassword());
    }

    private void initRemoteManagers() {
        // TODO: use this getManagers() rather than the following ?
        operationManager = getRemoteClient().getOperationManager();
        discoveryBoss = getRemoteClient().getDiscoveryBoss();
        configurationManager = getRemoteClient().getConfigurationManager();
        resourceManager = getRemoteClient().getResourceManager();
        resourceTypeManagerRemote = getRemoteClient().getResourceTypeManager();
        measurementDefinitionManagerRemote = getRemoteClient().getMeasurementDefinitionManager();
        measurementScheduleManagerRemote = getRemoteClient().getMeasurementScheduleManager();
        //subjectManager = getRemoteClient().getSubjectManagerRemote();
        roleManager = getRemoteClient().getRoleManager();
        resourceGroupManagerRemote = getRemoteClient().getResourceGroupManager();
    }

    public void logout() {
        if ((null != subjectManager) && (null != subject)) {
            try {
                subjectManager.logout(subject);
            } catch (Exception e) {
            	logger.warn("Can't logout:" + e.getMessage());
                // just suppress the exception, nothing else we can do
            }
        }
    }


    public synchronized static BaseRemote getInstance(LoginConfiguration loginConfig) {
        if (_instance == null) {
            _instance = new BaseRemote(loginConfig);
        }
        return _instance;
    }

    public ConfigurationManagerRemote getConfigurationManagerRemote() {
        return configurationManager;  //To change body of created methods use File | Settings | File Templates.
    }

    public SubjectManagerRemote getSubjectManager() {
    	if ( subjectManager == null && getRemoteClient() != null )
    		subjectManager =  getRemoteClient().getSubjectManager();
    	return subjectManager;
    }

    public ResourceManagerRemote getResourceManager() {
        return resourceManager;
    }

    public ResourceTypeManagerRemote getResourceTypeManagerRemote() {
        return resourceTypeManagerRemote;
    }

    public OperationManagerRemote getOperationManager() {
        return operationManager;
    }

    public DiscoveryBossRemote getDiscoveryBossRemote() {
        return discoveryBoss;
    }

    public Subject getSubject() {
        return subject;
    }

    private org.rhq.enterprise.clientapi.RemoteClient getRemoteClient() {
        return remoteClient;
    }

    public RoleManagerRemote getRoleManager() {
        return roleManager;
    }

    public MeasurementScheduleManagerRemote getMeasurementScheduleManagerRemote() {
        return measurementScheduleManagerRemote;
    }

    public MeasurementDefinitionManagerRemote getMeasurementDefinitionManagerRemote() {
        return measurementDefinitionManagerRemote;
    }

    public ResourceGroupManagerRemote getResourceGroupManagerRemote() {
        return resourceGroupManagerRemote;
    }

}
