Alfred.Persons = Ember.A([]);
Alfred.PersonsById = {};

Alfred.Person = Ember.Object.extend({
    id: null,
    name: null,
    email: null
});

Alfred.Person.find = function(id, data) {
    var finder = new Alfred.Finder(Alfred.Person, Alfred.PersonsById, Alfred.Persons);
    return finder.find(id, data);
}
