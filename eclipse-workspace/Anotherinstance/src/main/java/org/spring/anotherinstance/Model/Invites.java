package org.spring.anotherinstance.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "invites")
public class Invites {
    @Id
    private String id;
    private Date dates;
    private Boolean promoted;
    @DBRef
    private Sessiontask sessioninvite;
    @DBRef
    private User user;


    public Boolean getPromoted() {
        return promoted;
    }

    public void setPromoted(Boolean promoted) {
        this.promoted = promoted;
    }

    public Date getDates() {
        return dates;
    }

    public void setDates(Date dates) {
        this.dates = dates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Sessiontask getSessioninvite() {
        return sessioninvite;
    }

    public void setSessioninvite(Sessiontask sessioninvite) {
        this.sessioninvite = sessioninvite;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Invites() {
    }

}
