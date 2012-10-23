package org.jboss.rhq.sync.tool.query;

import java.util.List;

import org.rhq.core.domain.criteria.ResourceTypeCriteria;
import org.rhq.core.domain.resource.ResourceCategory;
import org.rhq.core.domain.resource.ResourceType;
import org.rhq.enterprise.server.resource.ResourceTypeManagerRemote;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 1/17/12
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceTypeQueryImpl extends BaseResourceQuery implements ResourceTypeQuery {
    ResourceTypeManagerRemote resourceTypeManagerRemote = null;

    public ResourceTypeQueryImpl() {
        resourceTypeManagerRemote = baseRemote.getResourceTypeManagerRemote();

    }

    @Override
    public List<ResourceType> findAllResourceTypes() {

        ResourceTypeCriteria resourceTypeCrit = new ResourceTypeCriteria();
        resourceTypeCrit.fetchMetricDefinitions(true);
        List<ResourceType> resourceTypeList = resourceTypeManagerRemote.findResourceTypesByCriteria(baseRemote.getSubject(), resourceTypeCrit);

        return resourceTypeList;
    }

    @Override
    public ResourceType findResourceType(String name, String pluginType, String resourceCategory) {
        ResourceTypeCriteria crit = new ResourceTypeCriteria();
        crit.addFilterName(name);
        crit.addFilterCategory(ResourceCategory.valueOf(resourceCategory));
        crit.addFilterPluginName(pluginType);
        crit.fetchMetricDefinitions(true);
        crit.setStrict(true);
        List<ResourceType>  resourceTypes = resourceTypeManagerRemote.findResourceTypesByCriteria(baseRemote.getSubject(),crit);
        if(resourceTypes.size()>1){
            throw new IllegalStateException("More than one resource type found. Expected only one resource type") ;

        }
        return resourceTypes.get(0);

    }



}
