package gr.hua.ds_group_13;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "email")
public class EmailSenderProperties {
    public String smtpServer;
    public String smtpPort;
    public String smtpSSL;
    public String smtpAuth;
    public String email;
    public String password;


    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public void setSmtpSSL(String smtpSSL) {
        this.smtpSSL = smtpSSL;
    }

    public void setSmtpAuth(String smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public String getSmtpSSL() {
        return smtpSSL;
    }

    public String getSmtpAuth() {
        return smtpAuth;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
