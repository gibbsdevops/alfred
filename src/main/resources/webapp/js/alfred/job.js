Alfred.Job = Ember.Object.extend({
    init: function() {
        this.set('output', Ember.A([]));
    },
    id: null,
    commit_id: null,
    commit: function() {
        return Alfred.Commit.find(this.get('commit_id'));
    }.property('commit_id'),
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
    }.property('output', 'output.@each'),
    isErrored: function() {
        return this.get('status') == 'errored';
    }.property('status')
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

Alfred.Job.find = function(id, data) {
    var finder = new Alfred.Finder(Alfred.Job, Alfred.JobsById, Alfred.Jobs);
    var job = finder.find(id, data);
    return job;
}

Alfred.ParseRawOrg = function(raw) {
    if (raw['organization'] && raw['organization']['login']) return raw['organization'];
    return { 'id': raw['repository']['owner']['email'], 'login': raw['repository']['owner']['name'] }
};

Alfred.Job.LoadOutput = function(job) {
    $.get("api/job/" + job.get('id') + '/output', function(response) {
        Alfred.debug('GET Job Output Response: ' + JSON.stringify(response));
        // Alfred.Job.merge(job, response);

        $.each(response, function(index, line) {
            Alfred.Job.AddLine(job, line);
        });

    }, 'json');
};

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
        var job = Alfred.Job.find(parseInt(params.id));
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
