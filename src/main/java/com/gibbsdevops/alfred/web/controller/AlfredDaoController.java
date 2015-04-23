package com.gibbsdevops.alfred.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gibbsdevops.alfred.dao.*;
import com.gibbsdevops.alfred.model.alfred.*;
import com.gibbsdevops.alfred.web.ResourceNotFoundRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
public class AlfredDaoController extends ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(AlfredDaoController.class);

    @Autowired
    private AlfredJobDao alfredJobDao;

    @Autowired
    private AlfredCommitDao alfredCommitDao;

    @Autowired
    private AlfredRepoDao alfredRepoDao;

    @Autowired
    private AlfredGitUserDao alfredGitUserDao;

    @Autowired
    private AlfredUserDao alfredUserDao;

    @RequestMapping("/not-found")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundRuntimeException.class)
    public Map exceptionHandler(HttpServletRequest req, Exception ex) {
        LOG.warn("Resource not found", ex);
        return Collections.singletonMap("error", "not found");
    }

    @RequestMapping("/job/{id}")
    public AlfredJob getJob(@PathVariable Long id) {
        AlfredJob job = alfredJobDao.findOne(id);
        if (job == null) throw new ResourceNotFoundRuntimeException();
        return job;
    }

    @RequestMapping("/commit/{id}")
    public AlfredCommit getCommit(@PathVariable Long id) {
        AlfredCommit commit = alfredCommitDao.findOne(id);
        if (commit == null) throw new ResourceNotFoundRuntimeException();
        return commit;
    }

    @RequestMapping("/repo/{id}")
    public AlfredRepo getRepo(@PathVariable Long id) {
        AlfredRepo repo = alfredRepoDao.findOne(id);
        if (repo == null) throw new ResourceNotFoundRuntimeException();
        return repo;
    }

    @RequestMapping("/person/{id}")
    public AlfredGitUser getPerson(@PathVariable Long id) {
        AlfredGitUser gitUser = alfredGitUserDao.findOne(id);
        if (gitUser == null) throw new ResourceNotFoundRuntimeException();
        return gitUser;
    }

    @RequestMapping("/user/{id}")
    public AlfredUser getUser(@PathVariable Long id) {
        AlfredUser user = alfredUserDao.findOne(id);
        if (user == null) throw new ResourceNotFoundRuntimeException();
        return user;
    }

}
