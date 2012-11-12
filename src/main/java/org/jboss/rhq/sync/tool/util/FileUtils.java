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

/**
 *
 */
package org.jboss.rhq.sync.tool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.actions.impl.impex.AbstractResourceInventoryExporter;


/**
 * @author rpelisse
 *
 */
public class FileUtils {

	private final static String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator");

    private final static Logger logger = Logger.getLogger(AbstractResourceInventoryExporter.class);

	private FileUtils() {}

	public static String getFileEncoding() {
        String fileEncoding = System.getProperty("file.encoding");
        if ( fileEncoding == null || "".equals(fileEncoding)) {
        	// Fallback to UTF-8 default encoding
        	fileEncoding = "UTF-8";
        }
        return fileEncoding;
	}

	public static boolean exists(String filename) {
		return new File(filename).exists();
	}

    public static void write(String filename, String text) {
        String fileEncoding = FileUtils.getFileEncoding();
        logger.info("Writing to file named " + filename + ". Encoding: " + fileEncoding);
        Writer out = null;
        try {
        	out = new OutputStreamWriter(new FileOutputStream(filename), fileEncoding);
            out.write(text);
            out.flush();
			if (out != null )
				out.close();
        } catch (IOException e) {
			throw new IllegalStateException(e);
		}
    }

	public static void delete(String outputFilename) {
		File file = new File(outputFilename);
		if ( file.exists() && file.canWrite() ) {
			if ( file.isFile() )
				file.delete();
		}
	}
/*
    public static String read(String fileName) {
    	logger.info("Reading from file.");
        String fileEncoding = FileUtils.getFileEncoding();
        StringBuilder text = new StringBuilder();

        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(fileName), fileEncoding);
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine() + SYSTEM_LINE_SEPARATOR);
            }
        } catch (FileNotFoundException e) {
        	throw new IllegalStateException(e);
		} finally {
        	if ( scanner != null )
        		scanner.close();
        }
		logger.info("Text read in: " + text);
        return text.toString();
    }
  */  
    private interface LineProcessor<T> {
    	public void doProcess(String line);
    	public T getResult();
    }

    class ListLineProcessor implements LineProcessor<List<String>> {

    	private List<String> lines = new ArrayList<String>();
		@Override
		public void doProcess(String line) {
			lines.add(line);
		}

		@Override
		public List<String> getResult() {
			return lines;
		}
    	
    }

    class OneLineStringProcessor implements LineProcessor<String> {

        private StringBuilder text = new StringBuilder();
		@Override
		public void doProcess(String line) {
            text.append(line + SYSTEM_LINE_SEPARATOR);			
		}
		
		@Override
		public String getResult() {
			logger.info("Text read in: " + text);
	        return text.toString();
		}    	
    }
        
    public static String read(String fileName) {
    	return read(fileName, new FileUtils().new OneLineStringProcessor()); 
    }
    
    public static List<String> readLines(String fileName) {
    	return read(fileName, new FileUtils().new ListLineProcessor()); 
    }
    
    private static <T> T read(String fileName, LineProcessor<T> processor) {
    	logger.info("Reading from file.");
        String fileEncoding = FileUtils.getFileEncoding();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(fileName), fileEncoding);
            while (scanner.hasNextLine()) {
            	processor.doProcess(scanner.nextLine());

            }
        } catch (FileNotFoundException e) {
        	throw new IllegalStateException(e);
		} finally {
        	if ( scanner != null )
        		scanner.close();
        }
		return processor.getResult();
    }
    
}
