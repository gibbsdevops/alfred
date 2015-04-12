package com.gibbsdevops.alfred.repository;

import com.gibbsdevops.alfred.model.alfred.AlfredGitUser;
import com.gibbsdevops.alfred.model.alfred.AlfredRepoNode;
import com.gibbsdevops.alfred.model.alfred.AlfredUser;

public interface AlfredRepository {

    AlfredUser save(AlfredUser user);

    AlfredRepoNode save(AlfredRepoNode repo);

    AlfredGitUser save(AlfredGitUser user);

    AlfredUser getOrgByName(String name);

    AlfredUser getUserByName(String name);

}
