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
