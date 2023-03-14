/**********************************
*              @2023              *
**********************************/
package simplelogin.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Login")
public class Login {

    @Id()
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private Date loginDate;
    private boolean success;
    private String uri;
    private String ip;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
