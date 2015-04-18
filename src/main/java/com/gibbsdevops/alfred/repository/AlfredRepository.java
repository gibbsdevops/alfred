package com.gibbsdevops.alfred.repository;

import com.gibbsdevops.alfred.model.alfred.*;

public interface AlfredRepository {

    AlfredUser save(AlfredUser user);

    AlfredRepoNode save(AlfredRepoNode repo);

    AlfredRepo save(AlfredRepo repo);

    AlfredGitUser save(AlfredGitUser user);

    AlfredCommitNode save(AlfredCommitNode commit);

    AlfredUser getUserByName(String name);

}
