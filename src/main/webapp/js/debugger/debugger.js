var Debugger = {
	debug : function(s) {
		var text = document.createTextNode();
		text.data = s;
		this.console.appendChild(text);
		this.console.appendChild(document.createElement('br'));
	},
	
	initDebugger: function() {
		var self = this;
		this.addEventListener('load', function() {
			self.console = document.createElement('div');
			self.console.className = 'debug_console';
			document.body.appendChild(self.console);
			
			var hidden =  document.createElement('div');
			hidden.className = 'debug_hidden';
			document.body.appendChild(hidden);
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
