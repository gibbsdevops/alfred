package com.gibbsdevops.alfred.model.alfred;

import com.gibbsdevops.alfred.model.github.GHPersonRef;
import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "alfred_git_user")
public class AlfredGitUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    public static AlfredGitUser from(GHPersonRef pusher) {
        AlfredGitUser user = new AlfredGitUser();
        user.id = null;
        user.name = pusher.getName();
        user.email = pusher.getEmail();
        return user;
    }

    //<editor-fold desc="Getters and Setters">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    //</editor-fold>

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AlfredGitUser)) return false;
        AlfredGitUser o = (AlfredGitUser) obj;
        if (!Objects.equal(id, o.getId())) return false;
        if (!Objects.equal(name, o.getName())) return false;
        if (!Objects.equal(email, o.getEmail())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, email);
    }

    @Override
    public String toString() {
        return "AlfredGitUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
