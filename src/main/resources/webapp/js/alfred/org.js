Alfred.Org = Ember.Object.extend({
    id: null,
    login: null,
    all_jobs: Alfred.SortedJobs,
    jobs: function() {
        return this.get('all_jobs').filterBy('owner.login', this.get('login')).slice(0, 10);
    }.property('all_jobs.@each.owner.login', 'login')
});

Alfred.Org.find = function(id, data) {
    // console.log("Alfred.Org.find " + id + ', ' + (data != null));
    var org = Alfred.OrgsByLogin[id];
    if (org == null) {
        console.log('New org: ' + id);
        if (data != null) {
            org = Alfred.Org.create(data);
        } else {
            org = Alfred.Org.create({ 'login': id });
        }
        Alfred.OrgsByLogin[id] = org;
    }
    return org;
};

Alfred.Orgs = Ember.A([]);
Alfred.OrgsByLogin = {};

Alfred.OrgRoute = Ember.Route.extend({
    setupController : function(controller, model) {
        controller.set('model', model);
        controller.set('socket', Alfred.Socket);
    },
    serialize: function(/* org */ model) {
        if (model == null) return null;
        if (typeof model.get == 'function') {
            return { org_id: model.get('login') };
        }
        return { org_id: model.login };
    },
    renderTemplate: function() {
        this.render('org');
    }
});
