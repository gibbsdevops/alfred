package com.gibbsdevops.alfred.service.github;

import com.gibbsdevops.alfred.model.github.GHOrganization;
import com.gibbsdevops.alfred.model.github.GHPerson;
import com.gibbsdevops.alfred.model.github.GHRepository;
import com.gibbsdevops.alfred.utils.rest.JsonRestClient;
import com.gibbsdevops.alfred.utils.rest.RestRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultGithubApiService implements GithubApiService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultGithubApiService.class);

    private JsonRestClient jsonRestClient;

    private RestRequest withAuth(RestRequest request) {
        return request.basicAuth(System.getenv("GITHUB_LOGIN"), System.getenv("GITHUB_PASSWORD"));
    }

    @Override
    public GHPerson getPerson(String url) {
        return jsonRestClient.exec(withAuth(RestRequest.get(url))).as(GHPerson.class);
    }

    @Override
    public GHOrganization getOrganization(String url) {
        return jsonRestClient.exec(withAuth(RestRequest.get(url))).as(GHOrganization.class);
    }

    @Override
    public GHRepository getRepository(String url) {
        return jsonRestClient.exec(withAuth(RestRequest.get(url))).as(GHRepository.class);
    }

    @Autowired
    public void setJsonRestClient(JsonRestClient jsonRestClient) {
        this.jsonRestClient = jsonRestClient;
    }

}
