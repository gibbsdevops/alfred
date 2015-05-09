package com.gibbsdevops.alfred.dao;

import com.gibbsdevops.alfred.model.alfred.AlfredRepo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlfredRepoDao extends CrudRepository<AlfredRepo, Long> {

    AlfredRepo findByOwnerIdAndName(Long owner, String name);

}
