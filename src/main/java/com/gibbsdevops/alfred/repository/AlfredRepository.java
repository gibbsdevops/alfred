package com.gibbsdevops.alfred.repository;

import com.gibbsdevops.alfred.model.alfred.*;

public interface AlfredRepository {

    AlfredUser save(AlfredUser user);

    AlfredRepo save(AlfredRepo repo);

    AlfredGitUser save(AlfredGitUser user);

    AlfredCommit save(AlfredCommit commit);

    AlfredJob save(AlfredJob job);

    AlfredUser getUserByName(String name);

    AlfredCommitNode getCommitNode(Long id);

}
