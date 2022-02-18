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

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSmtpSSL() {
        return smtpSSL;
    }

    public void setSmtpSSL(String smtpSSL) {
        this.smtpSSL = smtpSSL;
    }

    public String getSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(String smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
