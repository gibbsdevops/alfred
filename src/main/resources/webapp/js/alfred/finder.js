Alfred.Finder = function(clazz, index) {
    this.class = clazz;
    this.index = index;
};

Alfred.Finder.prototype.find = function(id, data) {
    var existing = this.index[id];
    var obj = null;

    if (data != null) {
        var props = [ 'sender', 'repo' ];
        props.forEach(function(prop) {
            if (data[prop]) data[prop + '_id'] = data[prop];
            delete data[prop];
        });
    }

    var copyProperties = function(source, target) {
        console.log('Merging ' + source.id);
        for (var key in source) {
            var targetKey = key;
            if (key == 'sender') targetKey = 'sender_id';
            if (key == 'committer') targetKey = 'committer_id';
            if (key == 'author') targetKey = 'author_id';
            if (key == 'pusher') targetKey = 'pusher_id';
            if (key == 'owner') targetKey = 'owner_id';
            if (key == 'repo') targetKey = 'repo_id';
            if (key == 'commit') targetKey = 'commit_id';
            console.log('Set [' + id + '].' + targetKey + ' = source.' + key);
            target.set(targetKey, source[key]);
        }
    };

    if (existing != null && data != null) {
        copyProperties(data, existing);
        return existing;
    } else if (existing == null && data != null) {
        console.log('Creating new ' + id);
        obj = this.class.create(data);
    } else if (existing != null && data == null) {
        console.log('Returning existing ' + id);
        return existing;
    } else if (existing == null && data == null) {
        console.log('Fetching ' + id);
        obj = this.class.create({ 'id': id });
        var objName = this.class.toString().split(".")[1].toLowerCase();

        Alfred.Finder.fetch({
            'path': '/api/' + objName + '/' + id,
            'handle': function(response) {
                copyProperties(response, obj);
            }
        });
    }

    this.index[id] = obj;
    return obj;
};

Alfred.Finder.fetch = function(req) {
    console.log('Executing GET ' + req.path);
    $.get(req.path, function(response) {
        console.log('GET ' + req.path + ' Response: ' + JSON.stringify(response));
        req.handle(response);
        for (var func in Alfred.Finder.hooks.after) func(response);
    }, 'json');
};
