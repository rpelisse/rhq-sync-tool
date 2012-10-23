package org.jboss.rhq.jon.mig.query;

import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.authz.Role;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: 11/1/11
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserQuery {
    public List<Subject> getAllUsers();
    public Collection<Subject> addAlllUsers(Collection<Subject> subjects);
	public Collection<Subject> retrievedExistingSubjectsAmong(Collection<Subject> subjects);

    public List<Role> getallRoles();
    public Collection<Role> addAllRoles(Collection<Role> roles);
    public Role addRole(Role role);
	public Collection<Role> findRolesByNames(Role role);
	
	public void addSubjectsToRole(Role role, Collection<Subject> subjects);
}
