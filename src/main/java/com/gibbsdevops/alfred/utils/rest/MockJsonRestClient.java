package com.gibbsdevops.alfred.utils.rest;

import com.google.common.collect.Maps;

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

    public void reset() {
        map.clear();
    }

}
