Alfred.User = Ember.Object.extend({
    id: null,
    all_jobs: Alfred.SortedJobs,
    jobs: function() {
        return this.get('all_jobs').filterBy('owner.login', this.get('login')).slice(0, 10);
    }.property('all_jobs.@each.owner.login', 'login'),
});

Alfred.User.find = function(id, data) {
    var finder = new Alfred.Finder(Alfred.User, Alfred.UsersById, Alfred.Users);
    return finder.find(id, data);
}
