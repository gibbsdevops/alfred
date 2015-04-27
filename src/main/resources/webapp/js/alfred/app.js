console.log('Alfred version 1');

$.ajaxSetup({
  contentType: "application/json; charset=utf-8"
});

window.Alfred = Ember.Application.create({
  LOG_TRANSITIONS: true,
  LOG_ACTIVE_GENERATION: false,
  LOG_VIEW_LOOKUPS: false,
  LOG_RESOLVER: false
});

Alfred.debug = function(msg) {
  // console.log(msg);
};

Alfred.Models = [
    'Job',
    'Commit',
    'Repo',
    'User',
    'Person',
];

Alfred.resetStores = function() {
    for (var model in Alfred.Models) {
        Alfred[model + 's'] = Ember.A([]);
        Alfred[model + 'sById'] = {};
    }
    // TODO revert this to explicit for now
};

// Alfred.resetStores();
