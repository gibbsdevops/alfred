package com.gibbsdevops.alfred.dao;

import com.gibbsdevops.alfred.model.alfred.AlfredCommit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlfredCommitDao extends CrudRepository<AlfredCommit, Long> {

    AlfredCommit findByRepoAndHash(Long repo, String hash);

}
