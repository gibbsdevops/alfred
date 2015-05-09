package com.gibbsdevops.alfred.web.controller;

import com.gibbsdevops.alfred.dao.*;
import com.gibbsdevops.alfred.model.alfred.*;
import com.gibbsdevops.alfred.web.ResourceNotFoundRuntimeException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class AlfredDaoController extends ApiController {

    private static final Logger LOG = LoggerFactory.getLogger(AlfredDaoController.class);

    @Autowired
    private AlfredJobDao alfredJobDao;

    @Autowired
    private AlfredJobLineDao alfredJobLineDao;

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
    public Map exceptionHandler(HttpServletRequest req, ResourceNotFoundRuntimeException ex) {
        LOG.warn("Requested resource not found: {}", ex.getMessage());
        return Collections.singletonMap("error", "not found");
    }

    @RequestMapping("/job/{id}")
    public AlfredJob getJob(@PathVariable Long id) {
        AlfredJob job = alfredJobDao.findOne(id);
        if (job == null) throw new ResourceNotFoundRuntimeException(String.format("Job with id %d", id));
        return job;
    }

    @RequestMapping("/job/{id}/output")
    public Object getJobOutput(@PathVariable Long id) {
        return alfredJobLineDao.findByJobId(id);
    }

    @RequestMapping("/commit/{id}")
    public AlfredCommit getCommit(@PathVariable Long id) {
        AlfredCommit commit = alfredCommitDao.findOne(id);
        if (commit == null) throw new ResourceNotFoundRuntimeException(String.format("Commit with id %d", id));
        return commit;
    }

    @RequestMapping("/repo/{id}")
    public AlfredRepo getRepo(@PathVariable Long id) {
        AlfredRepo repo = alfredRepoDao.findOne(id);
        if (repo == null) throw new ResourceNotFoundRuntimeException(String.format("Repo with id %d", id));
        return repo;
    }

    @RequestMapping("/person/{id}")
    public AlfredGitUser getPerson(@PathVariable Long id) {
        AlfredGitUser gitUser = alfredGitUserDao.findOne(id);
        if (gitUser == null) throw new ResourceNotFoundRuntimeException(String.format("Person with id %d", id));
        return gitUser;
    }

    @RequestMapping("/user/{id}")
    public AlfredUser getUser(@PathVariable Long id) {
        AlfredUser user = alfredUserDao.findOne(id);
        if (user == null) throw new ResourceNotFoundRuntimeException(String.format("User with id %d", id));
        return user;
    }

    @RequestMapping("/latest")
    public Object getLatest() {

        Slice<AlfredJob> jobsSlice = alfredJobDao.findAll(new PageRequest(0, 10, new Sort(Sort.Direction.DESC, "id")));

        List<AlfredJob> jobs = Lists.newArrayList();
        Map<Long, AlfredCommit> commits = Maps.newHashMap();
        Map<Long, AlfredRepo> repos = Maps.newHashMap();
        Map<Long, AlfredUser> users = Maps.newHashMap();
        Map<Long, AlfredGitUser> gitUsers = Maps.newHashMap();

        jobsSlice.forEach(j -> {
            jobs.add(j);

            AlfredCommit commit = commits.get(j.getCommitId());
            if (commit == null) {
                commit = alfredCommitDao.findOne(j.getCommitId());
                commits.put(commit.getId(), commit);
            }

            AlfredRepo repo = repos.get(commit.getRepoId());
            if (repo == null) {
                repo = alfredRepoDao.findOne(commit.getRepoId());
                repos.put(repo.getId(), repo);
            }

            Long[] userIds = new Long[]{repo.getOwnerId(), commit.getPusherId()};
            Long[] gitUserIds = new Long[]{commit.getPusherId(), commit.getCommitterId(), commit.getAuthorId()};

            for (Long id : userIds) {
                AlfredUser u = users.get(id);
                if (u == null) {
                    u = alfredUserDao.findOne(id);
                }
                users.put(id, u);
            }

            for (Long id : gitUserIds) {
                AlfredGitUser u = gitUsers.get(id);
                if (u == null) {
                    u = alfredGitUserDao.findOne(id);
                }
                gitUsers.put(id, u);
            }

        });

        Map<String, Object> latest = Maps.newHashMap();
        latest.put("jobs", jobs);
        latest.put("commits", commits);
        latest.put("repos", repos);
        latest.put("users", users);
        latest.put("persons", gitUsers);

        return latest;
    }

}
