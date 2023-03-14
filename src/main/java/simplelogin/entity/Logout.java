/**********************************
*              @2023              *
**********************************/
package simplelogin.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Logout")
public class Logout {

    @Id()
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private Date logoutDate;
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

    public Date getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
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
