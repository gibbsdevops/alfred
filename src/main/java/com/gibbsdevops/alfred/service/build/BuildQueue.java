package com.gibbsdevops.alfred.service.build;

import com.gibbsdevops.alfred.model.alfred.AlfredJobNode;

public interface BuildQueue {

    void submit(AlfredJobNode job);

}
