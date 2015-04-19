package com.gibbsdevops.alfred.utils.rest;

import com.google.common.base.Objects;

public class RestRequest {

    private String url;
    private RestRequestType type;

    public static RestRequest get(String url) {
        return new RestRequest(url, RestRequestType.GET);
    }

    public RestRequest() {
    }

    public RestRequest(String url) {
        this.url = url;
    }

    public RestRequest(String url, RestRequestType type) {
        this.url = url;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RestRequestType getType() {
        return type;
    }

    public void setType(RestRequestType type) {
        this.type = type;
    }

    public static enum RestRequestType {
        GET
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestRequest)) return false;

        RestRequest that = (RestRequest) o;

        if (!Objects.equal(url, that.url)) return false;
        if (!Objects.equal(type, that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url, type);
    }

}
