package org.jboss.rhq.sync.tool.util;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.authz.Permission;
import org.rhq.core.domain.authz.Role;

/**
 *
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public final class LogUtils {

    private LogUtils() {}

    public static void logSubjects(Logger logger, Collection<Subject> subjects) {
        for ( Subject subject : subjects )
            if ( logger.isDebugEnabled() )
                logger.debug(subject);
    }

    public static void logRoleInstance(Logger logger, Role role, String message) {
        if ( logger.isDebugEnabled() ) {
            logger.debug(message);
            logger.debug("Instance of " + Role.class + ":[" + role.toString() + "], with values:");
            logger.debug(role.getClass().toString());
            logger.debug(role.getDescription());
            logger.debug(role.getFsystem());
            logger.debug(role.getId());
            logger.debug(role.getMemberCount());
            logger.debug(role.getName());
            logger.debug("Permissions:");
            for ( Permission permission : role.getPermissions() )
                logger.debug("- " + permission);
            logger.debug("Subject:");
            for ( Subject subject : role.getSubjects() )
                logger.debug("- " + subject.toString());
        }
    }

    public static void logSubjectInstance(Logger logger, Subject subject,
            String string) {
        if ( logger.isDebugEnabled() ) {
            logger.debug(string);
            logger.debug("Instance of " + Subject.class + ":[" + subject.toString() + "], with values:");
            logger.debug(subject.getClass().toString());
            logger.debug(subject.getId());
            logger.debug(subject.getName());
            logger.debug(subject.getFirstName());
            logger.debug(subject.getLastName());
            logger.debug(subject.getEmailAddress());
            logger.debug(subject.getDepartment());
            logger.debug(subject.getPhoneNumber());
            logger.debug(subject.getSmsAddress());
            logger.debug(subject.getFsystem());
            logger.debug(subject.getFactive());
            logger.debug(subject.getLdapRoles());
            logger.debug(subject.getOwnedGroups());
        }
    }

}
