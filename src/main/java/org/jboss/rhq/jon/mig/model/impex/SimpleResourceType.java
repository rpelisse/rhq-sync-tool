package org.jboss.rhq.jon.mig.model.impex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 1/17/12
 * Time: 11:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleResourceType {
     private  String resourceTypeName;
    private String resourceTypePlugin;
    private String resourceCategory;
    private List<MetricTemplate> metricTemplates;

    public String getResourceCategory() {
        return resourceCategory;
    }

    public List<MetricTemplate> getMetricTemplates() {
        return metricTemplates;
    }

    public void setMetricTemplates(List<MetricTemplate> metricTemplates) {
        this.metricTemplates = metricTemplates;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public String getResourceTypePlugin() {
        return resourceTypePlugin;
    }

    public void setResourceTypePlugin(String resourceTypePlugin) {
        this.resourceTypePlugin = resourceTypePlugin;
    }

    public void addMetricTemplate(MetricTemplate metricTemplate) {
        if(metricTemplates==null){
            metricTemplates = new ArrayList<MetricTemplate>();
        }
           metricTemplates.add(metricTemplate);
    }

    public void setResourceCategory(String resourceCategory) {
        this.resourceCategory = resourceCategory;
    }
}
