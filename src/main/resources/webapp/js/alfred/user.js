Alfred.UsersById = Ember.A([]);

Alfred.User = Ember.Object.extend({
    id: null
});

Alfred.User.find = function(id, data) {
    var finder = new Alfred.Finder(Alfred.User, Alfred.UsersById);
    return finder.find(id, data);
}
