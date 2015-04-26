Alfred.Users = Ember.A([]);
Alfred.UsersById = {};

Alfred.User = Ember.Object.extend({
    id: null
});

Alfred.User.find = function(id, data) {
    var finder = new Alfred.Finder(Alfred.User, Alfred.UsersById, Alfred.Users);
    return finder.find(id, data);
}
