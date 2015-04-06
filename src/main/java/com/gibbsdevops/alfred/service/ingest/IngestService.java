package com.gibbsdevops.alfred.service.ingest;

import com.gibbsdevops.alfred.web.model.events.github.PushEvent;

public interface IngestService {

    void handle(PushEvent push);

}
