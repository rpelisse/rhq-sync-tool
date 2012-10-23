package org.jboss.rhq.sync.tool.query;

import java.util.List;

import org.rhq.core.domain.resource.ResourceType;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 1/17/12
 * Time: 10:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ResourceTypeQuery {
    public List<ResourceType> findAllResourceTypes();
    public ResourceType findResourceType(String name, String pluginType, String resourceCategory);

}
