package gr.hua.ds_group_13;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


public class User {

    @Id
    private String email;
    private String password;
    private String FName;
    private String LName;
    private String PhoneNo;

    public String getAccType() {
        return AccType;
    }

    public void setAccType(String accType) {
        AccType = accType;
    }

    private String AccType;
    /* Account Types:
    ROLE_STUDENT
    ROLE_PROFESSOR
    ROLE_ADMIN
     */


    public User() {

    }

    public User(String email, String password, String FName, String LName, String PhoneNo, String accType) {
        this.email = email;
        this.password = password;
        this.FName = FName;
        this.LName = LName;
        this.PhoneNo = PhoneNo;
        this.AccType = accType;
    }




    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUsername() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }


    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }


}

