Alfred.Repos = Ember.A([]);
Alfred.ReposById = {};

Alfred.Repo = Ember.Object.extend({
    name: null,
    owner: null,
    all_jobs: Alfred.SortedJobs,
    jobs: function() {
        return this.get('all_jobs');
        var owner = this.get('owner');
        if (owner == null) throw "Repo is missing owner";

        var jobs = owner.get('jobs');
        if (jobs == null) throw "Jobs is null";
        // if (jobs.get('length') < 1) throw "No jobs in org";

        var name = this.get('name');
        if (name == null) throw "Repository has no name";

        var filtered = jobs.filterBy('repo.name', name);
        return filtered.slice(0, 10);
    }.property('owner', 'owner.jobs.@each.repository.name', 'name'),
    branches: function() {
        var jobs = this.get('jobs');

        var latest_jobs_by_branch = {};

        // jobs is ordered
        $.each(jobs, function(index, job) {
            /*
            var branch = job.get('branch');
            var current = latest_jobs_by_branch[branch];
            if (current == null) {
                latest_jobs_by_branch[branch] = job;
            }
            */
        });

        var latest_jobs = Ember.A([]);
        $.each(latest_jobs_by_branch, function(branch, job) {
            latest_jobs.pushObject(job);
        });

        return latest_jobs;
    }.property('jobs', 'jobs.@each.branch'),
    owner_id: null,
    owner: function() {
        return Alfred.User.find(this.get('owner_id'));
    }.property('owner_id'),
});

Alfred.Repo.find = function(id, data) {
    var finder = new Alfred.Finder(Alfred.Repo, Alfred.ReposById, Alfred.Repos);
    return finder.find(id, data);
}

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
        if (repo.id == null) repo.set('id', data.id);
        if (repo.full_name == null) repo.set('full_name', data.full_name);
        if (repo.html_url == null) repo.set('html_url', data.html_url);
        if (repo.ssh_url == null) repo.set('ssh_url', data.ssh_url);
        if (repo.git_url == null) repo.set('git_url', data.git_url);
        if (repo.clone_url == null) repo.set('clone_url', data.clone_url);
        if (repo.owner == null) repo.set('owner', data.owner);
    }

    return repo;
}

Alfred.Repos = Ember.A([]);
Alfred.ReposByPath = {};

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

            console.log(JSON.stringify(repo));

            var request = {
                'repo': repo.get('name'),
                'organization': repo.get('organization').get('login'),
                'html_url': repo.get('html_url'),
                'ssh_url': repo.get('ssh_url'),
                'git_url': repo.get('git_url'),
                'clone_url': repo.get('clone_url'),
                'spec': spec
            };

            console.log('Job Request: ' + JSON.stringify(request));
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
            return { org_id: model.get('owner').get('login'), repo_id: model.get('name') };
        }
        return { org_id: model.owner.login, repo_id: model.name };
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
