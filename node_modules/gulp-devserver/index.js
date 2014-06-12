var tinyLr = require('tiny-lr'),
    express = require('express'),
    connectLr = require('connect-livereload'),
    serveStatic = require('serve-static'),
    serveIndex = require('serve-index'),
    path = require('path'),
    injector = require('connect-injector'),
    fs = require('fs');

module.exports = function() {
	var port = 5000,
		lrPort = 35729,
		moduleDirs = [],
		ignore = [],
		dir = __dirname,
		lr;

	function self() {
		startLivereload();
		startExpress();
		return self;
	}

	function startLivereload() {
		lr = tinyLr();
		lr.listen(lrPort);
	}

	function has(str, arr) {
		return arr.filter(function(el){ return str.indexOf(el) > -1; }).length > 0;
	}

	function module(req) {
		return has(req.url, moduleDirs) && /.js$/.test(req.url) && !has(req.url, ignore);
	}

	function startExpress() {
		var inject = injector(function(req, res) {
		  return module(req);
		}, function(callback, data, req, res) {
			try {
				callback(null, 'define(function(require,exports,module){\n' + 
		  			data.toString() +
		  			'\n});');
			}catch (e) {
				console.log(e);
				callback(null, data, req, res);
			}
		  
		});

		return express()
			.use(inject)
	    .use(connectLr())
	    .use(serveStatic(dir))
	    .use(serveIndex(dir))
	    .listen(port);
	}

	function notify(event) {
		var fileName = path.relative(dir, event.path);
		lr.changed({ body: { files: [fileName] } });
		return self;
	}
	self.notify = notify;

	self.port = function(_) {
		if(!arguments.length) return port;
		port = _;
		return self;
	};

	self.lrPort = function(_) {
		if(!arguments.length) return lrPort;
		lrPort = _;
		return self;
	};

	self.moduleDirs = function(_) {
		if(!arguments.length) return moduleDirs;
		moduleDirs = _;
		return self;
	};

	self.ignore = function(_) {
		if(!arguments.length) return ignore;
		ignore = _;
		return self;
	};

	self.dir = function(_) {
		if(!arguments.length) return dir;
		dir = _;
		return self;
	};

	return self;
};	

