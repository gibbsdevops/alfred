QUnit.module( "Job", {
  beforeEach: function( assert ) {
    Alfred.resetStores();
    Alfred.reset();
  }, afterEach: function( assert ) {
  }
});

QUnit.test( "find with data and traverse graph", function( assert ) {
  var job = Alfred.Job.find(122);
  assert.ok(job != null, "job not null");
  assert.strictEqual(job, Alfred.JobsById[122], "returns same object" );

  // job -> commit
  var commit = job.get('commit');
  assert.ok(commit != null, 'commit not null');
  assert.equal(commit.get('id'), 123);
  assert.equal(commit.get('hash'), 'abc');

  // job -> commit -> sender
  var sender = commit.get('sender');
  assert.ok(sender != null, 'sender not null');
  assert.equal(sender.get('id'), 2, 'sender id');

  // job -> commit -> committer
  var committer = commit.get('committer');
  assert.ok(committer != null, 'committer not null');
  assert.equal(committer.get('id'), 4, 'committer id');
  assert.equal(committer.get('name'), 'PersonName', 'committer name');
  assert.equal(committer.get('email'), 'PersonEmail', 'committer email');

  // job -> commit -> author
  var author = commit.get('author');
  assert.ok(author != null, 'author not null');
  assert.equal(author.get('id'), 4, 'author id');
  assert.strictEqual(author, committer, "author and committer are same object" );

  // job -> commit -> pusher
  var pusher = commit.get('pusher');
  assert.ok(pusher != null, 'pusher not null');
  assert.equal(pusher.get('id'), 4, 'pusher id');
  assert.strictEqual(pusher, committer, "pusher and committer are same object" );

  // job -> commit -> repo
  var repo = commit.get('repo');
  assert.ok(repo != null, 'repo not null');
  assert.equal(repo.get('id'), 3, 'repo id');
  assert.equal(repo.get('name'), 'myrepo', 'repo name');

  // job -> commit -> repo -> owner
  var owner = repo.get('owner');
  assert.ok(owner != null, 'owner not null');
  assert.equal(owner.get('id'), 2, 'owner id');
  assert.equal(owner.get('name'), 'UserName', 'owner name');
  assert.strictEqual(owner, sender, "owner and sender are same object" );

});
