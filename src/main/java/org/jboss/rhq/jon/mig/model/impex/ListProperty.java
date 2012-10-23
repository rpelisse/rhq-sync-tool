package org.jboss.rhq.jon.mig.model.impex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/13/11
 * Time: 11:59 PM
 * To change this template use File | Settings | File Templates.
 */

public class ListProperty extends BasicProperty {

    private List<BasicProperty> propertyList;

    public ListProperty() {
        propertyList = new ArrayList<BasicProperty>();
    }

    public ListProperty(String key) {
        super(key, null);
        propertyList = new ArrayList<BasicProperty>();

    }


    public List<BasicProperty> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<BasicProperty> propertyList) {
        this.propertyList = propertyList;
    }

    public void add(BasicProperty basicProperty) {
        propertyList.add(basicProperty);

    }

    @Override
    public String toString() {
        return super.toString() + ",ListProperty{" +
                "propertyList=" + propertyList +
                '}';
    }
}
