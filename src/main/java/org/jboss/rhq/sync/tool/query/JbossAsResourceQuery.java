package org.jboss.rhq.sync.tool.query;

import java.util.List;

import org.rhq.core.domain.resource.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 3/9/11
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface JbossAsResourceQuery extends ResourceQuery {
    public List<Resource> getAllNewJBossAS(String platfromName, String profile, String version);

    public List<Resource> getAllInventoriedJBossAS(String platfromName, String profile, String version);

    public List<Resource> getJBossAS(String platfromName, String profileName, String version);

    public Resource getResource(int id);

    public int getCpuCount(int linuxmachinbe);


}
