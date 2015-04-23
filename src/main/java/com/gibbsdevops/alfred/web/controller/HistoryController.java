package com.gibbsdevops.alfred.web.controller;

import com.gibbsdevops.alfred.service.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/history/jobs")
public class HistoryController extends ApiController {

    // @Autowired
    // private JobService jobService;

    @RequestMapping(method = RequestMethod.GET)
    public Object history() {
        return null; // jobService.getJobs();
    }

}
