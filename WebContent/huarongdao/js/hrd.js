window.debugConsole = null;
var debug = function(msg) {
	if (!debugConsole) {
		return;
	}
	if (typeof debugConsole == 'string') {
		debugConsole = document.getElementById(debugConsole);
	}
	if (debugConsole) {
		debugConsole.innerHTML = debugConsole.innerHTML + "<br>" + msg;
	}
};

var GameUI = function(id) {
	this.ui = document.getElementById(id);
	this.width = null;
	this.height = null;
	this.initialize();
};

GameUI.prototype = {
	initialize : function() {
		this.width = this.ui.clientWidth;
		this.height = this.ui.clientHeight;
	},

	show : function(el, x, y) {
		if (!this.ui.contains(el)) {
			this.ui.appendChild(el);
		}
		el.style.left = x + "px";
		el.style.top = y + "px";
	},

	remove : function(element) {
		this.ui.removeChild(element);
	}
};

/*
 * w-block width, h-block height, x,y-block position in game coordinate px-cell
 * width
 */
var Block = function(w, h, x, y, game) {
	this.w = w;
	this.h = h;
	this.x = x;
	this.y = y;
	this.cell = [];
	this.ui = null;

	this.game = game;
	this.px = game.PIX;

	this.initialize();
};

Block.prototype = {
	initialize : function() {
		this.ui = document.createElement('div');
		this.ui.className = 'block';
		this.ui.style.width = (this.px * this.w - 2) + "px";
		this.ui.style.height = (this.px * this.h - 2) + "px";
		this.ui.blockObj = this;
		this.refresh();
	},

	refresh : function() {
		for ( var i = 0; i < this.h; i++) {
			this.cell[i] = [];
			for ( var j = 0; j < this.w; j++) {
				var posX = this.x + j;
				var posY = this.y + i;
				this.cell[i][j] = {
					x : posX,
					y : posY
				};
			}
		}
	},

	move : function(x, y) {
		this.x = x;
		this.y = y;
		this.refresh();
	},

	getUIPosition : function() {
		var x = this.ui.offsetLeft;
		var y = this.ui.offsetTop;
		return {
			x : x,
			y : y
		};
	},

	setUIPosition : function(x, y) {
		if (x) {
			this.ui.style.left = x + 'px';
		}
		if (y) {
			this.ui.style.top = y + 'px';
		}
	},

	setImg : function(img) {
		this.ui.style.backgroundImage = 'url(' + img + ')';
	}
};

var Game = function(id, ctx) {
	this.WIDTH = 4;
	this.HEIGHT = 5;
	this.PIX = 100;
	this.blockLayout = null;

	this.gameUI = new GameUI(id);
	this.cells = [];
	this.blocks = [];
	this.moveOff = true;

	this.isReplaying = false;
	this.history = [];
	this.stepNo = 0;

	this.onFinish = null;
	this.onReplayFinish = null;
	this.onMoveBlock = null;

	this.loadContext(ctx);
	this.initialize();
};

