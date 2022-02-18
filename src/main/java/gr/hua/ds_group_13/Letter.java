package gr.hua.ds_group_13;


import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "letters")
public class Letter {

    @Id
    private String letter_id;
    @OneToOne
    @JoinColumn(name = "app_id")
    private Application AppID;
    private String profFName;
    private String profLName;
    private String body;
    private String receiverEmail;

    public Letter() {

    }

    public Letter(String profFName, String profLName, String body, Application AppId, String receiverEmail) {
        this.AppID = AppId;
        this.letter_id = UUID.randomUUID().toString();
        this.profFName = profFName;
        this.profLName = profLName;
        this.body = body;
        this.receiverEmail = receiverEmail;
    }

    public String getId() {
        return letter_id;
    }

    public void setId(String id) {
        this.letter_id = id;
    }

    public Application getAppID() {
        return AppID;
    }

    public void setAppID(Application appID) {
        AppID = appID;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getProfFName() {
        return profFName;
    }

    public void setProfFName(String profFName) {
        this.profFName = profFName;
    }

    public String getProfLName() {
        return profLName;
    }

    public void setProfLName(String profLName) {
        this.profLName = profLName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLetter_id() {
        return letter_id;
    }

    public void setLetter_id(String letter_id) {
        this.letter_id = letter_id;
    }
}
