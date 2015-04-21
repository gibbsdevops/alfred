package com.gibbsdevops.alfred.dao;

import com.gibbsdevops.alfred.model.alfred.AlfredJob;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlfredJobDao extends CrudRepository<AlfredJob, Long> {

}
