var Message = function(content, confirm, cancel) {
	this.content = content;
	this.element = null;
	this.onConfirm = confirm;
	this.onCancel = cancel;
	this.initialize();
};

Message.prototype = {
	initialize : function() {
		this.element = document.createElement("div");
		this.element.className = "popup_background";
		var container = document.createElement("div");
		container.className = "popup";
		var msgui = document.createElement("div");
		msgui.className = "popup_content";
		var btnui = document.createElement("div");
		btnui.className = "popup_bottom";
		this.element.appendChild(container);
		container.appendChild(msgui);
		container.appendChild(btnui);
		msgui.innerHTML = '<p>' + this.content + '</p>';
		btnui.appendChild(this.createConfirmButton());
		if (this.onCancel) {
			btnui.appendChild(this.createCancelButton());
		}
		document.body.appendChild(this.element);
	},

	close : function() {
		document.body.removeChild(this.element);
	},

	createConfirmButton : function() {
		var btn = document.createElement("input");
		btn.type = "button";
		btn.value = 'OK';
		var _self = this;
		if (this.onConfirm) {
			btn.onclick = function() {
				_self.onConfirm();
				_self.close();
			};
		} else {
			btn.onclick = function() {
				_self.close();
			};
		}
		return btn;
	},

	createCancelButton : function() {
		var btn = document.createElement("input");
		btn.type = "button";
		btn.value = "Cancel";
		var _self = this;
		if (this.onCancel) {
			btn.onclick = function() {
				_self.onCancel();
				_self.close();
			};
		} else {
			btn.onclick = function() {
				_self.close();
			};
		}
		return btn;
	}
};