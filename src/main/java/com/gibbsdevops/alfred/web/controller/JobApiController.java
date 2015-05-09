package com.gibbsdevops.alfred.web.controller;

import com.gibbsdevops.alfred.model.events.local.NewJobRequest;
import com.gibbsdevops.alfred.repository.AlfredRepository;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.job.repositories.JobOutputRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/jobs")
public class JobApiController extends ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(JobApiController.class);

    @Autowired
    private AlfredRepository alfredRepository;

    @Autowired
    private JobOutputRepository jobOutputRepository;

    @Autowired
    private BuildService buildService;

    @RequestMapping(method = RequestMethod.GET)
    public Object getAll(@RequestParam(required = false) Integer limit) {
        return null; // jobService.getJobs();
    }

    @RequestMapping(value = "/{id}/output", method = RequestMethod.GET)
    public Object getOutput(@PathVariable int id) {
        return jobOutputRepository.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable int id) {
        return null; // jobService.getJob(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Object create(@RequestBody NewJobRequest request) throws IOException {

        // GitHub github = GitHub.connect();

        // org.kohsuke.github.GHOrganization organization = github.getOrganization(request.getOrganization());


        /* TODO
        AlfredJobNode job = new AlfredJobNode();

        GHOrganization organization = new GHOrganization();
        organization.setLogin(request.getOrganization());
        job.setOrganization(organization);

        GHHookRepository repository = new GHHookRepository();
        repository.setName(request.getRepo());
        repository.setHtmlUrl(request.getHtmlUrl());
        repository.setSshUrl(request.getSshUrl());
        repository.setGitUrl(request.getGitUrl());
        repository.setCloneUrl(request.getCloneUrl());
        job.setRepository(repository);

        job.setRef(request.getSpec());
        job.setStatus("queued");

        // save job
        jobService.save(job);

        // submit job for building
        buildService.submit(job);

        NewJobResponse response = new NewJobResponse();
        response.setRequest(request);
        response.setId(job.getId());
        return response;
        */

        return "TODO";
    }

}
