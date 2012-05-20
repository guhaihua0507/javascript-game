var Popup = {
	newDialog: function(msg) {
		return new Dialog(msg);
	}
};

var Dialog = function(message) {
	this.message = message || '';
	this.buttons = [];
	
	this.background = null;
	this.messageContainer = null;
	this.buttonContainer = null;
	
	this.initialize();
};

Dialog.prototype = {
	initialize : function() {
		this.background = document.createElement("div");
		this.background.className = "popup_background";

		var dialog = document.createElement("div");
		dialog.className = "popup";
		this.background.appendChild(dialog);
		
		this.messageContainer = document.createElement("div");
		this.messageContainer.className = "popup_content";
		dialog.appendChild(this.messageContainer);

		this.buttonContainer = document.createElement("div");
		this.buttonContainer.className = "popup_bottom";
		dialog.appendChild(this.buttonContainer);
	},

	show: function() {
		/*show message*/
		this.messageContainer.innerHTML = '<p>' + this.message + '</p>';
		
		/*show buttons*/
		if (this.buttons == null || this.buttons.length == 0) {
			this.buttonContainer.appendChild(this.createButton({}));
		} else {
			for (var i = 0; i < this.buttons.length; i++) {
				this.buttonContainer.appendChild(this.createButton(this.buttons[i]));
			}
		}
		
		document.body.appendChild(this.background);
	},
	
	addButton: function(button) {
		if (this.buttons == null) {
			this.buttons = [];
		}
		this.buttons.push(button);
	},
	
	close : function() {
		document.body.removeChild(this.background);
	},

	createButton: function(button) {
		var _self = this;
		var value = button.value || 'Ok';
		var action = button.action;
		
		var ui = document.createElement("input");
		ui.type = "button";
		ui.value = value;
		if (action) {
			ui.onclick = function() {
				action();
				_self.close();
			};
		} else {
			ui.onclick = function() {
				_self.close();
			};
		}
		return ui;
	}
};