package com.gibbsdevops.alfred.model.events.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {

    private int id;
    private String name;
    @JsonProperty("full_name")
    private String fullName;
    private User owner;
    private String url;
    @JsonProperty("git_url")
    private String gitUrl;
    @JsonProperty("ssh_url")
    private String sshUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
