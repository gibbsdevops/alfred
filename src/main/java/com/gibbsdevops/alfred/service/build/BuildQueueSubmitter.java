package com.gibbsdevops.alfred.service.build;

import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;

public interface BuildQueueSubmitter {

    void submit(AlfredJobNode job);

}
