package org.jboss.rhq.jon.mig.actions;

import java.util.ArrayList;
import java.util.List;

import org.rhq.core.domain.authz.Role;

public class JonActionUserResult extends JonActionResult {

	private List<Role> roles = new ArrayList<Role>(0);

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	
}
