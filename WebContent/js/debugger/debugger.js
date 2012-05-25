var Debugger = {
	debug : function(s) {
		var content = this.console.innerHTML;
		if (content == null || content.length == 0) {
			this.console.innerHTML = s;
		} else {
			this.console.innerHTML = content + '<br>' + s;
		}
	},
	
	initDebugger: function() {
		var self = this;
		this.addEventListener('load', function() {
			self.console = document.createElement('div');
			self.console.className = 'debug_console';
			document.body.appendChild(self.console);
		});
	},
	
	getConsole : function() {
		if (this.console) {
			return this.console;
		}
		this.console = document.createElement('div');
		console.className = 'debug_console';
		document.body.appendChild(console);
	},
	
	addEventListener : function(e, call) {
		if (window.addEventListener) { // chrome, firefox
			window.addEventListener(e, call);
		} else if (document.attachEvent) { // IE7, IE8, IE9
			document.attachEvent('on' + e, call);
		}
	}
};
