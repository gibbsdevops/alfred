Alfred.CommitsById = Ember.A([]);

Alfred.Commit = Ember.Object.extend({
    id: null,
    sender_id: null,
    sender: function() {
        return Alfred.UsersById[this.sender_id];
    }.property('sender_id'),
    repo_id: null,
    repo: function() {
        return Alfred.ReposById[this.repo_id];
    }.property('repo_id'),
    shortHash: function() {
        if (this.get('hash') == null) {
            return "N/A";
        }
        return this.get('hash').substring(0,7);
    }.property('hash')
});

Alfred.Commit.find = function(id, data) {
    var finder = new Alfred.Finder(Alfred.Commit, Alfred.CommitsById);
    return finder.find(id, data);
}
