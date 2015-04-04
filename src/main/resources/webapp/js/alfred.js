console.log('Alfred version 0');

$.ajaxSetup({
  contentType: "application/json; charset=utf-8"
});

window.Alfred = Ember.Application.create({
  LOG_TRANSITIONS: false,
  LOG_ACTIVE_GENERATION: true,
  LOG_VIEW_LOOKUPS: false,
  LOG_RESOLVER: false
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

    // parse org into tree
    var plainOrg = null;
    if (j['organization'] && j['organization']['login']) {
        plainOrg = j['organization'];
    } else {
        plainOrg = { 'id': j['repository']['owner']['email'], 'login': j['repository']['owner']['name'] }
    }

    var org = Alfred.Org.find(plainOrg.login, plainOrg);
    if (org == null) throw "org is null"
    if (org.login == null) throw "org.login is null"
    job.set('organization', org);

    // parse repo into tree
    var repo = Alfred.Repo.findByOrgAndName(org, j['repository']['name'], j['repository']);
    job.set('repository', repo);

    return job;
};

Alfred.Commit = Ember.Object.extend({
    id: null,
    shortId: function() {
        if (this.get('id') == null) {
            return "";
        }
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

Alfred.FadeInView = Ember.View.extend({
    didInsertElement: function(){
        this.$().hide().fadeIn("fast");
    }
});

Alfred.IndexRoute = Ember.Route.extend({
    model: function(params) {
        // console.log("IndexRoute.model params=" + JSON.stringify(params));
        return Alfred.SortedJobs;
    },
    setupController : function(controller, model) {
        // console.log("IndexRoute.setupController model=" + JSON.stringify(model));
        controller.set('model', model);
        controller.set('running_jobs', Alfred.RunningJobs);
        controller.set('socket', Alfred.Socket);
    }
});

Alfred.Org = Ember.Object.extend({
    id: null,
    login: null,
    all_jobs: Alfred.SortedJobs,
    jobs: function() {
        return this.get('all_jobs').filterBy('organization.login', this.get('login'));
    }.property('all_jobs.@each.organization.login', 'login')
});

Alfred.Orgs = Ember.A([]);
Alfred.OrgsByLogin = {};

Alfred.Repo = Ember.Object.extend({
    name: null,
    organization: null,
    jobs: function() {
        var org = this.get('organization');
        if (org == null) throw "Repo is missing organization";

        var jobs = org.get('jobs');
        // if (jobs.get('length') < 1) throw "No jobs in org";

        var name = this.get('name');
        if (name == null) throw "Repository has no name";

        var filtered = jobs.filterBy('repository.name', name);
        // if (filtered.get('length') < 1) throw "No jobs in org after filter";

        return filtered;
    }.property('organization', 'organization.jobs.@each.repository.name', 'name'),
    branches: function() {
        var jobs = this.get('jobs');

        var latest_jobs_by_branch = {};

        // jobs is ordered
        $.each(jobs, function(index, job) {
            var branch = job.get('branch');
            var current = latest_jobs_by_branch[branch];
            if (current == null) {
                latest_jobs_by_branch[branch] = job;
            }
        });

        var latest_jobs = Ember.A([]);
        $.each(latest_jobs_by_branch, function(branch, job) {
            latest_jobs.pushObject(job);
        });

        return latest_jobs;
    }.property('jobs', 'jobs.@each.branch'),
});

Alfred.Org.find = function(id, data) {
    // console.log("Alfred.Org.find " + id + ', ' + (data != null));
    var org = Alfred.OrgsByLogin[id];
    if (org == null) {
        console.log('New org: ' + id);
        if (data != null) {
            org = Alfred.Org.create(data);
        } else {
            org = Alfred.Org.create({ 'login': id });
        }
        Alfred.OrgsByLogin[id] = org;
    }
    return org;
};

Alfred.Repo.findByOrgAndName = function(org, name, data) {
    console.log("Alfred.Repo.findByOrgAndName " + org.login + ', ' + name + ', ' + (data != null));
    if (data != null && data.name == null) throw "data supplied but name is null";
    if (data != null && name != data.name) throw "data supplied but name does not match";

    var repoPath = org.login + '/' + name;

    var repo = Alfred.ReposByPath[repoPath];
    if (repo == null) {
        if (data != null) {
            repo = Alfred.Repo.create(data);
        } else {
            repo = Alfred.Repo.create({ 'name': name });
        }
        repo.set('organization', org);
        Alfred.ReposByPath[repoPath] = repo;
    } else if (data != null) {
        repo.set('name', data.name);
    }

    return repo;
}

Alfred.Repos = Ember.A([]);
Alfred.ReposByPath = {};

Alfred.OrgRoute = Ember.Route.extend({
    setupController : function(controller, model) {
        controller.set('model', model);
        controller.set('socket', Alfred.Socket);
    },
    serialize: function(/* org */ model) {
        if (model == null) return null;
        if (typeof model.get == 'function') {
            return { org_id: model.get('login') };
        }
        return { org_id: model.login };
    },
    renderTemplate: function() {
        this.render('org');
    }
});

Alfred.RepoController = Ember.Controller.extend({
    init: function() {
        console.log('Init RepoController');
    }
});

Alfred.RepoIndexController = Ember.Controller.extend({
    init: function() {
        console.log('Init RepoIndexController');
    },
    actions: {
        build: function() {
            var repo = this.get('model');
            console.log('build ' + this.get('gitSpec'));

            var jobRequest = {
                'repo': repo.get('name'),
                'organization': repo.get('organization').get('login'),
                'spec': this.get('gitSpec')
            };

            $.post("api/jobs", JSON.stringify(jobRequest), function(response) {
                console.log('Response: ' + JSON.stringify(response));
            }, 'json');

        }
    },
    gitSpec: 'master'
});

Alfred.RepoRoute = Ember.Route.extend({
    init: function() {
        console.log('Init RepoRoute');
    },
    model: function(id, trans) {
        console.log('Loading model for RepoRoute');

        var org = trans.resolvedModels.org;
        if (org == null) throw "no org found for repo in route"

        console.log('Loading Repo for route - ' + org.login + '/' + id.repo_id)

        var repo = Alfred.Repo.findByOrgAndName(org, id.repo_id);

        return repo;
    },
    serialize: function(/* repo */ model) {
        if (model == null) return null;
        if (typeof model.get == 'function') {
            return { org_id: 'devops', repo_id: model.get('name') };
        }
        return { org_id: 'devops', repo_id: model.name };
    }
});

Alfred.RepoIndexRoute = Ember.Route.extend({
    init: function() {
        console.log('Init RepoIndexRoute');
    },
    model: function(id, trans) {
        console.log('Pulling model for RepoIndexRoute: ');
        console.log(trans.resolvedModels.repo);
        return trans.resolvedModels.repo;
    },
    serialize: function(/* repo */ model) {
        if (model == null) return null;
        if (typeof model.get == 'function') {
            return { org_id: 'devops', repo_id: model.get('name') };
        }
        return { org_id: 'devops', repo_id: model.name };
    }
});

/*
Alfred.Router.map(function() {
    this.resource('orgs', function() {
        this.resource('org', { path: '/:org_id' }, function() {
            this.resource('repos', function() {
                this.resource('repo', { path: '/:repo_id' });
            });
        });
    });
});
*/

Alfred.Router.map(function() {
    this.resource('orgs', function() {
        this.resource('org', { path: '/:org_id' }, function() {
            this.resource('repos', function() {
                this.resource('repo', { path: '/:repo_id' }, function() {
                });
            });
        });
    });
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
    $.getJSON("api/history/jobs", function(data) {
        console.log('Loading history');
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
