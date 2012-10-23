package org.jboss.rhq.jon.mig.model.impex;

//import com.sun.istack.internal.Nullable;


/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 7/12/11
 * Time: 12:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class BasicProperty {
    private String key;
    private String value;

    public BasicProperty() {
    }

    public BasicProperty(String key, /* FIXME: @Nullable*/ String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BasicProperty{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
