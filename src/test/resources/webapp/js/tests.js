Alfred.rootElement = '#qunit-fixture';
Alfred.setupForTesting();
Alfred.injectTestHelpers();

Alfred.Fixtures = {};
Alfred.Fixtures.Finder = {};
Alfred.Fixtures.Finder['/api/job/122'] = { 'id': 122, 'commit': 123 };
Alfred.Fixtures.Finder['/api/commit/123'] = { 'id': 123, 'sender': 2,  'repo': 3, 'committer': 4, 'author': 4, 'pusher': 4, 'hash': 'abc' };
Alfred.Fixtures.Finder['/api/repo/3'] = { 'id': 3, 'name': 'myrepo', 'owner': 2 };
Alfred.Fixtures.Finder['/api/user/2'] = { 'id': 2, 'name': 'UserName' };
Alfred.Fixtures.Finder['/api/person/4'] = { 'id': 4, 'name': 'PersonName', 'email': 'PersonEmail' };

Alfred.Finder.fetch = function(req) {
  console.log('Mock request GET ' + req.path)
  var response = Alfred.Fixtures.Finder[req.path];
  if (response == null) throw "Unexpected fetch request " + req.path;
  req.handle(response);
};
