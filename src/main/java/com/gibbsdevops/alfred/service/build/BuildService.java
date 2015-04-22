package com.gibbsdevops.alfred.service.build;

import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;

public interface BuildService {

    public void submit(AlfredJobNode job);

    public void starting(AlfredJobNode job);

    public void finished(AlfredJobNode job);

    void failed(AlfredJobNode job, String reason);

    void logOutput(AlfredJobNode job, String line);

}
