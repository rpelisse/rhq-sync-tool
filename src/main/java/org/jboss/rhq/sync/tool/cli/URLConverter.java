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