Game.prototype = {
	initialize : function() {
		this.initCells();
		this.initBlocks();
		this.showBlocks();
		this.setupControl();
	},

	loadContext : function(ctx) {
		if (ctx) {
			for ( var p in ctx) {
				this[p] = ctx[p];
			}
		}
	},

	start : function() {
		this.moveOff = false;
	},

	restart : function() {
		this.resetGame();
		this.start();
	},

	resetGame : function() {
		this.resetBlocks();
		this.moveOff = true;
		this.history = [];
		this.stepNo = 0;
	},

	initCells : function() {
		for ( var i = 0; i < this.HEIGHT; i++) {
			this.cells[i] = [];
			for ( var j = 0; j < this.WIDTH; j++) {
				this.cells[i][j] = 0;
			}
		}
	},

	initBlocks : function() {
		this.blocks.length = 0;
		if (!this.blockLayout) {
			this.defaultLayout();
		} else {
			this.blockLayout(this);
		}
	},

	defaultLayout : function() {
		this.addBlock(new Block(2, 2, 1, 0, this)).setImg('chr/caocao.jpg'); // caocao
		this.addBlock(new Block(1, 2, 0, 0, this)).setImg('chr/huangzhong.jpg');
		this.addBlock(new Block(1, 2, 3, 0, this)).setImg('chr/machao.jpg');
		this.addBlock(new Block(1, 2, 0, 2, this)).setImg('chr/zhaoyun.jpg');
		this.addBlock(new Block(2, 1, 1, 2, this)).setImg('chr/guanyu.jpg');
		this.addBlock(new Block(1, 1, 1, 3, this)).setImg('chr/bing.jpg');
		this.addBlock(new Block(1, 1, 2, 3, this)).setImg('chr/bing.jpg');
		this.addBlock(new Block(1, 2, 3, 2, this)).setImg('chr/zhangfei.jpg');
		this.addBlock(new Block(1, 1, 0, 4, this)).setImg('chr/bing.jpg');
		this.addBlock(new Block(1, 1, 3, 4, this)).setImg('chr/bing.jpg');
	},

	addBlock : function(block) {
		this.blocks.push(block);
		return block;
	},

	resetBlocks : function() {
		for ( var i = 0; i < this.blocks.length; i++) {
			this.removeBlock(this.blocks[i]);
		}
		this.initCells();
		this.initBlocks();
		this.showBlocks();
	},

	checkFinish : function() {
		var keyBlock = this.blocks[0];
		if (keyBlock.x == 1 && keyBlock.y == 3) {
			if (this.onFinish) {
				this.onFinish.call(this, this);
			}
			this.moveOff = true;
		}
	},

	onMoveStep : function(block, tox, toy) {
		this.stepNo++;
		this.logMoveStep(block, tox, toy);
		if (this.onMoveBlock) {
			this.onMoveBlock.call(this, this);
		}
		this.checkFinish();
	},

	replay : function() {
		if (this.history.length == 0) {
			return;
		}
		this.moveOff = true;
		this.isReplaying = true;
		this.resetBlocks();
		var self = this;
		setTimeout(function() {
			self.replayMove(0);
		}, 1000);
	},

	replayMove : function(index) {
		if (index >= this.history.length || !this.isReplaying) {
			this.isReplaying = false;
			if (this.onReplayFinish) {
				this.onReplayFinish.call(this, this);
			}
			return;
		}
		var mov = this.history[index];
		this.moveTo(this.blocks[mov.index], mov.to.x, mov.to.y);
		var self = this;
		setTimeout(function() {
			self.replayMove(++index);
		}, 1000);
	},

	showBlocks : function() {
		for ( var i = 0; i < this.blocks.length; i++) {
			this.showBlock(this.blocks[i]);
			this.updateCell(this.blocks[i], true);
		}
	},

	showBlock : function(block) {
		var pos = this.mapUIPosition(block.x, block.y);
		this.gameUI.show(block.ui, pos.x, pos.y);
	},

	removeBlock : function(block) {
		this.gameUI.remove(block.ui);
	},

	mapUIPosition : function(x, y) {
		return {
			x : this.PIX * x,
			y : this.PIX * y
		};
	},

	moveTo : function(block, x, y) {
		this.updateCell(block, false);
		block.move(x, y);
		this.showBlock(block);
		this.updateCell(block, true);
	},

	updateCell : function(block, b) {
		for ( var i = 0; i < block.cell.length; i++) {
			for ( var j = 0; j < block.cell[i].length; j++) {
				var bc = block.cell[i][j];
				this.cells[bc.y][bc.x] = b ? 1 : 0;
			}
		}
	},

	/* d - direct, 0-up, 1-right, 2-down, 3-left */
	movable : function(block, d) {
		var b = true;
		if (d == 0) {
			for ( var i = 0; i < block.cell[0].length; i++) {
				var c = block.cell[0][i];
				if (c.y == 0 || this.cells[c.y - 1][c.x] == 1) {
					b = false;
					break;
				}
			}
		} else if (d == 1) {
			var lastIndex = block.cell[0].length - 1;
			for ( var i = 0; i < block.cell.length; i++) {
				var c = block.cell[i][lastIndex];
				if (c.x == this.WIDTH - 1 || this.cells[c.y][c.x + 1] == 1) {
					b = false;
					break;
				}
			}
		} else if (d == 2) {
			var lastIndex = block.cell.length - 1;
			for ( var i = 0; i < block.cell[lastIndex].length; i++) {
				var c = block.cell[lastIndex][i];
				if (c.y == this.HEIGHT - 1 || this.cells[c.y + 1][c.x] == 1) {
					b = false;
					break;
				}
			}
		} else if (d == 3) {
			for ( var i = 0; i < block.cell.length; i++) {
				var c = block.cell[i][0];
				if (c.x == 0 || this.cells[c.y][c.x - 1] == 1) {
					b = false;
					break;
				}
			}
		} else {
			b = false;
		}
		return b;
	},

	setupControl : function() {
		var game = this;
		this.addEventListener('mousedown', function(e) {
			if (game.moveOff) {
				return;
			}
			var event = e || window.event;
			var uiElement = event.srcElement || event.target;
			var movingBlock = uiElement.blockObj;
			if (!movingBlock) {
				return;
			}
			game.blockStartMoving(movingBlock, event);
			if (movingBlock.temp && movingBlock.temp.readyMoving) {
				game.capturedBlock = movingBlock;
				if (uiElement.setCapture) {
					uiElement.setCapture();
				}
			}
			if (event.preventDefault) {
				event.preventDefault();
			}
		});

		this.addEventListener('mousemove', function(e) {
			if (game.moveOff) {
				return;
			}
			if (!game.capturedBlock) {
				return;
			}
			var movingBlock = game.capturedBlock;
			if (!movingBlock.temp || !movingBlock.temp.readyMoving) {
				return;
			}
			var event = e || window.event;
			game.blockOnMoving(movingBlock, event);
			if (event.preventDefault) {
				event.preventDefault();
			}
		});

		this.addEventListener('mouseup', function(e) {
			if (game.moveOff) {
				return;
			}
			var movingBlock = game.capturedBlock;
			if (!movingBlock) {
				return;
			}
			if (!movingBlock.temp || !movingBlock.temp.readyMoving) {
				return;
			}
			var event = e || window.event;
			game.blockEndMoving(movingBlock, event);
			if (movingBlock.ui.releaseCapture) {
				movingBlock.ui.releaseCapture();
			}
			movingBlock.temp = undefined;
			game.capturedBlock = null;
			if (event.preventDefault) {
				event.preventDefault();
			}
		});
	},

	blockStartMoving : function(block, e) {
		block.temp = new Object();
		var route = this.getMovableRoute(block);
		if (route.length > 0) {
			block.temp.route = route;
			block.temp.startPos = block.getUIPosition();
			block.temp.mousePos = {
				x : e.clientX,
				y : e.clientY
			};
			block.temp.readyMoving = true;
		} else {
			block.temp.readyMoving = false;
		}
	},

	blockOnMoving : function(block, e) {
		var ATTRICT_VALVE = 10;
		var offsetMouseX = e.clientX - block.temp.mousePos.x;
		var offsetMouseY = e.clientY - block.temp.mousePos.y;

		var dir = {
			dx : 0,
			dy : 0
		};
		var blockPos = block.getUIPosition();
		var offsetBlockLeft = blockPos.x - block.temp.startPos.x;
		var offsetBlockTop = blockPos.y - block.temp.startPos.y;
		if (offsetBlockLeft < 0) {
			dir.dx = -1;
		} else if (offsetBlockLeft > 0) {
			dir.dx = 1;
		}
		if (offsetBlockTop < 0) {
			dir.dy = -1;
		} else if (offsetBlockTop > 0) {
			dir.dy = 1;
		}

		/*
		 * decide which direction to go
		 */
		var route = block.temp.route;
		var offsetX = 0;
		var offsetY = 0;
		for ( var i = 0; i < route.length; i++) {
			if (offsetMouseX * route[i].x > 0) {
				offsetX = offsetMouseX;
			}
			if (offsetMouseY * route[i].y > 0) {
				offsetY = offsetMouseY;
			}
		}
		var isStart = (dir.dx == 0 && dir.dy == 0);
		if (dir.dx != 0 || (isStart && Math.abs(offsetX) > Math.abs(offsetY))) {
			offsetX = offsetX > 0 ? Math.min(offsetX, this.PIX) : Math.max(
					offsetX, 0 - this.PIX);
			offsetY = 0;
		} else {
			offsetY = offsetY > 0 ? Math.min(offsetY, this.PIX) : Math.max(
					offsetY, 0 - this.PIX);
			offsetX = 0;
		}

		/*
		 * decide to offset value and move
		 */
		if (offsetX != 0) {
			if (Math.abs(offsetX) <= ATTRICT_VALVE) {
				offsetX = 0;
			} else if (this.PIX - Math.abs(offsetX) <= ATTRICT_VALVE) {
				offsetX = offsetX > 0 ? this.PIX : 0 - this.PIX;
			}
			block.setUIPosition(block.temp.startPos.x + offsetX, null);
		}
		if (offsetY != 0) {
			if (Math.abs(offsetY) <= ATTRICT_VALVE) {
				offsetY = 0;
			} else if (this.PIX - Math.abs(offsetY) <= ATTRICT_VALVE) {
				offsetY = offsetY > 0 ? this.PIX : 0 - this.PIX;
			}
			block.setUIPosition(null, block.temp.startPos.y + offsetY);
		}
	},

	blockEndMoving : function(block, e) {
		var blockPos = block.getUIPosition();
		var offsetStartX = blockPos.x - block.temp.startPos.x;
		var offsetStartY = blockPos.y - block.temp.startPos.y;
		var moveX = 0;
		var moveY = 0;
		if (Math.abs(offsetStartX) >= this.PIX / 2) {
			if (offsetStartX < 0) {
				moveX = -1;
			}
			if (offsetStartX > 0) {
				moveX = 1;
			}
		} else if (Math.abs(offsetStartY) >= this.PIX / 2) {
			if (offsetStartY < 0) {
				moveY = -1;
			}
			if (offsetStartY > 0) {
				moveY = 1;
			}
		}
		if (moveX != 0 || moveY != 0) {
			var tox = block.x + moveX;
			var toy = block.y + moveY;
			this.moveTo(block, tox, toy);
			this.onMoveStep(block, tox, toy);
		} else {
			this.showBlock(block);
		}
	},

	getMovableRoute : function(block) {
		var route = [];
		if (this.movable(block, 0)) {
			route.push({
				x : 0,
				y : -1
			});
		}
		if (this.movable(block, 1)) {
			route.push({
				x : 1,
				y : 0
			});
		}
		if (this.movable(block, 2)) {
			route.push({
				x : 0,
				y : 1
			});
		}
		if (this.movable(block, 3)) {
			route.push({
				x : -1,
				y : 0
			});
		}
		return route;
	},

	logMoveStep : function(block, tox, toy) {
		var blockIndex = null;
		for ( var i = 0; i < this.blocks.length; i++) {
			if (block == this.blocks[i]) {
				blockIndex = i;
				break;
			}
		}
		if (blockIndex != null) {
			this.history.push({
				index : blockIndex,
				to : {
					x : tox,
					y : toy
				}
			});
		}
	},
	
	addEventListener : function(e, call) {
		if (window.addEventListener) { // chrome, firefox
			window.addEventListener(e, call);
		} else if (document.attachEvent) { // IE7, IE8, IE9
			document.attachEvent('on' + e, call);
		}
	}
	
};