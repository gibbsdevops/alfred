package com.gibbsdevops.alfred.dao;

import com.gibbsdevops.alfred.model.alfred.AlfredGitUser;
import org.springframework.data.repository.CrudRepository;

public interface AlfredGitUserDao extends CrudRepository<AlfredGitUser, Long> {

    AlfredGitUser getByNameAndEmail(String name, String email);

}
