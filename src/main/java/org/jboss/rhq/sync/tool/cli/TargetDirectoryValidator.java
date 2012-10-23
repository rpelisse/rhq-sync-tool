package org.jboss.rhq.sync.tool.cli;

import java.io.File;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class TargetDirectoryValidator implements IParameterValidator {

	public static final String DEFAULT_DIRECTORY_NAME = "target";
	
	@Override
	public void validate(String name, String value) throws ParameterException {
		if ( value != null ) {
			File file = new File(value);	
			if ( ! file.exists() ) {
				createDir(value);
				return;
			}
			if ( ! new File(value).isDirectory() )
				throw new ParameterException("The file " + value + " is not a directory. Target directory must be a directory.");
		} else {
			createDir(DEFAULT_DIRECTORY_NAME);
		}
	}
	
	private static void createDir(String dirname) {
		new File(dirname).mkdirs();
	}
}
