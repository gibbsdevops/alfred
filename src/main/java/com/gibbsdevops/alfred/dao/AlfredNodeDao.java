package com.gibbsdevops.alfred.dao;

import com.gibbsdevops.alfred.model.alfred.AlfredCommitNode;
import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;

public interface AlfredNodeDao {

    AlfredJobNode getJob(Long id);

    AlfredCommitNode getCommit(Long id);

}
