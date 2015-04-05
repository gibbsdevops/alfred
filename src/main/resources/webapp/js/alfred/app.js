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
