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

package org.jboss.rhq.sync.tool.util;

/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 20.04.11
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionPropsTemplate extends QuickFileTemplate {
    private String templatename;
    private String saveToFile;

    /*

     */
    public ConnectionPropsTemplate(String templatename, String saveToFile) {
        this.templatename = templatename;
        this.saveToFile = saveToFile;
    }

    private int i = 0;

    public void add(String platform,
                    String profile,
                    String version,
                    String principal,
                    String javaHome,
                    String javaHomePath,
                    String shutdownMethod,
                    String shutdownScript,
                    String credentials) {

        if ((shutdownMethod != null && !shutdownMethod.equals("SCRIPT")) || (shutdownScript != null && !shutdownScript.equals("bin/jon_jboss_stop.sh")) || (credentials != null && credentials.equals("6JXVpjzJ93a")))
            this.appendToBody("<tr class=\"error\">");
        else
            this.appendToBody("<tr>\n");
        addCell("" + i++);
        addCell(platform);
        addCell(profile);
        addCell(version);
        addCell(principal);
        addCell(javaHome);
        addCell(javaHomePath);
        addCell(shutdownMethod);
        addCell(shutdownScript);
        addCell(credentials);
        this.appendToBody("</tr>\n");
    }

    private void addCell(String content) {
        this.appendToBody("\t<td>" + content + "</td>\n");

    }

    @Override
    protected String getTemplateName() {
        return saveToFile;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getTemplate() {
        return getTemplateFromFile(templatename);
    }
}
