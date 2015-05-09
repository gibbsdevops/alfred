package com.gibbsdevops.alfred.utils.rest;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Map;

public class RestRequest {

    private String url;
    private Map<String, String> headers = Maps.newHashMap();
    private RestRequestType type;
    private Object body;
    private Integer expected = HttpStatus.OK_200;

    public static RestRequest get(String url) {
        return new RestRequest(url, RestRequestType.GET);
    }

    public static RestRequest post(String url, Object body) {
        RestRequest request = new RestRequest(url, RestRequestType.POST, body);
        request.setExpected(HttpStatus.CREATED_201);
        return request;
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

    public RestRequest(String url, RestRequestType type, Object body) {
        this.url = url;
        this.type = type;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public RestRequestType getType() {
        return type;
    }

    public void setType(RestRequestType type) {
        this.type = type;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Integer getExpected() {
        return expected;
    }

    public void setExpected(Integer expected) {
        this.expected = expected;
    }

    public void basicAuthorization(String username, String password) {
        String credString = username + ":" + password;
        String encodedCreds = new String(Base64.encodeBase64(credString.getBytes()));
        headers.put("Authorization", "Basic " + encodedCreds);
    }

    public static enum RestRequestType {
        GET,
        POST
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestRequest)) return false;

        RestRequest that = (RestRequest) o;

        if (!Objects.equal(url, that.url)) return false;
        if (!Objects.equal(type, that.type)) return false;
        if (!Objects.equal(body, that.body)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url, type, body);
    }

}
