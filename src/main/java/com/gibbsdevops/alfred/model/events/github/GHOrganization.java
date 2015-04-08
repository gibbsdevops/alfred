package com.gibbsdevops.alfred.model.events.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GHOrganization extends GHUser {

    private String description;

}
