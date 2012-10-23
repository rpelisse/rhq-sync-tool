package org.jboss.rhq.sync.tool.actions.impl.impex;

import org.rhq.core.domain.resource.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/22/11
 * Time: 12:55 AM
 * only overrides basic properties
 */
public interface PropertyValueOverride {
    public String getPropertyToOverride();

    /**
     * interface sets an overide of a property., this is in the case when the context of the property need to be taken into consideration
     *
     * @param value
     * @param resource
     * @return new value to set
     */
    public String overrideValue(String value, Resource resource);
}
