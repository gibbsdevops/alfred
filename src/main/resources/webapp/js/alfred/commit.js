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
    return commit;
};
