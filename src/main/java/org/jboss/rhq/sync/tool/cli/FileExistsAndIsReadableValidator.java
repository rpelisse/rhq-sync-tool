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
