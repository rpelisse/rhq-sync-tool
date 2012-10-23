package org.jboss.rhq.sync.tool.cli;

import java.net.MalformedURLException;
import java.net.URL;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class URLValidation implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		try {
			new URL(value);
		} catch (MalformedURLException e) {
			throw new ParameterException("Invalid URL format:" + value);
		}
	}

}
