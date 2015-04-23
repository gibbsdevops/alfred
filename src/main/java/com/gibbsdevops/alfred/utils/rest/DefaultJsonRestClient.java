package com.gibbsdevops.alfred.utils.rest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DefaultJsonRestClient implements JsonRestClient {

    @Override
    public RestResponse exec(RestRequest request) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(request.getUrl());

        CloseableHttpResponse response = null;

        try {
            response = client.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.OK_200) {
                throw new RuntimeException(String.format(
                        "Failed to get: '%s'. Expected response code 200, but was %s.", request.getUrl(), statusCode));
            }

            HttpEntity entity = response.getEntity();

            String body = IOUtils.toString(entity.getContent());
            System.out.println(body);

            return new RestResponse(body);

        } catch (IOException e) {
            throw new RuntimeException("Http get failed", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    throw new RuntimeException("Unable to close http response", e);
                }
            }
        }

    }

}
