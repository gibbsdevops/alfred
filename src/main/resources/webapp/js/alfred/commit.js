Alfred.Commit = Ember.Object.extend({
    id: null,
    sender_id: null,
    sender: function() {
        return Alfred.User.find(this.get('sender_id'));
    }.property('sender_id'),
    committer_id: null,
    committer: function() {
        return Alfred.Person.find(this.get('committer_id'));
    }.property('committer_id'),
    author_id: null,
    author: function() {
        return Alfred.Person.find(this.get('author_id'));
    }.property('author_id'),
    pusher_id: null,
    pusher: function() {
        return Alfred.Person.find(this.get('pusher_id'));
    }.property('pusher_id'),
    repo_id: null,
    repo: function() {
        return Alfred.Repo.find(this.get('repo_id'));
    }.property('repo_id'),
    shortHash: function() {
        if (this.get('hash') == null) {
            return "N/A";
        }
        return this.get('hash').substring(0,7);
    }.property('hash')
});

Alfred.Commit.find = function(id, data) {
    var finder = new Alfred.Finder(Alfred.Commit, Alfred.CommitsById, Alfred.Commits);
    return finder.find(id, data);
}
