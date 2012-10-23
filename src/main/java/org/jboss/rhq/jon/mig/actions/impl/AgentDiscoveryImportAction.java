package org.jboss.rhq.jon.mig.actions.impl;

import org.apache.log4j.Logger;
import org.jboss.rhq.jon.mig.actions.AgentDiscoveryListener;
import org.jboss.rhq.jon.mig.actions.JonActionResult;
import org.jboss.rhq.jon.mig.query.AgentResoucreQuery;
import org.jboss.rhq.jon.mig.query.ResourceQueryImpl;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.criteria.OperationDefinitionCriteria;
import org.rhq.core.domain.criteria.ResourceOperationHistoryCriteria;
import org.rhq.core.domain.operation.OperationDefinition;
import org.rhq.core.domain.operation.OperationRequestStatus;
import org.rhq.core.domain.operation.ResourceOperationHistory;
import org.rhq.core.domain.resource.Resource;
import org.rhq.core.domain.util.PageList;
import org.rhq.core.domain.util.PageOrdering;
import org.rhq.enterprise.server.operation.OperationManagerRemote;
import org.rhq.core.domain.operation.bean.ResourceOperationSchedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 26.04.11
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class AgentDiscoveryImportAction extends AbstractJONAction {

    private final List<AgentDiscoveryListener> agentDiscoveryListerners;
    private final AgentResoucreQuery agentquery;
    private final OperationManagerRemote operationManager;

    private final Long delay;
    private final Integer timeout;
    private final Integer repeat;

    private final Long pollSleepInterval;
    private final Integer pollRepeatCount;

    private static Logger logger = Logger.getLogger(AgentDiscoveryImportAction.class);

    public AgentDiscoveryImportAction() {
        super();

        operationManager = baseRemote.getOperationManager();
        agentquery = new ResourceQueryImpl();
        agentDiscoveryListerners = new ArrayList<AgentDiscoveryListener>();
        delay = Long.decode((String) props.get("agent.discovery.delay"));
        timeout = Integer.valueOf((String) props.get("agent.discovery.timeout"));
        repeat = Integer.valueOf((String) props.get("agent.discovery.repeat"));

        pollRepeatCount = Integer.valueOf((String) props.get("agent.discovery.poll.count"));
        pollSleepInterval = Long.decode((String) props.get("agent.discovery.poll.sleepInterval"));
    }

    @Override
    protected JonActionResult.JonActionResultType perform(Map<String, String> values) throws RuntimeException {
        int retval = 0;
        String platformName = (String) values.get(PLATFORM_NAME);

        if (platformName == null || platformName.length() <= 0) {
            logger.warn("PLATFORM is not null or not defined. Cannot run discovery with a platform name");

            retval = -1;
        } else {
            logger.debug("runing discovery on platform " + platformName);
            //todo find my agent
            List<Resource> resources = agentquery.getRHQAgent(platformName);
            //todo
            if (resources.size() > 0)
                retval = scheduleDiscoveryOperation(resources);
            else
                logger.debug("no agents found for the platform with name [" + platformName + "]");
        }
        if (retval == 0) {
            return JonActionResult.JonActionResultType.SUCCESS;
        } else {
            return JonActionResult.JonActionResultType.FAIL;
        }


    }

    /*
   useless method to print all operations available
    */
    @SuppressWarnings("unused")
	private void printAllOperations() {
        OperationDefinitionCriteria c = new OperationDefinitionCriteria();
        c.addFilterResourceTypeName("RHQ Agent");
        List<OperationDefinition> operations = operationManager.findOperationDefinitionsByCriteria(getSubject(), c);
        for (OperationDefinition operationDefinition : operations) {
            logger.debug(operationDefinition.getName());
        }

    }

    private int scheduleDiscoveryOperation(List<Resource> resources) {

        int retval = 0;

        for (Resource resource : resources) {

            int tmpRet = scheduleDiscoveryOperation(resource);

            if (retval == 0) {
                retval = tmpRet;
            }
        }

        return retval;

    }


    /**
     * operation available
     * switchToServer
     * restart
     * shutdown
     * restartPluginContainer
     * downloadLatestFailoverList
     * updatePlugins
     * retrieveAllPluginInfo
     * retrievePluginInfo
     * executeAvailabilityScan
     * retrieveCurrentDateTime
     * setDebugMode
     * executePromptCommand
     * also be called using
     * operationManager.findOperationDefinitionsByCriteria
     *
     * @param agentResource
     */
    private int scheduleDiscoveryOperation(Resource agentResource) {

        int retval = 0;

        logger.debug("Scheduling discovery operation " + agentResource.getName());
        Configuration config = new Configuration();

        config.put(new PropertySimple("command", "discovery -f"));

        ResourceOperationSchedule operationSchedule = operationManager.scheduleResourceOperation(getSubject(),
                agentResource.getId(),
                "executePromptCommand",

                delay,   // delay
                1,   // repeatInterval
                repeat,   // repeat Count
                timeout,  // timeOut
                config,    // config
                "EXECUTING DISCOVERY: Profile was create. forcing discovery " // description
        );
        ///


        ResourceOperationHistoryCriteria roh = new ResourceOperationHistoryCriteria();
        roh.addFilterJobId(operationSchedule.getJobId());
        roh.addFilterResourceIds(operationSchedule.getResource().getId());
        roh.addSortStartTime(PageOrdering.DESC);
        roh.setPaging(0, 1);
        roh.fetchOperationDefinition(true);
        roh.fetchParameters(true);
        roh.fetchResults(true);
        //  roh.addf();
        //TODO. CHECK IF OPERATION COMPLETE HERE. BY CHECKING THE HISTORY

        int i = 0;
        ResourceOperationHistory history = null;

        while (history == null && i < pollRepeatCount) {
            logger.debug("sleeping " + i + ", ");
            sleep(pollSleepInterval);
            PageList<ResourceOperationHistory> histories = operationManager
                    .findResourceOperationHistoriesByCriteria(getSubject(), roh);
            if (histories.size() > 0 && histories.get(0).getStatus() != OperationRequestStatus.INPROGRESS) {
                history = histories.get(0);
            }
            ++i;
        }
        if (history == null) {
            logger.debug("we timed out here. no result was return. should fail gracefully and exit. or thow back a runtime. your choice");
        } else {
            if (history.getStatus().equals(OperationRequestStatus.FAILURE) || history.getErrorMessage() != null) {
                retval = -1;
                logger.debug("we just failed. command was not successful. status=failure");
                logger.debug("" + history.getErrorMessage());
            } else {
                logger.debug("we should call out call back listeners here. ");
                for (AgentDiscoveryListener agentDiscoveryListerner : agentDiscoveryListerners) {
                    logger.debug("calling call back listener ");
                    agentDiscoveryListerner.discoveredResources(agentResource.getParentResource().getName());
                }
            }

        }


        //command = executePromptCommand

        return retval;
    }

    public void addDiscoveryListener(AgentDiscoveryListener listener) {
        agentDiscoveryListerners.add(listener);
    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }
}
