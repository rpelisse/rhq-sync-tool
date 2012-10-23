package org.jboss.rhq.sync.tool.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 20.04.11
 * Time: 13:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class QuickFileTemplate {

    private StringWriter bodyWriter;

    protected QuickFileTemplate() {
    }

    protected void appendToBody(String bodyContent) {
        getBodyWriter().append(bodyContent);
    }

    public void printTemplate() {
        try {

            FileWriter writer = new FileWriter(getTemplateName());
            PrintWriter out = new PrintWriter(writer);
            String template = getTemplate();
            String result = template.replace(getBodyPlaceHolder(), bodyWriter.toString());

            out.print(result);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    protected abstract String getTemplateName();

    private StringWriter getBodyWriter() {
        if (bodyWriter == null)
            bodyWriter = new StringWriter();
        return bodyWriter;
    }

    protected abstract String getTemplate();

    protected String getBodyPlaceHolder() {
        return "@body@";
    }

    protected String getTemplateFromFile(String filelocation) {

        File f = new File(filelocation);
        try {
            BufferedReader r = new BufferedReader(new FileReader(f));

            String line = "";

            StringWriter fullTemplate = new StringWriter();
            while ((line = r.readLine()) != null) {
                fullTemplate.append(line + "\n");
            }

            return fullTemplate.toString();
        } catch (IOException e) {

            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e);
        }


    }
}


