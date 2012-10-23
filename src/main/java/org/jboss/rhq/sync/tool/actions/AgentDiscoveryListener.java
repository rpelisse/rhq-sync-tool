package org.jboss.rhq.sync.tool.actions;

/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 26.04.11
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public interface AgentDiscoveryListener {
    /**
     * resources were discovery on this platform.
     *
     * @param platformName
     */
    public void discoveredResources(String platformName);
}
