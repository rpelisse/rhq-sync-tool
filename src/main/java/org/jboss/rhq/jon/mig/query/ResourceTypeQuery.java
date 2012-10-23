package org.jboss.rhq.jon.mig.query;

import org.rhq.core.domain.resource.ResourceType;

import java.util.List;

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
