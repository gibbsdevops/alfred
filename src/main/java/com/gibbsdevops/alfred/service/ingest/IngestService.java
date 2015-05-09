package com.gibbsdevops.alfred.service.ingest;

import com.gibbsdevops.alfred.model.github.events.GHPushEvent;

public interface IngestService {

    void handle(GHPushEvent push);

}
