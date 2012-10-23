package org.jboss.rhq.jon.mig.query;

import org.rhq.core.domain.resource.Resource;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/3/11
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Query {
    public Resource getResource(int id);

    public Resource getResourceAll(int id);

    public List<Resource> getResourceByName(String name);

    public List<Resource> getResourceByNameAndType(String resourceName, String resourceType);
}
