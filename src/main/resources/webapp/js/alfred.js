$(document).ready(function() {
    setInterval(function() {
        $(".console-cursor").each(function() {
            $(this).toggleClass("hidden");
        });
    }, 500);
});

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
                var body = JSON.parse(event.body);
                Alfred.Job.find(body.id, body);
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
            Alfred.User.find(user.id, user);
        });
        $.each(data.repos, function(index, repo) {
            Alfred.Repo.find(repo.id, repo);
        });
        $.each(data.commits, function(index, commit) {
            Alfred.Commit.find(commit.id, commit);
        });
        $.each(data.jobs, function(index, job) {
            Alfred.Job.find(job.id, job);
        });
    });
}

function JobIdsCompare(a, b) {
  return parseInt(a) - parseInt(b);
}

function handleJobLine(l) {
    var job = Alfred.Job.find(l.jobId);
    if (job == null) throw "Unable to handle job line. Got null for id " + l.jobId;
    Alfred.Job.AddLine(job, l);
}

function handleGitHubEvent(event) {
    console.log('New GitHub event:');
    console.log(event);
}

function alfred_main(live) {
    Alfred.resetStores();
    if (live) {
        connect();
        load_history();
    }
}
