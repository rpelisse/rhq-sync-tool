package org.jboss.rhq.sync.tool.cli;

import java.net.MalformedURLException;
import java.net.URL;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

public class URLConverter implements IStringConverter<URL> {

	@Override
	public URL convert(String arg0) {
		try {
			return new URL(arg0);
		} catch (MalformedURLException e) {
			throw new ParameterException(e);
		}
	}

}
