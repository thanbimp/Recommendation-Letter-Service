package gr.hua.ds_group_13;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;


@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    private String email;
    private String password;
    private String FName;
    private String LName;
    /* Account Types:
    0:Student
    1:Professor
    2:Admin (must be set manually in DB)
     */
    private short AccType;



    public User(){

    }

    public User(String email, String password, String FName, String LName,short AccType) {
        this.email = email;
        this.password = password;
        this.FName = FName;
        this.LName = LName;
        this.AccType=AccType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public short getAccType() {
        return AccType;
    }

    public void setAccType(short accType) {
        AccType = accType;
    }
}
