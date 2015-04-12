package com.gibbsdevops.alfred.model.alfred;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gibbsdevops.alfred.model.github.GHOrganization;
import com.gibbsdevops.alfred.model.github.GHPerson;
import com.gibbsdevops.alfred.model.github.GHUser;

public class AlfredUser {

    private long id;
    private String login;
    @JsonProperty("html_url")
    private String name;
    private String email;
    private String url;
    private String htmlUrl;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String type;
    private String description;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

    public static AlfredUser from(GHUser u) {
        if (u == null) throw new NullPointerException("user is null");
        AlfredUser user = new AlfredUser();
        user.id = u.getId();
        user.login = u.getLogin();
        user.url = u.getUrl();
        user.htmlUrl = u.getHtmlUrl();
        user.avatarUrl = u.getAvatarUrl();
        user.createdAt = u.getCreatedAt();
        user.updatedAt = u.getUpdatedAt();
        return user;
    }

    public static AlfredUser from(GHOrganization org) {
        if (org == null) throw new NullPointerException("org is null");
        AlfredUser user = from((GHUser) org);
        user.type = "Organization";
        user.description = org.getDescription();
        return user;
    }

    public static AlfredUser from(GHPerson person) {
        if (person == null) throw new NullPointerException("person is null");
        AlfredUser user = from((GHUser) person);
        user.type = "User";
        user.name = person.getName();
        user.email = person.getEmail();
        return user;
    }

    //<editor-fold desc="Getters and Setters">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    //</editor-fold>

}
