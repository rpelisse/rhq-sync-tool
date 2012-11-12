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

package org.jboss.rhq.sync.tool.actions.impl;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jboss.rhq.sync.tool.BaseRemote;
import org.jboss.rhq.sync.tool.LoginConfiguration;
import org.jboss.rhq.sync.tool.actions.JONAction;
import org.jboss.rhq.sync.tool.actions.JonActionResult;
import org.jboss.rhq.sync.tool.util.PropertiesLoaderUtil;
import org.rhq.core.domain.auth.Subject;


/**
 * Created by IntelliJ IDEA.
 * User: uuxgbig
 * Date: 20.04.11
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractJONAction implements JONAction {

    public static final String PLATFORM_NAME = "platformName";
    public static final String PROFILE_NAME = "profileName";
    public static final String ACTION = "action";

    private static Logger logger = Logger.getLogger(AbstractJONAction.class);

    final Properties props;

    public String getProperty(String key) {
        return (String) props.get(key);
    }

    private final LoginConfiguration loginConfiguration;

    protected BaseRemote baseRemote;
    JonActionResult result = null;

    @Override
    public JonActionResult doAction(Map<String, String> values) {
        logger.debug("excuting peform method");
        result = new JonActionResult();
        try {
            JonActionResult.JonActionResultType myresultType = perform(values);
            result.setResultType(myresultType);
        } catch (RuntimeException e) {
            logger.error("Perform method did not successfully complete: " + this.getClass().getName());
            e.printStackTrace();
            result.setResultType(JonActionResult.JonActionResultType.FAIL);
            addResultMessage("ERROR. perform method did not successfully complete. " + e.getMessage());
            return result;
        }
        logger.debug("Got Result " + result);
        logger.debug(" Logging out subject");
        //baseRemote.logout();
        return result;
    }

    /**
     * any messages that need to be given back to the calling code
     *
     * @param message
     */
    protected void addResultMessage(String message) {
        result.addMessage(message);
    }

    protected abstract JonActionResult.JonActionResultType perform(Map<String, String> values);
    
    protected AbstractJONAction() {
        props = PropertiesLoaderUtil.getProperties();
        // hostname -f
        loginConfiguration = new LoginConfiguration((String) props.get("jon.server.user"),
                (String) props.get("jon.server.password"),
                (String) props.get("jboss_jon_server"),
                Integer.valueOf((String) props.get("jon.server.port")));
        baseRemote = BaseRemote.getInstance(loginConfiguration);
    }

    public AbstractJONAction(LoginConfiguration loginConfiguration,BaseRemote baseRemote) {
        props = PropertiesLoaderUtil.getProperties();
        this.baseRemote = baseRemote;
        this.loginConfiguration = loginConfiguration;
	}

	protected Subject getSubject() {
        logger.info("getting logged in jon subject");
        return BaseRemote.getInstance(loginConfiguration).getSubject();
    }
}
