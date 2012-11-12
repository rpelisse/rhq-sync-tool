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

package org.jboss.rhq.sync.tool;

import static org.jboss.rhq.sync.tool.util.PasswordUtil.decode;

/**
 * Created by IntelliJ IDEA.
 * User: imckinle
 * Date: Mar 7, 2011
 * Time: 11:21:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoginConfiguration {
    // A remote session always starts with a login, define default user/password/server/port
    private String userName = "rhqadmin";
    private String password = "rhqadmin";
    private String host = "localhost";
    private int port = 7080;

    public LoginConfiguration(String username, String password, String host, int port) {
        this.userName = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return decode(password);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}

