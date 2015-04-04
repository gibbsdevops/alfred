package com.gibbsdevops.alfred.controller;

import com.gibbsdevops.alfred.model.events.github.Organization;
import com.gibbsdevops.alfred.model.events.github.PushEvent;
import com.gibbsdevops.alfred.model.events.github.Repository;
import com.gibbsdevops.alfred.model.events.local.NewJobRequest;
import com.gibbsdevops.alfred.model.events.local.NewJobResponse;
import com.gibbsdevops.alfred.model.job.Job;
import com.gibbsdevops.alfred.service.build.BuildService;
import com.gibbsdevops.alfred.service.job.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/job")
public class JobController extends ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private BuildService buildService;

    @RequestMapping(method = RequestMethod.POST)
    public Object newJob(@RequestBody NewJobRequest request) {

        Job job = new Job();

        Organization organization = new Organization();
        organization.setLogin(request.getOrganization());
        job.setOrganization(organization);

        Repository repository = new Repository();
        repository.setName(request.getRepo());
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
    }

}
