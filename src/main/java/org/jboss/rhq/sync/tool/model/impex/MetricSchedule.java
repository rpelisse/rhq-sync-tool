package org.jboss.rhq.sync.tool.model.impex;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/3/11
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
//todo figure out method to record the base line too http://docs.redhat.com/docs/en-US/JBoss_Operations_Network/2.4/html/API_Guides/domain/index.html?org/rhq/core/domain/measurement/package-summary.html

public class MetricSchedule {
    ;

    List<String> resourceName;
    /**
     * http://docs.redhat.com/docs/en-US/JBoss_Operations_Network/2.4/html/API_Guides/domain/index.html?org/rhq/core/domain/measurement/package-summary.html
     * See org.rhq.core.domain.resource.ResourceType
     * JBossAS Server,RHQ Agent,Linux
     */
    String resourceType;
    List<Schedule> schedules;


    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public List<String> getResourceName() {
        return resourceName;
    }

    public void setResourceName(List<String> resourceName) {
        this.resourceName = resourceName;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
