package org.jboss.rhq.jon.mig.query;

import org.rhq.core.domain.measurement.MeasurementDefinition;
import org.rhq.core.domain.measurement.MeasurementSchedule;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 6/24/11
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MetricResourceQuery {
    public List<MeasurementDefinition> findAllJbossMetric(String platform);

    public List<MeasurementSchedule> findAllSchedulesForResource(int resourceId);

    public MeasurementSchedule findMeasurementSchedule(String name, String displayName, String dataType, int resourceid);

    public void updateMeasurementSchedule(MeasurementSchedule measurementSchedule);


}
