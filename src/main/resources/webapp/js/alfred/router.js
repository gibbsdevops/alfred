Alfred.Router.map(function() {
    this.resource('owners', function() {
        this.resource('owner', { path: '/:owner_id' }, function() {
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
