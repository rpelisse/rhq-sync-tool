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
 * Date: 10.06.11
 * Time: 13:20
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorCountTemplate extends QuickFileTemplate {

    private String templatename;
    private String saveToFile;
    ;

    public ProcessorCountTemplate(String templatename, String saveToFile) {
        this.templatename = templatename;
        this.saveToFile = saveToFile;
    }

    public void add(String group, String name, String resourcekey, String version, String parentResource) {

        this.appendToBody("<tr class=\"" + group + "\">");
        addCell(group);
        addCell(name);
        addCell(resourcekey);
        addCell(version);
        addCell(parentResource);
        this.appendToBody("</tr>\n");
    }

    public void addSummary(String name, String value) {
        this.appendToBody("<tr class=\"\">");
        this.appendToBody("\t<td colspan=\"4\">Total count for " + name + "</td>\n");
        this.appendToBody("\t<td>" + value + "</td></tr>\n");

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

    public void addHeader(String group, String s) {
        this.appendToBody("<tr class=\"\">");
        this.appendToBody("\t<td colspan=\"5\">Report for group = " + group + "</td></tr>\n");


    }
}
