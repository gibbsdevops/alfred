window.Alfred = Ember.Application.create({
  LOG_TRANSITIONS: true
});

Alfred.SocketClass = Ember.Object.extend({
    time_now: new Date().getTime(),
    last_received: new Date().getTime(),
    last_msg_age: function() {
        return Math.max(0, this.get('time_now') - this.get('last_received'));
    }.property('time_now', 'last_received'),
    isStale: function() {
        return this.get('last_msg_age') > 30000;
    }.property('last_msg_age'),
    msg_count: 0,
    receive_message: function(msg) {
        // console.log(msg);
        this.set('msg_count', this.get('msg_count') + 1)
        this.set('last_received', new Date().getTime());
    }
});
Alfred.Socket = Alfred.SocketClass.create();

function update_time() {
    Alfred.Socket.set('time_now', new Date().getTime())
}
window.setInterval(update_time, 1000);

Alfred.Job = Ember.Object.extend({
    id: null,
    ref: null,
    branch: function() {
        return this.get('ref').replace("refs/heads/", "");
    }.property('ref'),
    output: null,
    last_line: function() {
        output = this.get('output');
        l = output.get('lastObject');
        console.log("Last line is " + l)
        return l;
    }.property('output', 'output.@each'),
    addLine: function(line) {
        // console.log("Adding line to " + this.get('id'));
        output = this.get('output');
        output.pushObject(Alfred.OutputLine.create(line));
        this.set('output', output);

        $(".mini-console").scrollTop(10000);
    }
});

Alfred.Job.build = function(j) {
   job = Alfred.Job.create(j);
   job.set('commit', Alfred.Commit.create(job.commit));
   job.set('output', []);
   return job;
};

Alfred.Commit = Ember.Object.extend({
    id: null,
    shortId: function() {
        return this.get('id').substring(0,7);
    }.property('id')
});

Alfred.OutputLine = Ember.Object.extend({
    id: null,
    pos: null,
    line: null,
    number: function() {
        return this.get('pos') + 1;
    }.property('pos')
});

Alfred.Jobs = Ember.A([]);
Alfred.JobsById = {};

Alfred.SortedJobs = Ember.ArrayController.create({
  model: Alfred.Jobs,
  sortProperties: ['id'],
  sortAscending: false
});

Alfred.RunningJobsClass = Ember.ArrayController.extend({
  jobs: Alfred.Jobs,
  model: function() {
    return this.get('jobs').filterBy('status', 'in-progress');
  }.property('jobs.@each.status'),
  sortProperties: ['id'],
  sortAscending: false
});
Alfred.RunningJobs = Alfred.RunningJobsClass.create();

Alfred.IndexRoute = Ember.Route.extend({
    model: function(params) {
        console.log("IndexRoute.model params=" + JSON.stringify(params));
        return Alfred.SortedJobs;
    },
    setupController : function(controller, model) {
        console.log("IndexRoute.setupController model=" + JSON.stringify(model));
        controller.set('jobs', model);
        controller.set('running_jobs', Alfred.RunningJobs);
        controller.set('socket', Alfred.Socket);
    }
})

Alfred.FadeInView = Ember.View.extend({
    didInsertElement: function(){
        // this.$().hide().show(100);
    }
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
        console.log(settings);
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
    $.getJSON("api/history/jobs", function(data) {
        $.each(data, function(index, j) {
            handleJob(j);
        });
    });
}

function sendName() {
    var name = document.getElementById('name').value;
    stompClient.send("/app/hello", {}, JSON.stringify({ 'name': name }));
}

function generateJobElement(job) {
    var pre = document.createElement('pre');
    pre.appendChild(document.createTextNode(
            "#" + job.id + " " + job.organization.login + "/" + job.repository.name + "\n"
            + job.commit.id.substring(0,7) + " (" + job.ref.replace("refs/heads/", "") + ")" + "\n"
            + job.commit.message + "\n"
        )
    );
    return pre;
}

function handleJob(j) {
    job = Alfred.Job.build(j);

    existing = Alfred.JobsById[job.get('id')];
    if (existing != null) {
        if (existing.get('status') == 'in-progress' && job.get('status') == 'complete') {
            setTimeout(function() {
                existing.set('status', 'complete');
            }, 3000);
        } else {
            existing.set('status', job.get('status'));
            existing.set('error', job.get('error'));
        }

    } else {

        Alfred.JobsById[job.get('id')] = job;
        Alfred.Jobs.pushObject(job);
    }
}

function handleJobLine(l) {
    // console.log("Received job line: " + JSON.stringify(l));
    Alfred.JobsById[l.id].addLine(l);
}

function handleGitHubEvent(event) {

}

function alfred_main(live) {
    if (live) {
        connect();
        load_history();
    }
}

alfred_main(true);
