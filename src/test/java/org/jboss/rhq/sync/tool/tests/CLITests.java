package org.jboss.rhq.sync.tool.tests;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.jboss.rhq.sync.tool.CLIParameters;
import org.jboss.rhq.sync.tool.RhqSyncTool;
import org.junit.Ignore;
import org.junit.Test;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;


/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
@Ignore("To be updated.")
public class CLITests {

	@Test
	public void missingPropertiesFiles() {
		String[] argv = {"-verbose", "2" };
		try {
			new JCommander(new CLIParameters(), argv);
		} catch ( ParameterException e) {
			return;
		}
		fail("Missing properties files list did NOT trigger an exception.");
	}
	
	@Test
	public void notExistingPropsFile() throws IOException {		
		String[] argv = {"-verbose", "2", CLIParameters.PROPERTIES_OPTION, "/path/to/none/existing/file"  };
		try {
			new JCommander(new CLIParameters(), argv);
		} catch ( ParameterException e) {
			System.out.println(e.getMessage());
			return;
		}
		fail("None existing file did NOT trigger an exception.");
	}

	@Test
	public void unreadablePropsFile() throws IOException {		
		File file = File.createTempFile("propertiesFilePropertiesFiles", ".test.properties");
		file.setReadable(false);
		String[] argv = {"-verbose", "2", CLIParameters.PROPERTIES_OPTION, file.getAbsoluteFile().toString()  };
		try {
			new JCommander(new CLIParameters(), argv);
		} catch ( ParameterException e) {
			System.out.println(e.getMessage());
			return;
		} finally {
			file.delete();
		}
		fail("Unreadable file failed did NOT trigger an exception");
	}
	
	@Test
	public void propertiesFilePropertiesFiles() throws IOException {
		File file = File.createTempFile("propertiesFilePropertiesFiles", ".test.properties");
		
		String[] argv = {"-verbose", "2", CLIParameters.PROPERTIES_OPTION , file.getAbsoluteFile().toString() };
		try {
			RhqSyncTool.main(argv);
		} catch ( ParameterException e) {
			fail("Existing and readable file triggered an exception.");
		} finally {
			file.delete();
		}		
	}
}

