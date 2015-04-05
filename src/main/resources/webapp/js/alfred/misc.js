Alfred.SocketClass = Ember.Object.extend({
    time_now: new Date().getTime(),
    last_received: new Date().getTime(),
    last_msg_age: function() {
        return Math.max(0, this.get('time_now') - this.get('last_received'));
    }.property('time_now', 'last_received'),
    isStale: function() {
        return this.get('last_msg_age') > 30000;
    }.property('last_msg_age'),
    msg_count: 0,
    receive_message: function(msg) {
        // console.log(msg);
        this.set('msg_count', this.get('msg_count') + 1)
        this.set('last_received', new Date().getTime());
    }
});
Alfred.Socket = Alfred.SocketClass.create();

function update_time() {
    Alfred.Socket.set('time_now', new Date().getTime())
}
window.setInterval(update_time, 1000);

Alfred.Commit = Ember.Object.extend({
    id: null,
    shortId: function() {
        if (this.get('id') == null) {
            return "";
        }
        return this.get('id').substring(0,7);
    }.property('id')
});

Alfred.OutputLine = Ember.Object.extend({
    index: null,
    line: null,
    number: function() {
        return this.get('index') + 1;
    }.property('index')
});

Alfred.RunningJobsClass = Ember.ArrayController.extend({
  jobs: Alfred.Jobs,
  model: function() {
    return this.get('jobs').filterBy('status', 'in-progress');
  }.property('jobs.@each.status'),
  sortProperties: ['id'],
  sortAscending: false
});
Alfred.RunningJobs = Alfred.RunningJobsClass.create();

Alfred.FadeInView = Ember.View.extend({
    didInsertElement: function(){
        this.$().hide().fadeIn("fast");
    }
});

Alfred.IndexRoute = Ember.Route.extend({
    model: function(params) {
        // console.log("IndexRoute.model params=" + JSON.stringify(params));
        return Alfred.SortedLatestJobs;
    },
    setupController : function(controller, model) {
        // console.log("IndexRoute.setupController model=" + JSON.stringify(model));
        controller.set('model', model);
        controller.set('running_jobs', Alfred.RunningJobs);
        controller.set('socket', Alfred.Socket);
    }
});

function sendName() {
    var name = document.getElementById('name').value;
    stompClient.send("/app/hello", {}, JSON.stringify({ 'name': name }));
}

function generateJobElement(job) {
    var pre = document.createElement('pre');
    pre.appendChild(document.createTextNode(
            "#" + job.id + " " + job.organization.login + "/" + job.repository.name + "\n"
            + job.commit.id.substring(0,7) + " (" + job.ref.replace("refs/heads/", "") + ")" + "\n"
            + job.commit.message + "\n"
        )
    );
    return pre;
}
