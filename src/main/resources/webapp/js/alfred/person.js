Alfred.Persons = Ember.A([]);
Alfred.PersonsById = {};

Alfred.Person = Ember.Object.extend({
    id: null,
    name: null,
    email: null,
    all_jobs: Alfred.SortedJobs,
    jobs: function() {
        return this.get('all_jobs').filterBy('owner.login', this.get('login')).slice(0, 10);
    }.property('all_jobs.@each.owner.login', 'login'),
});

Alfred.Person.find = function(id, data) {
    var finder = new Alfred.Finder(Alfred.Person, Alfred.PersonsById, Alfred.Persons);
    return finder.find(id, data);
}
