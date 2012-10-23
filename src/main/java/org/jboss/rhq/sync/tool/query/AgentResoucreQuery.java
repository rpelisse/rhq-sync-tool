package org.jboss.rhq.sync.tool.query;

import java.util.List;

import org.rhq.core.domain.resource.Resource;

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
