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
    var output = job.output;
    while (output.length < l.index + 1) {
        output.pushObject(Alfred.OutputLine.create({ 'index': (output.length) }));
    }

    output[l.index].set('line', l.line);
    if (output[l.index].index != l.index) throw "Line numbers do not match";

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