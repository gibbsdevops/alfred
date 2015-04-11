package com.gibbsdevops.alfred.model.github;

/**
 * Created by sgibbs on 4/7/2015.
 */
public class GHPersonRef {

    private String name;
    private String email;
    private String username;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
