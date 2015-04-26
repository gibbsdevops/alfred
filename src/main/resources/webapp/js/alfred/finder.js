Alfred.Finder = function(clazz, index, list) {
    this.class = clazz;
    this.index = index;
    this.list = list;
};

Alfred.Finder.prototype.find = function(id, data) {
    var existing = this.index[id];
    var obj = null;

    var objName = this.class.toString().split(".")[1];

    function transform(data) {
        if (data != null) {
            var props = [ 'sender', 'committer', 'author', 'pusher', 'owner', 'repo', 'commit' ];
            props.forEach(function(prop) {
                if (data[prop]) data[prop + '_id'] = data[prop];
                delete data[prop];
            });
        }
    }
    transform(data);

    var copyProperties = function(source, target) {
        console.log('Merging ' + objName + '[' + source.id + ']');
        for (var key in source) {
            console.log('Set [' + id + '].' + key + ' = source.' + key);
            target.set(key, source[key]);
        }
    };

    if (existing != null && data != null) {
        copyProperties(data, existing);
        return existing;
    } else if (existing == null && data != null) {
        console.log('Creating new with data ' + objName + '[' + id + ']');
        obj = this.class.create(data);
        this.list.pushObject(obj);
    } else if (existing != null && data == null) {
        console.log('Returning existing ' + id);
        return existing;
    } else if (existing == null && data == null) {
        console.log('Creating shell ' + objName + '[' + id + ']');
        obj = this.class.create({ 'id': id });
        this.list.pushObject(obj);

        Alfred.Finder.fetch({
            'path': '/api/' + objName.toLowerCase() + '/' + id,
            'handle': function(response) {
                transform(response);
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
