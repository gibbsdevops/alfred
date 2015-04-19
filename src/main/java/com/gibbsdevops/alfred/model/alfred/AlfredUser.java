package com.gibbsdevops.alfred.model.alfred;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gibbsdevops.alfred.model.github.GHOrganization;
import com.gibbsdevops.alfred.model.github.GHPerson;
import com.gibbsdevops.alfred.model.github.GHUser;
import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Field;

@Entity(name = "alfred_user")
public class AlfredUser {

    @Id
    private long id;
    private String login;
    @JsonProperty("html_url")
    private String name;
    private String email;
    private String url;
    @Column(name = "html_url")
    private String htmlUrl;
    @JsonProperty("avatar_url")
    @Column(name = "avatar_url")
    private String avatarUrl;
    private String type;
    private String description;
    @JsonProperty("created_at")
    @Column(name = "created_at")
    private Long createdAt;
    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    private Long updatedAt;

    public static AlfredUser from(GHUser u) {
        if (u == null) throw new NullPointerException("user is null");
        AlfredUser user = new AlfredUser();
        user.id = u.getId();
        user.login = u.getLogin();
        user.url = u.getUrl();
        user.htmlUrl = u.getHtmlUrl();
        user.avatarUrl = u.getAvatarUrl();
        if (u.getCreatedAt() != null && !u.getCreatedAt().isEmpty()) {
//            user.createdAt = u.getCreatedAt();
        }
        if (u.getUpdatedAt() != null && !u.getUpdatedAt().isEmpty()) {
//            user.updatedAt = u.getUpdatedAt();
        }
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
    //</editor-fold>

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof AlfredUser)) return false;
        try {
            for (Field field : getClass().getDeclaredFields()) {
                if (!Objects.equal(field.get(this), field.get(obj))) return false;
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to equals objects", e);
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, login, name, email, url, htmlUrl, avatarUrl, type, description, createdAt,
                updatedAt);
    }

    @Override
    public String toString() {
        return "AlfredUser{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }

}
