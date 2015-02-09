package com.gibbsdevops.alfred.service;

import com.gibbsdevops.alfred.model.events.github.PushEvent;

public interface IngestService {

    void handle(PushEvent push);

}
