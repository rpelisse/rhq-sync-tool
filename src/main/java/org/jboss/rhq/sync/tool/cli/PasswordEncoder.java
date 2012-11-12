/*
*
* RHQ Sync Tool
* Copyright (C) 2012-2013 Red Hat, Inc.
* All rights reserved.
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License,
* version 2.1, as published by the Free Software Foundation.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License and the GNU Lesser General Public License
* for more details.
*
* You should have received a copy of the GNU General Public License
* and the GNU Lesser General Public License along with this program;
* if not, write to the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*
*/

package org.jboss.rhq.sync.tool.cli;

import org.jboss.rhq.sync.tool.CLIParameters;

import static org.jboss.rhq.sync.tool.util.PasswordUtil.encode;
import static org.jboss.rhq.sync.tool.util.PasswordUtil.decode;

/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class PasswordEncoder implements CLIAction {

	@Override
	public void execute(CLIParameters parameters) {
		if ( parameters == null )
			throw new IllegalStateException("No proper instance of " + CLIParameters.class + " provided (null)");

		try {
			encodePassword(validatePassword(parameters.getPassword()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String validatePassword(String password) {
		if ( password == null || "".equals(password) )
			throw new IllegalArgumentException("Provided password is 'null' or empty.");
		return password;
	}

	private void encodePassword(String password) throws Exception {
		System.out.println("Masking password:[" + password + "]");
		String encodedPassword = encode(password);
		System.out.println("Masked password: " + encodedPassword);
		System.out.println("decoded " + decode(encodedPassword));
	}

}
