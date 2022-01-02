package gr.hua.ds_group_13;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    private String AppId;
    private String profEmail;
    private String Body;
    private String StudFName;
    private String StudLName;
    private Boolean Accepted;

    public Application(){

    }

    public Application(String profEmail, String body, String studFName, String studLName) {
        AppId = UUID.randomUUID().toString();
        this.profEmail = profEmail;
        Body = body;
        StudFName = studFName;
        StudLName = studLName;
    }

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }

    public String getProfEmail() {
        return profEmail;
    }

    public void setProfEmail(String profEmail) {
        this.profEmail = profEmail;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getStudFName() {
        return StudFName;
    }

    public void setStudFName(String studFName) {
        StudFName = studFName;
    }

    public String getStudLName() {
        return StudLName;
    }

    public void setStudLName(String studLName) {
        StudLName = studLName;
    }

    public Boolean getAccepted() {
        return Accepted;
    }

    public void setAccepted(Boolean accepted) {
        Accepted = accepted;
    }
}

