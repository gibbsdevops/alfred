package com.gibbsdevops.alfred.config;

import org.junit.Test;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

/**
 * Created by sgibbs on 4/7/2015.
 */
public class GithubTest {

    @Test
    public void testGithub() throws IOException {

        GitHub github = GitHub.connect();

        GHOrganization organization = github.getOrganization("gibbsdevops");
        // organization.

    }

}
