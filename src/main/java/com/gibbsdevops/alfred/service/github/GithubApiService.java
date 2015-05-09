package com.gibbsdevops.alfred.service.github;

import com.gibbsdevops.alfred.model.github.GHOrganization;
import com.gibbsdevops.alfred.model.github.GHPerson;
import com.gibbsdevops.alfred.model.github.GHRepository;

public interface GithubApiService {

    GHPerson getPerson(String url);

    GHOrganization getOrganization(String url);

    GHRepository getRepository(String url);

}
