package com.gibbsdevops.alfred.repository;

import com.gibbsdevops.alfred.model.alfred.AlfredUser;

public interface AlfredRepository {

    AlfredUser save(AlfredUser user);

    AlfredUser getOrgByName(String name);

}
