package gr.hua.ds_group_13;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    private String appId;
    private String fromMail;
    private String profEmail;
    private String Body;
    private String StudFName;
    private String StudLName;
    private Boolean Accepted;
    private String timeStamp;
    private String letterReceiverEmail;
    private String letterId;

    public Application(){

    }



    public Application(String profEmail, String body, String studFName, String studLName,String fromMail,String letterReceiverEmail) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.timeStamp=dtf.format(now);
        this.fromMail=fromMail;
        this.appId = UUID.randomUUID().toString();
        this.profEmail = profEmail;
        Body = body;
        StudFName = studFName;
        StudLName = studLName;
        this.letterReceiverEmail =letterReceiverEmail;
    }


    public String getLetterId() {
        return letterId;
    }

    public void setLetterId(String  letterId) {
        this.letterId = letterId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getLetterReceiverEmail() {
        return letterReceiverEmail;
    }

    public void setLetterReceiverEmail(String letterReceiverEmail) {
        this.letterReceiverEmail = letterReceiverEmail;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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
        this.Body = body;
    }

    public String getStudFName() {
        return StudFName;
    }

    public void setStudFName(String studFName) {
        this.StudFName = studFName;
    }

    public String getStudLName() {
        return StudLName;
    }

    public void setStudLName(String studLName) {
        this.StudLName = studLName;
    }

    public Boolean getAccepted() {
        return Accepted;
    }

    public void setAccepted(Boolean accepted) {
        Accepted = accepted;
    }

    public String getFromMail() {
        return fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }
}

