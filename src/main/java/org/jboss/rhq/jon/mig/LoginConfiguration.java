package org.jboss.rhq.jon.mig;

import static org.jboss.rhq.jon.mig.util.PasswordUtil.decode;

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

