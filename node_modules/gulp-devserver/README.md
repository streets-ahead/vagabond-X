#Dev Server

```
var devserver = require('devserver');

var ds = devserver()
	.port(5000)
	// directories from which .js files will automatically be 
	//converted from common.js to amd on demand
	.moduleDirs(['/src'])
	.dir(__dirname)();

// when files change call
ds.notify(event);
```

##Features

  * Supports live-reloading, requires a file watcher like the one include with gulp
  * Auto converts common.js to amd to allow testing parts of your app without compiling
  * Easy to run and configure
  * Built using express