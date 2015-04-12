package com.gibbsdevops.alfred.model.alfred;

import com.gibbsdevops.alfred.model.github.GHPersonRef;

public class AlfredGitUser {

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

}
