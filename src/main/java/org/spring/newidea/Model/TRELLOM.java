package org.spring.newidea.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trello")
public class TRELLOM {
    @Id
    private String id;
    private String status;
    private String title;
    private String details;
    private Boolean completed;
    @DBRef
    private Sessiontask task;

    @DBRef
    private User user;
public TRELLOM(){}
    public String getId() {
        return id;
    }

    public Sessiontask getTask() {
        return task;
    }

    public void setTask(Sessiontask task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
