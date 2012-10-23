package org.jboss.rhq.jon.mig.cli;

/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public enum QualifierType {

    ALL, METRICS,ROLES, RESOURCES;

    public static QualifierType defaultValue() {
        return ALL;
    }
}
