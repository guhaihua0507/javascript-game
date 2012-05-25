var MoveEffect = function(inteval, movPix) {
	this.interval = 2 || inteval;
	this.movPix = 3 || movPix;
};

MoveEffect.prototype = {
	startMove : function(obj, tox, toy, callback) {
		var offsetX = tox - obj.offsetLeft;
		var offsetY = toy - obj.offsetTop;
		var step = {
			x : 0,
			y : 0
		};
		if (Math.abs(offsetX) > Math.abs(offsetY)) {
			step.x = offsetX > 0 ? this.movPix : 0 - this.movPix;
			if (offsetY != 0) {
				var v = parseInt(Math.abs(offsetX) / Math.abs(offsetY), 10)
						* this.movPix;
				step.y = offsetY > 0 ? v : 0 - v;
			}
		} else {
			step.y = offsetY > 0 ? this.movPix : 0 - this.movPix;
			if (offsetX != 0) {
				var v = parseInt(Math.abs(offsetY) / Math.abs(offsetX), 10)
						* this.movPix;
				step.x = offsetX > 0 ? v : 0 - v;
			}
		}
		this.moveStep(obj, tox, toy, step, callback);
	},

	moveStep : function(obj, tox, toy, step, callback) {
		var posx = obj.offsetLeft;
		var posy = obj.offsetTop;
		if ((posx == tox && posy == toy)) {
			if (callback) {
				callback.call();
			}
			return;
		}

		var mov2x = step.x > 0 ? Math.min(tox, posx + step.x) : Math.max(tox,
				posx + step.x);
		var mov2y = step.y > 0 ? Math.min(toy, posy + step.y) : Math.max(toy,
				posy + step.y);
		obj.style.left = mov2x + "px";
		obj.style.top = mov2y + "px";

		var self = this;
		setTimeout(function() {
			self.moveStep(obj, tox, toy, step, callback);
		}, this.interval);
	}
};