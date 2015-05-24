package com.gibbsdevops.alfred.utils.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.model.alfred.utils.AlfredObjectMapperFactory;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class DefaultJsonRestClient implements JsonRestClient {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultJsonRestClient.class);

    private final ObjectMapper objectMapper = AlfredObjectMapperFactory.get();

    @Override
    public RestResponse exec(RestRequest request) {
        HttpClientContext context = HttpClientContext.create();
        CloseableHttpClient client = HttpClients.createDefault();

        URI uri = null;
        try {
            uri = new URI(request.getUrl());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to parse URI: " + request.getUrl(), e);
        }

        HttpUriRequest httpRequest = null;
        switch (request.getType()) {
            case GET:
                LOG.info("GET request to {}", request.getUrl());
                httpRequest = new HttpGet(uri);
                break;
            case POST:
                HttpPost post = new HttpPost(uri);
                String body = null;
                try {
                    body = AlfredObjectMapperFactory.get().writeValueAsString(request.getBody());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Failed to process json", e);
                }

                LOG.debug("POST request to {}:\n{}", request.getUrl(), body);

                post.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
                httpRequest = post;
                break;
            default:
                throw new IllegalArgumentException();
        }

        for (String key : request.getHeaders().keySet()) {
            httpRequest.setHeader(key, request.getHeaders().get(key));
        }

        CloseableHttpResponse response = null;

        try {
            response = client.execute(httpRequest, context);

            int statusCode = response.getStatusLine().getStatusCode();

            HttpEntity entity = response.getEntity();
            String body = IOUtils.toString(entity.getContent());

            String prettyBody = null;
            try {
                prettyBody = objectMapper.writeValueAsString(objectMapper.readTree(body));
            } catch (Exception e) {
                LOG.warn("Response body is not parsable", e);
            }

            if (request.getExpected() != null && statusCode != request.getExpected().intValue()) {

                throw new RuntimeException(String.format(
                        "Http failed with %s - %s. Expected 200: %s %s\n%s",
                        statusCode, response.getStatusLine().getReasonPhrase(),
                        request.getType(), request.getUrl(), prettyBody));
            }

            LOG.debug("Response {}:\n{}", response.getStatusLine(), prettyBody);

            return new RestResponse(body);

        } catch (IOException e) {
            throw new RuntimeException("Http request failed", e);
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
