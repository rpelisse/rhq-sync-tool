package org.jboss.rhq.sync.tool.actions.impl.impex;


import java.io.IOException;
import java.util.List;

import org.jboss.rhq.sync.tool.model.impex.MetricSchedule;
import org.jboss.rhq.sync.tool.model.impex.ResourceInventoryConnection;
import org.jboss.rhq.sync.tool.model.impex.SimpleResourceType;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/3/11
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ConfigurationRepo {
    public List<ResourceInventoryConnection> getInventory(String filename) throws IOException;

    public List<MetricSchedule> getMetric(String filename) throws IOException;

    public void saveMetric(List<MetricSchedule> metrics, String filename) throws IOException;

    public void saveCollection(List connectionList, String filename) throws IOException;

    public List<SimpleResourceType> getSimpleResourceType(String filename);
}
