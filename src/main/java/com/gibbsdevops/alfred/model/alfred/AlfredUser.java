package com.gibbsdevops.alfred.model.alfred;

import com.gibbsdevops.alfred.model.github.GHOrganization;
import com.gibbsdevops.alfred.model.github.GHPerson;
import com.gibbsdevops.alfred.model.github.GHUser;
import com.google.common.base.Objects;

import javax.persistence.*;
import java.lang.reflect.Field;

@Entity(name = "alfred_user")
public class AlfredUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    private String login;
    private Long githubId;
    private String name;
    private String email;
    private String url;
    private String htmlUrl;
    private String avatarUrl;
    private String type;
    private String description;
    private Long createdAt;
    private Long updatedAt;

    private static AlfredUser from(GHUser u) {
        if (u == null) throw new NullPointerException("user is null");
        AlfredUser user = new AlfredUser();
        user.githubId = u.getId();
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getGithubId() {
        return githubId;
    }

    public void setGithubId(Long githubId) {
        this.githubId = githubId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
