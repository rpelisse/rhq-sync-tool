package org.jboss.rhq.jon.mig.cli;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

/**
 * 
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public class ActionTypeConverter implements IStringConverter<ActionType> {

	@Override
	public ActionType convert(String parameter) {
		if ( parameter == null || "".equals(parameter) ) 
			throw new ParameterException(new NullPointerException());
		for ( ActionType action : ActionType.values())
			if ( action.toString().equalsIgnoreCase(parameter) )
				return action;
		throw new UnsupportedOperationException("Unsupported action:" + parameter);
	}
}
