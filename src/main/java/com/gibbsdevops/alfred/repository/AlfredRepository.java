package com.gibbsdevops.alfred.repository;

import com.gibbsdevops.alfred.model.alfred.AlfredCommitNode;
import com.gibbsdevops.alfred.model.alfred.AlfredGitUser;
import com.gibbsdevops.alfred.model.alfred.AlfredRepoNode;
import com.gibbsdevops.alfred.model.alfred.AlfredUser;

public interface AlfredRepository {

    AlfredUser save(AlfredUser user);

    AlfredRepoNode save(AlfredRepoNode repo);

    AlfredGitUser save(AlfredGitUser user);

    AlfredCommitNode save(AlfredCommitNode commit);

    AlfredUser getUserByName(String name);

}
