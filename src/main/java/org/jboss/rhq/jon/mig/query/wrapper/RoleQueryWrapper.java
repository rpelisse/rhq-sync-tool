package org.jboss.rhq.jon.mig.query.wrapper;

import org.rhq.core.domain.auth.Subject;
import org.rhq.core.domain.authz.Role;
import org.rhq.core.domain.criteria.Criteria;
import org.rhq.core.domain.criteria.RoleCriteria;
import org.rhq.core.domain.util.PageList;
import org.rhq.enterprise.server.authz.RoleManagerRemote;


/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class RoleQueryWrapper extends AbstractQueryWrapper<Role> {

    private RoleManagerRemote roleManager;
	
	public RoleQueryWrapper(RoleManagerRemote roleManager, Subject subject) {
		super(subject);
		this.roleManager = roleManager; 
	}
	
	@Override
	public PageList<Role> doPagedQuery(Criteria criteria) {
		return (criteria instanceof RoleCriteria) ? roleManager.findRolesByCriteria(this.subject, (RoleCriteria)criteria) : invalidCriteriaInstance(criteria);
	}

}
