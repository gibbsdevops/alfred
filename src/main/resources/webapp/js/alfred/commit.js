Alfred.CommitsById = Ember.A([]);

Alfred.Commit = Ember.Object.extend({
    id: null,
    shortHash: function() {
        if (this.get('hash') == null) {
            return "N/A";
        }
        return this.get('hash').substring(0,7);
    }.property('hash')
});

Alfred.Commit.build = function(c) {
    commit = Alfred.Commit.create(c);

    commit.set('sender_id', commit.sender);
    commit.set('sender', Alfred.UsersById[commit.sender_id]);

    commit.set('repo_id', commit.repo);
    commit.set('repo', Alfred.ReposById[commit.repo_id]);

    Alfred.CommitsById[commit.id] = commit;
    return commit;
};
