package org.jboss.rhq.sync.tool.query;

import java.util.List;

import org.rhq.core.domain.resource.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/3/11
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ResourceQuery {
    /**
     * a string sperated by the following ->
     * example=   imckinle.csb-embedded=>RHQ Agent=>JVM
     *
     * @param string
     * @return
     */
    public Resource getResourceByParents(String string);

    public Resource getResource(int id);

    public List<Resource> getResourceByNameAndParentID(String name, int parentid);

    public Resource getResourceAll(int id);

    public List<Resource> getResourceByName(String name);
    
    public List<Resource> getResourceByFilter(String filter);

    public List<Resource> getResourceByNameAndType(String resourceName, String resourceType);

    List<Resource> getResourceByParentsAndType(String resourceName, String resourceType);

}
