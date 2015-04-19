package com.gibbsdevops.alfred.service.github;

import com.gibbsdevops.alfred.model.github.GHOrganization;
import com.gibbsdevops.alfred.model.github.GHRepository;
import com.gibbsdevops.alfred.model.github.GHUser;

public interface GithubService {

    GHUser getUser(String login);

    GHOrganization getOrganization(String login);

    GHRepository getRepository(String owner, String name);

}
