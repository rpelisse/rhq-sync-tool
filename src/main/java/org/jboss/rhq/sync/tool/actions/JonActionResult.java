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
