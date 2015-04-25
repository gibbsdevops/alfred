var stompClient = null;

function execConnect() {

}

function connect() {

    function openSocket(url) {
        var ws = new SockJS(url);

        stompClient = Stomp.over(ws);
        stompClient.heartbeat.outgoing = 2000;
        stompClient.heartbeat.incoming = 2000;
        stompClient.debug = null

        stompClient.connect({}, function(frame) {
            console.log('Connected');
            stompClient.subscribe('/topic/github', function(event){
                Alfred.Socket.receive_message(event);
                handleGitHubEvent(JSON.parse(event.body));
            });
            stompClient.subscribe('/topic/jobs', function(event){
                Alfred.Socket.receive_message(event);
                handleJob(JSON.parse(event.body));
            });
            stompClient.subscribe('/topic/job-line', function(event){
                Alfred.Socket.receive_message(event);
                handleJobLine(JSON.parse(event.body));
            });
        }, function(msg) {
            console.log("Socket closed, scheduling reconnection...");
            setTimeout(connect, 3000);
        });
    }

    console.log("Connecting")

    $.getJSON("api/settings/system", function(settings) {
        console.log('Settings: ' + JSON.stringify(settings));
        openSocket(settings["websocket-uri"])
    });

    var url = '/api/broker.socket'

    // stompClient = Stomp.client(url, 'v11.stomp');
}

function disconnect() {
    stompClient.disconnect();
    setConnected(false);
    console.log("Disconnected");
}

function load_history() {
    $.getJSON("api/latest", function(data) {
        console.log('Loading history');
        $.each(data.users, function(index, user) {
            handleUser(user);
        });
        $.each(data.repos, function(index, repo) {
            handleRepo(repo);
        });
        $.each(data.commits, function(index, commit) {
            handleCommit(commit);
        });
        $.each(data.jobs, function(index, j) {
            handleJob(j);
        });
    });
}

function JobIdsCompare(a, b) {
  return parseInt(a) - parseInt(b);
}

function handleUser(user) {
    existing = Alfred.UsersById[user.id];
    if (existing != null) return;
    Alfred.UsersById[user.id] = Ember.Object.create(user);
}

function handleRepo(repo) {
    existing = Alfred.ReposById[repo.id];
    if (existing != null) return;
    Alfred.Repo.build(repo);
}

function handleCommit(c) {
    existing = Alfred.CommitsById[c.id];
    if (existing != null) return;

    Alfred.Commit.build(c);
}

function handleJob(j) {
    job = Alfred.Job.build(j);

    existing = Alfred.JobsById[job.get('id')];
    if (existing != null) {
        throw "todo"
        // Alfred.Job.merge(existing, job);
    } else {
        Alfred.JobsById[job.get('id')] = job;
        Alfred.Jobs.pushObject(job);
    }

    /*
    if (Alfred.Jobs.length > 100) {
        var ids = Object.keys(Alfred.JobsById);
        ids.sort(JobIdsCompare);
        id = ids[0];

        var obj = Alfred.Jobs.findProperty('id', parseInt(id));
        Alfred.Jobs.removeObject(obj);
        delete Alfred.JobsById[id];
    }
    */
}

function handleJobLine(l) {
    Alfred.Job.AddLine(Alfred.JobsById[l.id], { 'index': l.index, 'line': l.line });
}

function handleGitHubEvent(event) {
    console.log(event);
}

function alfred_main(live) {
    if (live) {
        connect();
        load_history();
    }
}

alfred_main(true);
