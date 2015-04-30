package com.gibbsdevops.alfred.service.build;

import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;

public interface BuildService {

    public void submit(AlfredJobNode job);

    public void starting(AlfredJobNode job);

    public void succeeded(AlfredJobNode job, int duration);

    void failed(AlfredJobNode job, int duration);

    void errored(AlfredJobNode job, String error);

    void logOutput(AlfredJobNode job, int index, String line);

}
