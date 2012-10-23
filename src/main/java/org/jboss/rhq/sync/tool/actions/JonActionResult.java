package org.jboss.rhq.sync.tool.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 20.04.11
 * Time: 11:24
 * To change this template use File | Settings | File Templates.
 */
public class JonActionResult {
    private JonActionResultType resultType;
    /**
     * for now a list of strings is enough
     */
    private List<String> messages;

    public void addMessage(String msg) {
        if (messages == null)
            messages = new ArrayList<String>();
        messages.add(msg);
    }


    public enum JonActionResultType {
        SUCCESS, FAIL, SUCCESS_WARNINGS
    }

    public JonActionResultType getResultType() {
        return resultType;
    }

    public void setResultType(JonActionResultType resultType) {
        this.resultType = resultType;
    }
}
