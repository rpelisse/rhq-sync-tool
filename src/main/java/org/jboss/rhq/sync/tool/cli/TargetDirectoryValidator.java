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
