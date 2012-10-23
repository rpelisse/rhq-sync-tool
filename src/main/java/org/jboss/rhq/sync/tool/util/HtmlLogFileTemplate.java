package org.jboss.rhq.sync.tool.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 20.04.11
 * Time: 13:18
 * To change this template use File | Settings | File Templates.
 */
public class HtmlLogFileTemplate extends QuickFileTemplate {
    private String templateName;

    public HtmlLogFileTemplate(String template) {
        this.templateName = template;
    }

    public void add(String platform, String profile, String version, String logFile, String enabled, String serverity) {
        if (enabled.equals("false"))
            this.appendToBody("<tr class=\"error\">");
        else
            this.appendToBody("<tr>\n");
        addCell(platform);
        addCell(profile);
        addCell(version);
        addCell(logFile);
        addCell(enabled);
        addCell(serverity);
        this.appendToBody("</tr>\n");
    }

    private void addCell(String content) {
        this.appendToBody("\t<td>" + content + "</td>\n");

    }

    @Override
    protected String getTemplateName() {

        SimpleDateFormat df = new SimpleDateFormat("yyMMdd_HH_mm");
        return "disabledLog" + df.format(new Date()) + ".html";
    }

    @Override
    protected String getTemplate() {
        return getTemplateFromFile(templateName);
    }

    /**
     * just a silly string with a place holder called @body@
     *
     * @return
     */
    //@Override
    protected String sgetTemplate() {
        return "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n" +
                "        \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Disabled Log File Results</title>\n" +
                "<style type=\"text/css\">\n" +
                "    table{\n" +
                "       border-collapse:collapse;\n" +
                "    \n" +
                "    }\n" +
                "    table, th, td{\n" +
                "        border: 1px solid black;\n" +
                "    }\n" +
                "    td{\n" +
                "        text-align:left;\n" +
                "    padding:3px;\n" +
                "    }\n" +
                " tr.error{\n" +
                "     background-color:yellow;\n" +
                "    }" +

                "</style>" +
                "</head>\n" +
                "<body>\n" +
                "<table>\n" +
                "    <tr>\n" +
                "        <th>Platform</th>\n" +
                "        <th>Profile</th>\n" +
                "        <th>Version</th>\n" +
                "        <th>File</th>\n" +
                "        <th>Enabled</th>\n" +
                "        <th>Serverity</th>\n" +
                "    </tr>\n" +
                "    <tbody>\n" +
                "    @body@\n" +
                "    </tbody>\n" +
                "    \n" +
                "</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>";  //To change body of implemented methods use File | Settings | File Templates.
    }

}
