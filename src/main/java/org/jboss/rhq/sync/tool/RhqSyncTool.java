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

package org.jboss.rhq.sync.tool;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.cli.ActionType;
import org.jboss.rhq.sync.tool.cli.CLIAction;
import org.jboss.rhq.sync.tool.cli.Exporter;
import org.jboss.rhq.sync.tool.cli.Importer;
import org.jboss.rhq.sync.tool.cli.PasswordEncoder;
import org.jboss.rhq.sync.tool.cli.QualifierType;
import org.jboss.rhq.sync.tool.cli.TargetDirectoryValidator;
import org.jboss.rhq.sync.tool.util.FileUtils;
import org.jboss.rhq.sync.tool.util.PropertiesLoaderUtil;
import org.jboss.rhq.sync.tool.util.SystemUtils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * <p>
 * JBoss Operation Network Export System (JONES)
 * </p>
 *
 * <p>
 * This class exposed the CLI access to the tool and take care of the
 * marshalling of argument into proper variable and parameter.
 * </p>
 *
 * <em>
 *
 * @author Romain PELISSE - <belaran@redhat.com>
 *
 */
public final class RhqSyncTool {

    private static Logger logger = Logger.getLogger(RhqSyncTool.class);
    private static String PROG_NAME = "rhq-sync-tool";
	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		CLIParameters parameters = RhqSyncTool.extractParameters(args);
		printParameters(parameters);
		// loading properties
		if ( parameters.getPropertiesFile() != null )
			SystemUtils.setSystemProperty(PropertiesLoaderUtil.USER_OVERRIDE_PROPERTIES_FILE,parameters.getPropertiesFile().getAbsolutePath());
		logger.debug("Properties loaded.");
		CLIAction action = actionFactory(parameters.getAction());
		logger.debug("Action created :" + action.toString());
		action.execute(parameters);
	}

	private static CLIAction actionFactory(ActionType action) {
		switch(action) {
			case EXPORT:
				return new Exporter();
			case ENCODE:
				return new PasswordEncoder();
			case IMPORT:
				return new Importer();
		}
		throw new UnsupportedOperationException("ActionType not supported:" + action);
	}

	@SuppressWarnings("unused")
	private static List<String> loadResourcesList(String filename) {
		if ( ! FileUtils.exists(filename) )
			throw new IllegalArgumentException("The file " + filename + " does not exist");
		return FileUtils.readLines(filename);
	}

	private static void printParameters(CLIParameters parameters) {
		logger.debug("Parameters are:");
		if ( parameters.getPropertiesFile() != null )
			printParameter(CLIParameters.PROPERTIES_OPTION, parameters.getPropertiesFile(),parameters.getPropertiesFile().getClass());
		printParameter(CLIParameters.VERBOSE_OPTION, parameters.getVerbose(),parameters.getVerbose().getClass());
		printParameter(CLIParameters.ENCODED_PASSWORD_OPTION, parameters.isEncodedPassword(),Boolean.class);
	}

	private static <T> void printParameter(String name, Object value, Class<T> type) {
		logger.debug(name + ":" + value.toString() + " [" + type + "]");
	}

	private static CLIParameters extractParameters(String[] args) {
		CLIParameters parameters = new CLIParameters();
		JCommander jcommander = null;
		try {
			jcommander = new JCommander(parameters, args);
			jcommander.setProgramName(PROG_NAME);
			if ( parameters.isPrintUsage() ) {
				jcommander.usage();
				System.out.println("Available valids action are :");
				for ( ActionType action : ActionType.values() )
					System.out.println("- " + action.name());
				System.out.println();
				System.out.println("Import/Export actions support the following qualifiers:");
				for ( QualifierType type : QualifierType.values() )
				    System.out.println("- " + type);
				System.out.println("Default value for qualifier is: " + QualifierType.defaultValue());
				System.exit(0);
			} else {
				postValidation(parameters);
			}

		} catch (ParameterException e ) {
			if ( jcommander != null )
				jcommander.usage();
			e.printStackTrace();
			System.exit(1);
		}
		return parameters;
    }

	private static void postValidation(CLIParameters parameters) {
		if ( parameters.getExportDir() == null ) {
			parameters.setExportDir(new File(TargetDirectoryValidator.DEFAULT_DIRECTORY_NAME));
		}
	}
}
