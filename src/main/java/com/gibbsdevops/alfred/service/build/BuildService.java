package com.gibbsdevops.alfred.service.build;

import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;

public interface BuildService {

    void queued(AlfredJobNode job);

    void starting(AlfredJobNode job);

    void succeeded(AlfredJobNode job, int duration);

    void failed(AlfredJobNode job, int duration);

    void errored(AlfredJobNode job, String error);

    void logOutput(AlfredJobNode job, int index, String line);

}
