package org.jboss.rhq.jon.mig.cli;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

/**
 * 
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class QualifierTypeConverter implements IStringConverter<QualifierType> {

	@Override
	public QualifierType convert(String parameter) {
		if ( parameter == null || "".equals(parameter) ) 
			throw new ParameterException(new NullPointerException());
		for ( QualifierType qualifier : QualifierType.values())
			if ( qualifier.toString().equalsIgnoreCase(parameter) )
				return qualifier;
		throw new UnsupportedOperationException("Unsupported qualifier:" + parameter);
	}
}
