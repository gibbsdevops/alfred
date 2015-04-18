package com.gibbsdevops.alfred.service.job.repositories.file;

import com.gibbsdevops.alfred.model.job.Job;
import com.gibbsdevops.alfred.service.job.repositories.JobRepository;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class FileJobRepositoryTest {

    JobRepository jobRepository;

    File path = new File("target/jobs-test-db");

    @Before
    public void before() throws IOException {
        FileUtils.deleteDirectory(path);
        jobRepository = new FileJobRepository(path);
    }

    @Test
    public void saveNewJobTest() {
        Job j = new Job();
        j.setRef("abc");
        jobRepository.save(j);

        assertThat(j, is(notNullValue()));
        assertThat(j.getId(), is(equalTo(0)));
    }

    @Test
    public void saveExistingJobTest() {
        Job j = new Job();
        j.setRef("abc");
        jobRepository.save(j);

        assertThat(j, is(notNullValue()));
        assertThat(j.getId(), is(equalTo(0)));

        jobRepository.save(j);

        assertThat(j, is(notNullValue()));
        assertThat(j.getId(), is(equalTo(0)));
    }

    @Test
    public void getJobTest() {
        Job j = new Job();
        j.setRef("abc");
        jobRepository.save(j);

        assertThat(j, is(notNullValue()));
        assertThat(j.getId(), is(equalTo(0)));

        Set<Job> jobs = jobRepository.getJobs();
        assertThat(jobs, hasSize(1));

        j = jobs.iterator().next();

        assertThat(j, is(notNullValue()));
        assertThat(j.getId(), is(equalTo(0)));
        assertThat(j.getRef(), is(equalTo("abc")));

        j = new Job();
        j.setRef("123");
        jobRepository.save(j);

        jobs = jobRepository.getJobs();
        assertThat(jobs, hasSize(2));
    }

}
