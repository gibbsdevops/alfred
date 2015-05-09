Alfred.Finder = function(clazz, index, list) {
    this.class = clazz;
    this.index = index;
    this.list = list;
};

Alfred.Finder.prototype.className = function() {
    return this.class.toString().split(".")[1];
};

Alfred.Finder.prototype.find = function(id, data) {
    if (id == null) return null;
    if (id.substring) {
        throw "id was string. expecting number.";
    }

    var existing = this.index[id];
    var obj = null;

    var copyProperties = function(finder, source, target) {
        Alfred.debug('Merging ' + finder.className() + '[' + source.id + ']');
        for (var key in source) {
            Alfred.debug('Set [' + id + '].' + key + ' = source.' + key);
            target.set(key, source[key]);
        }
    };

    if (existing != null && data != null) {
        copyProperties(this, data, existing);
        return existing;
    } else if (existing == null && data != null) {
        Alfred.debug('Creating new with data ' + this.className() + '[' + id + ']');
        obj = this.class.create(data);
        this.list.pushObject(obj);
    } else if (existing != null && data == null) {
        Alfred.debug('Returning existing ' + this.className() + '[' + id + ']');
        return existing;
    } else if (existing == null && data == null) {
        Alfred.debug('Creating shell ' + this.className() + '[' + id + ']');
        obj = this.class.create({ 'id': id });
        this.list.pushObject(obj);

        var finder = this;
        Alfred.Finder.fetch({
            'path': '/api/' + this.className().toLowerCase() + '/' + id,
            'handle': function(response) {
                copyProperties(finder, response, obj);
            }
        });
    }

    this.index[id] = obj;
    return obj;
};

Alfred.Finder.fetch = function(req) {
    Alfred.debug('Executing GET ' + req.path);
    $.get(req.path, function(response) {
        Alfred.debug('GET ' + req.path + ' Response: ' + JSON.stringify(response));
        req.handle(response);
    }, 'json');
};
