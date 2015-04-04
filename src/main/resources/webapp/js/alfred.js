console.log('Alfred version 1');

$.ajaxSetup({
  contentType: "application/json; charset=utf-8"
});

window.Alfred = Ember.Application.create({
  LOG_TRANSITIONS: true,
  LOG_ACTIVE_GENERATION: false,
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
        var ref = this.get('ref');
        if (ref) return ref.replace("refs/heads/", "");
        return null;
    }.property('ref'),
    output: null,
    last_line: function() {
        output = this.get('output');
        l = output.get('lastObject');
        console.log("Last line is " + l)
        return l;
    }.property('output', 'output.@each')
});

Alfred.Job.AddLine = function(job, l) {
    var line = Alfred.OutputLine.create(l);

    var output = job.output;
    if (output.length < line.index + 1) {
        output.pushObject(line);
    }

    output[line.index].set('line', line.line);

    $(".mini-console").scrollTop(10000);
}

Alfred.Job.find = function(id) {
    id = parseInt(id);

    // console.log("Alfred.Org.find " + id + ', ' + (data != null));
    var job = Alfred.JobsById[id];
    if (job == null) {
        console.log('New job: ' + id);
        job = Alfred.Job.create({ 'id': id, 'version': -1, 'output': Ember.A([]) });
        Alfred.Jobs.pushObject(job);
        Alfred.JobsById[id] = job;

        $.get("api/jobs/" + id, function(response) {
            console.log('GET Job Response: ' + JSON.stringify(response));
            Alfred.Job.merge(job, response);
        }, 'json');

    }
    return job;
};

Alfred.ParseRawOrg = function(raw) {
    if (raw['organization'] && raw['organization']['login']) return raw['organization'];
    return { 'id': raw['repository']['owner']['email'], 'login': raw['repository']['owner']['name'] }
};

Alfred.Job.build = function(j) {
    job = Alfred.Job.create(j);
    job.set('id', parseInt(job.get('id')));
    job.set('commit', Alfred.Commit.create(job.commit));
    job.set('output', []);

    var plainOrg = Alfred.ParseRawOrg(j);
    var org = Alfred.Org.find(plainOrg.login, plainOrg);
    if (org == null) throw "org is null"
    if (org.login == null) throw "org.login is null"
    job.set('organization', org);

    // parse repo into tree
    var repo = Alfred.Repo.findByOrgAndName(org, j['repository']['name'], j['repository']);
    job.set('repository', repo);

    return job;
};

Alfred.Job.merge = function(job, data) {
    if (data.id == null) throw "Will not merge job with no id";
    if (data.version == null) throw "Will not merge job with no version";
    if (job.get('id') != data.id) throw "Can not merge jobs with different id's";

    if (job.get('version') < data.version) {
        console.log('Received job update. Version=' + data.version + ', current version=' + job.get('version'));
        job.set('version', data.version);
        job.set('status', data.status);
        job.set('error', data.error);
        job.set('ref', data.ref);

        if (job.get('commit') == null) {
            job.set('commit', Alfred.Commit.create(data.commit));
        }
        var org = job.get('organization');
        if (org == null) {
            var plainOrg = Alfred.ParseRawOrg(data);
            org = Alfred.Org.find(plainOrg.login, plainOrg);
            job.set('organization', org);
        }
        if (job.get('repository') == null) {
            job.set('repository', Alfred.Repo.findByOrgAndName(org, data.repository.name, data.repository));
        }
        if (job.get('commit') == null) {
            job.set('commit', Alfred.Commit.create(data.commit));
        }
    } else {
        console.log('Received stale job update. Version=' + data.version + ', current version=' + job.get('version'));
    }
};

Alfred.Job.LoadOutput = function(job) {
    $.get("api/jobs/" + job.get('id') + '/output', function(response) {
        console.log('GET Job Output Response: ' + JSON.stringify(response));
        // Alfred.Job.merge(job, response);

        $.each(response.output, function(index, line) {
            Alfred.Job.AddLine(job, { 'index': index, 'line': line });
        });

    }, 'json');
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
    index: null,
    line: null,
    number: function() {
        return this.get('index') + 1;
    }.property('index')
});

Alfred.Jobs = Ember.A([]);
Alfred.JobsById = {};

Alfred.SortedJobs = Ember.ArrayController.create({
  model: Alfred.Jobs,
  sortProperties: ['id'],
  sortAscending: false
});

Alfred.SortedLatestJobsClass = Ember.Object.extend({
    model: function() {
        return this.get('ref').slice(0, 100);
    }.property('ref', 'ref.@each')
});
Alfred.SortedLatestJobs = Alfred.SortedLatestJobsClass.create({ ref: Alfred.SortedJobs });

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
        return Alfred.SortedLatestJobs;
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
        return this.get('all_jobs').filterBy('organization.login', this.get('login')).slice(0, 10);
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
        return filtered.slice(0, 10);
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
    // console.log("Alfred.Repo.findByOrgAndName " + org.login + ', ' + name + ', ' + (data != null));
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
            var spec = this.get('gitSpec');
            if (spec == '') spec = 'master';

            var request = {
                'repo': repo.get('name'),
                'organization': repo.get('organization').get('login'),
                'spec': spec
            };

            console.log('Job Response: ' + JSON.stringify(request));
            $.post("api/jobs", JSON.stringify(request), function(response) {
                console.log('Job Response: ' + JSON.stringify(response));
            }, 'json');

        }
    },
    gitSpec: ''
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

Alfred.JobRoute = Ember.Route.extend({
    init: function() {
        console.log('init JobRoute');
    },
    model: function(params) {
        var job = Alfred.Job.find(params.id);
        Alfred.Job.LoadOutput(job);
        return job;
    },
    setupController: function(controller, model) {
        this._super(controller, model);
        Alfred.Job.LoadOutput(model);
    }
});

Alfred.JobIndexRoute = Ember.Route.extend({
    init: function() {
        console.log('init JobIndexRoute');
    },
    model: function(id, trans) {
        return trans.resolvedModels.job;
    }
});

Alfred.Router.map(function() {
    this.resource('orgs', function() {
        this.resource('org', { path: '/:org_id' }, function() {
            this.resource('repos', function() {
                this.resource('repo', { path: '/:repo_id' }, function() {
                });
            });
        });
    });
    this.resource('jobs', function() {
        this.resource('job', { path: '/:id' }, function() {
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
    $.getJSON("api/jobs?limit=100", function(data) {
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

function JobIdsCompare(a, b) {
  return parseInt(a) - parseInt(b);
}

function handleJob(j) {
    job = Alfred.Job.build(j);

    existing = Alfred.JobsById[job.get('id')];
    if (existing != null) {
        Alfred.Job.merge(existing, job);
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

}

function alfred_main(live) {
    if (live) {
        connect();
        load_history();
    }
}

alfred_main(true);
