package com.gibbsdevops.alfred.repository;

import com.gibbsdevops.alfred.dao.AlfredCommitDao;
import com.gibbsdevops.alfred.dao.AlfredJobDao;
import com.gibbsdevops.alfred.dao.AlfredNodeDao;
import com.gibbsdevops.alfred.dao.AlfredRepoDao;
import com.gibbsdevops.alfred.model.alfred.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultAlfredNodeDao implements AlfredNodeDao {

    @Autowired
    private AlfredJobDao alfredJobDao;

    @Autowired
    private AlfredCommitDao alfredCommitDao;

    @Autowired
    private AlfredRepoDao alfredRepoDao;

    @Override
    public AlfredJobNode getJob(Long id) {
        AlfredJob job = alfredJobDao.findOne(id);
        AlfredJobNode node = AlfredJobNode.from(job);
        node.setCommit(getCommit(job.getCommitId()));
        return node;
    }

    @Override
    public AlfredCommitNode getCommit(Long id) {
        AlfredCommit commit = alfredCommitDao.findOne(id);
        AlfredCommitNode node = AlfredCommitNode.from(commit);

        AlfredRepo repo = alfredRepoDao.findOne(commit.getRepoId());
        AlfredRepoNode repoNode = AlfredRepoNode.from(repo);
        node.setRepo(repoNode);

        return node;
    }
}
