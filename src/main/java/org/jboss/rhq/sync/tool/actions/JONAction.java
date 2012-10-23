package org.jboss.rhq.sync.tool.actions;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 20.04.11
 * Time: 11:22
 * To change this template use File | Settings | File Templates.
 */
public interface JONAction {
    public JonActionResult doAction(Map<String, String> values);
}
