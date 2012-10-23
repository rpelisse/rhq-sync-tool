package org.jboss.rhq.jon.mig.query;

import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.rhq.jon.mig.BaseRemote;
import org.rhq.core.domain.criteria.ResourceCriteria;
import org.rhq.core.domain.resource.Resource;
import org.rhq.enterprise.server.resource.ResourceManagerRemote;


/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 3/9/11
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseResourceQuery implements ResourceQuery {
    protected final ResourceManagerRemote resourceManagerRemote;

    private static Logger logger = Logger.getLogger(BaseResourceQuery.class);
    final BaseRemote baseRemote;

    public BaseResourceQuery() {
        this.baseRemote = BaseRemote.getInstance(null);
        this.resourceManagerRemote = baseRemote.getResourceManager();
    }

    public List<Resource> getResourceByParentsAndType(String resourceName, String resourceType) {
        String[] resouceNames = resourceName.split(">");
        Resource parentResource = null;
        List<Resource> r = null;
        int length = resouceNames.length - 1;
        for (int i = 0; i < resouceNames.length; i++) {
            String resource = resouceNames[i];
            if (parentResource != null) {
                r = getResourceByNameAndParentID(resource.trim(), parentResource.getId());
            } else {
                ResourceCriteria parentRC = new ResourceCriteria();

                parentRC.setStrict(true);
                parentRC.fetchAgent(false);
                parentRC.fetchChildResources(false);
                parentRC.addFilterName(resource.trim());
                if (i == length)
                    parentRC.addFilterResourceTypeName(resourceType);
                r = resourceManagerRemote.findResourcesByCriteria(baseRemote.getSubject(), parentRC);
            }
        }
        return r;
    }

    public Resource getResourceByParents(String parentedString) {
        String[] resouceNames = parentedString.split(">");
        Resource parentResource = null;
        for (String resource : resouceNames) {
            List<Resource> r = null;
            if (parentResource != null) {
                r = getResourceByNameAndParentID(resource.trim(), parentResource.getId());
            } else {
                ResourceCriteria parentRC = new ResourceCriteria();
                parentRC.setStrict(true);
                parentRC.fetchAgent(false);
                parentRC.fetchChildResources(false);
                parentRC.addFilterName(resource.trim());
                r = resourceManagerRemote.findResourcesByCriteria(baseRemote.getSubject(), parentRC);
            }
            parentResource = resourceListSize(r, resource);;
        }
        return getResource(parentResource.getId());
    }

    private Resource resourceListSize(List<Resource> r, String resource) {
    	Resource result = null;
        if (r.size() > 1) {
        	logger.warn("More than one resources while expecting only one for resource [" + resource + "]:");
        	for ( Resource res : r) {
        		logger.warn(res.getName());
        		if (result == null &&  resource.equals(res.getName())) {
                	logger.warn("Keep the first one matching " + resource + ":" + res.getId());
                	result = res;
        		}        			
        	}
        	if ( result == null )
        		throw new IllegalArgumentException("More than one resource found. Expected to find one resource for resouce name" + resource);
        	
        } else if (r.size() == 0) {
            throw new IllegalArgumentException("No resource found. Expected to find one resource for resouce name" + resource);
        }  else
        	result = r.get(0);
        return result;
    }
    
    public Resource getResource(int id) {
        return baseRemote.getResourceManager().getResource(baseRemote.getSubject(), id);
    }

    public Resource getResourceAll(int id) {
        ResourceCriteria resourceCriteria = new ResourceCriteria();

        resourceCriteria.setStrict(true);
        resourceCriteria.fetchAgent(true);
        // resourceCriteria.fetchChildResources(true);
        resourceCriteria.fetchResourceConfiguration(true);
        resourceCriteria.fetchPluginConfiguration(true);
        resourceCriteria.addFilterId(id);
        return getResource(id);
    }


    public List<Resource> getResourceByName(String name) {
        ResourceCriteria resourceCriteria = new ResourceCriteria();

        resourceCriteria.setStrict(true);
        resourceCriteria.fetchAgent(true);
        resourceCriteria.fetchChildResources(true);
        resourceCriteria.addFilterName(name);
        return resourceManagerRemote.findResourcesByCriteria(baseRemote.getSubject(), resourceCriteria);
    }

    public List<Resource> getResourceByNameAndParentID(String name, int parentid) {
        ResourceCriteria resourceCriteria = new ResourceCriteria();

        resourceCriteria.setStrict(true);
        resourceCriteria.fetchAgent(false);
        resourceCriteria.fetchChildResources(false);
        resourceCriteria.addFilterName(name);
        resourceCriteria.addFilterParentResourceId(parentid);
        return resourceManagerRemote.findResourcesByCriteria(baseRemote.getSubject(), resourceCriteria);
    }

    @Override
    public List<Resource> getResourceByFilter(String filter) {
        ResourceCriteria resourceCriteria = new ResourceCriteria();
        resourceCriteria.setStrict(false);
        resourceCriteria.fetchAgent(false);
        resourceCriteria.fetchChildResources(false);
        resourceCriteria.addFilterName(filter);
        return resourceManagerRemote.findResourcesByCriteria(baseRemote.getSubject(), resourceCriteria);
    }
    
    
    /**
     * returns a list of resources
     *
     * @param resourceName
     * @param resourceType
     * @return
     */
    public List<Resource> getResourceByNameAndType(String resourceName, String resourceType) {
        ResourceCriteria resourceCriteria = new ResourceCriteria();
        resourceCriteria.addFilterResourceTypeName(resourceType);
        resourceCriteria.setStrict(true);
        resourceCriteria.addFilterName(resourceName);
        return resourceManagerRemote.findResourcesByCriteria(baseRemote.getSubject(), resourceCriteria);
    }

    @SuppressWarnings("unused")
    private List<Resource> executeQuery(ResourceCriteria resourceCriteria) {
        return resourceManagerRemote.findResourcesByCriteria(baseRemote.getSubject(), resourceCriteria);
    }


}
