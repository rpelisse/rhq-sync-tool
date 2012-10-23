package org.jboss.rhq.jon.mig.query;

import org.rhq.core.domain.resource.Resource;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 3/9/11
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AgentResoucreQuery extends ResourceQuery {

    /**
     * rerturns all RHQ query that match the stringname
     *
     * @param platformName
     * @return
     */
    public List<Resource> getRHQAgent(String platformName);

    /**
     * @param platformName
     * @return
     */
    public List<Resource> getAllInventoriedRHQAgents(String platformName);

}
