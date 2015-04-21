package com.gibbsdevops.alfred.utils.rest;

import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Map;

public class MockJsonRestClient implements JsonRestClient {

    private Map<RestRequest, RestResponse> map = Maps.newHashMap();

    @Override
    public RestResponse exec(RestRequest request) {
        RestResponse response = map.get(request);
        if (response == null) {
            throw new UnexpectedJsonRequestRuntimeException(request);
        }
        return response;
    }

    public void addRequest(RestRequest request, RestResponse response) {
        map.put(request, response);
    }

    public void addRequest(RestRequest request, String response) {
        map.put(request, new RestResponse(response));
    }

    public void addRequestFromJar(RestRequest request, Class c, String resource) {
        try {
            map.put(request, new RestResponse(
                    IOUtils.toString(c.getResourceAsStream(resource))
            ));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource: " + resource, e);
        }
    }

    public void reset() {
        map.clear();
    }

    public static class Builder {

        private MockJsonRestClient mockJsonRestClient;
        private Class clazz;

        public Builder(Class clazz) {
            this.mockJsonRestClient = new MockJsonRestClient();
            this.clazz = clazz;
        }

        public void get(String url, String resource) {
            mockJsonRestClient.addRequestFromJar(RestRequest.get(url), clazz, resource);
        }

        public MockJsonRestClient getMockJsonRestClient() {
            return mockJsonRestClient;
        }

    }
}
