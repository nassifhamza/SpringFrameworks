package org.spring.newidea.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "JOBS")
public class JOBS {
    @Id
    private String id;
    private String title;
    private  String name;
    private String specs;
    private String skills;
    private double salary;
    @DBRef
   private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String spes) {
        this.specs = spes;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public double getSalary() {
        return salary;
    }



    public JOBS() {
    }

    @Override
    public String toString() {
        return "JOBS{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", spes='" + specs + '\'' +
                ", skills='" + skills + '\'' +
                ", salary=" + salary +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
