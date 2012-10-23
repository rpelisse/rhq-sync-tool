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
