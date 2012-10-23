package org.jboss.rhq.sync.tool.cli;

import org.jboss.rhq.sync.tool.CLIParameters;

public interface CLIAction {
	public void execute(CLIParameters parameters);
}
