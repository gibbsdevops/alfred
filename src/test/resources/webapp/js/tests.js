Alfred.rootElement = '#qunit-fixture';
Alfred.setupForTesting();
Alfred.injectTestHelpers();

Alfred.debug = function(msg) {
  console.log(msg);
};

Alfred.Fixtures = {};
Alfred.Fixtures.Finder = {};
Alfred.Fixtures.Finder['/api/job/122'] = { 'id': 122, 'commit_id': 123 };
Alfred.Fixtures.Finder['/api/commit/123'] = { 'id': 123, 'sender_id': 2,  'repo_id': 3, 'committer_id': 4, 'author_id': 4, 'pusher_id': 4, 'hash': 'abc' };
Alfred.Fixtures.Finder['/api/repo/3'] = { 'id': 3, 'name': 'myrepo', 'owner_id': 2 };
Alfred.Fixtures.Finder['/api/user/2'] = { 'id': 2, 'name': 'UserName' };
Alfred.Fixtures.Finder['/api/person/4'] = { 'id': 4, 'name': 'PersonName', 'email': 'PersonEmail' };

Alfred.Finder.fetch = function(req) {
  console.log('Mock request GET ' + req.path)
  var response = Alfred.Fixtures.Finder[req.path];
  if (response == null) throw "Unexpected fetch request " + req.path;
  console.log('Mock request GET ' + req.path + ' done')
  req.handle(response);
};
