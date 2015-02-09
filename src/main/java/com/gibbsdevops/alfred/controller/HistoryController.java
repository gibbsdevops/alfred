package com.gibbsdevops.alfred.controller;

import com.gibbsdevops.alfred.service.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/history/jobs")
public class HistoryController extends ApiController {

    @Autowired
    private JobRepository jobRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Object history() {
        return jobRepository.getJobs();
    }

}
