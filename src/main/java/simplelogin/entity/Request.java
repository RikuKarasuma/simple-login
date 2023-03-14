/**********************************
*              @2023              *
**********************************/
package simplelogin.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Request")
public class Request {

    @Id()
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private Date requestDate;
    private String uri;
    private String ip;
    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

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

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
}
