package com.gibbsdevops.alfred.dao;

import com.gibbsdevops.alfred.model.alfred.JobLine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlfredJobLineDao extends CrudRepository<JobLine, Long> {

    List<JobLine> findByJobId(Long id);

}
