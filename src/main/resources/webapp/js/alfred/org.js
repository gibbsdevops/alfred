Alfred.OwnerRoute = Ember.Route.extend({
    model: function(params) {
        var id = Number(params.owner_id);
        var owner = Alfred.User.find(id);
        return owner;
    },
    setupController : function(controller, model) {
        controller.set('model', model);
        controller.set('socket', Alfred.Socket);
    },
    /* serialize: function(model) {
        if (model == null) return null;
        if (typeof model.get == 'function') {
            return { owner_id: model.get('login') };
        }
        return { owner_id: model.login };
    },*/
    /*renderTemplate: function() {
        this.render('owner');
    },*/
});
