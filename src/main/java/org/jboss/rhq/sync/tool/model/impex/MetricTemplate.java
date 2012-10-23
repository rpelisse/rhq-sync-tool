package org.jboss.rhq.sync.tool.model.impex;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 12/3/11
 * Time: 10:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricTemplate {

    private boolean defaultOn;
    private String name;
    private long defaultInterval;
    private boolean perMinute;
    private String description;

    private String measurementCategory;

    public long getDefaultInterval() {
        return defaultInterval;
    }

    public boolean isDefaultOn() {
        return defaultOn;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public boolean isPerMinute() {
        return perMinute;
    }

    public String getMeasurementCategory() {
        return measurementCategory;
    }

    public void setMeasurementCategory(String measurementCategory) {
        this.measurementCategory = measurementCategory;
    }

    public void setDefaultOn(boolean defaultOn) {
        this.defaultOn = defaultOn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultInterval(long defaultInterval) {
        this.defaultInterval = defaultInterval;
    }

    public void setPerMinute(boolean perMinute) {
        this.perMinute = perMinute;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
