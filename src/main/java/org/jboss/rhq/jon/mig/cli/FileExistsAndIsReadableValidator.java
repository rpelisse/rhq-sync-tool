package org.jboss.rhq.jon.mig.cli;

import java.io.File;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class FileExistsAndIsReadableValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		if ( value != null ) {
			File file = new File(value);	
			if ( ! file.exists() )
				throw new ParameterException("The file " + value + " provided for argument " + name + " does not exist");
			if ( ! file.canRead() )
				throw new ParameterException("The file " + value + " provided for argument " + name + " is not readable");
		}
	}
}
