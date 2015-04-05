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
