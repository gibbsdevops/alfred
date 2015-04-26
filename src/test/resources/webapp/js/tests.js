Alfred.rootElement = '#qunit-fixture';
Alfred.setupForTesting();
Alfred.injectTestHelpers();

Alfred.Fixtures = {};
Alfred.Fixtures.Finder = {};
Alfred.Fixtures.Finder['/api/commit/123'] = { 'id': 123, 'sender': 2, 'repo': 3 };

QUnit.module( "Commit", {
  beforeEach: function( assert ) {
    Alfred.CommitsById = {};
    Alfred.reset();
  }, afterEach: function( assert ) {
  }
});

QUnit.test( "shortHash", function( assert ) {
  var commit = Alfred.Commit.create({ 'hash': '1234567890', 'sender': 2, 'repo': 3 });
  assert.equal( commit.get('shortHash'), "1234567", "Passed!" );
});

QUnit.test( "find with data", function( assert ) {
  var commit = Alfred.Commit.find(123, { 'id': 123, 'sender': 2, 'repo': 3 });
  assert.ok(commit != null);
  assert.equal( commit.get('sender_id'), 2, "sender_id" );
  assert.equal( commit.get('repo_id'), 3, "repo_id" );
  assert.strictEqual( commit, Alfred.CommitsById[123], "CommitsById" );
});

QUnit.test("find with id", function(assert) {

  Alfred.Finder.fetch = function(req) {
    req.handle(Alfred.Fixtures.Finder['/api/commit/123']);
  };

  var commit = Alfred.Commit.find(123);
  assert.ok(commit != null);
  assert.strictEqual( commit, Alfred.CommitsById[123], "CommitsById" );
  assert.equal( commit.get('sender_id'), 2, "sender_id" );
  assert.equal( commit.get('repo_id'), 3, "repo_id" );
});

QUnit.test( "find with updated data", function( assert ) {

  Alfred.Finder.fetch = function(req) {
    req.handle(Alfred.Fixtures.Finder['/api/commit/123']);
  };

  var commit = Alfred.Commit.find(123);
  var commit_new = Alfred.Commit.find(123, { 'id': 123, 'sender': 2, 'repo': 3 });

  assert.strictEqual(commit, commit_new, "returns same object" );

  assert.equal( commit.get('sender_id'), 2, "sender_id" );
  assert.equal( commit.get('repo_id'), 3, "repo_id" );
});
