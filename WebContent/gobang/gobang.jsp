<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.ghh.common.game.Player"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Gobang - GAME</title>
<%
	Long gameId = (Long)request.getAttribute("gameId");
	Player player = (Player)request.getAttribute("player");
%>
<style type="text/css">
.chessboard {
	position: relative;
	width: 400px;
	height: 400px;
	border: 1px solid;
	float: left;
}

.grid {
	position: absolute;
	width: 38px;
	height: 38px;
	border: 1px solid blue;
	background-image: url("");
}

.grid_blank {
	cursor: pointer;
}

.grid_blank:hover {
	background-color: #EEEEEE;
}

.stateboard {
	position: relative;
	float: left;
	height: 402px;
} 
</style>
<script type="text/javascript">
	var Ajax = function(param) {
		this.req = null;
		this.async = false;
		this.onComplete = null;
		this.method = 'GET';
		this.initialize(param);
	};
	
	Ajax.prototype = {
		initialize : function(param) {
			this.req = this.getHttpRequest();
			for (p in param) {
				this[p] = param[p];
			}
		},

		getHttpRequest : function() {
			try {
			    return new XMLHttpRequest();
			} catch (e) {}

			try {
				return new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {}

			try {
				return new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {}

			alert('cant initialize XmlHttpRequest!');
			return null;
		},

		send : function(url) {
			var req = this.req;
			var _self = this;
			req.open(this.method, url, false);
			req.setRequestHeader("If-Modified-Since", "0"); //disable cache
			req.setRequestHeader("Cache-Control", "no-cache");
			req.onreadystatechange = function() {
				if (req.readyState == 4) {
					if (req.status == 200) {
						if (_self.onComplete) {
							_self.onComplete(req.responseText);
						}
					} else {
						alert('cant get response from server');
					}
				}
			};
			req.send(null);
		}
	};
</script>
<script type="text/javascript">
	var Grid = function(x, y, board) {
		this.value = -1;
		this.x = x;
		this.y = y;
		this.board = board;
		this.ui = null;
		this.initialize();
	};

	Grid.prototype = {
		initialize : function() {
			var pos = this.board.getGridPos(this.x, this.y);
			this.ui = document.createElement('div');
			this.ui.className = 'grid grid_blank';
			this.ui.style.left = pos.left + "px";
			this.ui.style.top = pos.top + "px";
			this.board.ui.appendChild(this.ui);
		},
		
		activate : function() {
			var _self = this;
			this.ui.onclick = function() {
				if (_self.value != -1) {
					return;
				}
				_self.board.putChess(_self);
			};
		},

		deactivate : function() {
			this.value = -1;
			this.ui.onclick = null;
			this.ui.style.backgroundImage = '';
		},

		showChess : function(value) {
			this.value = value;
			if (value == 0) {
				this.ui.style.backgroundImage = 'url("pic/chess_black.jpg")';
			} else {
				this.ui.style.backgroundImage = 'url("pic/chess_white.jpg")';
			}
		}
	};

	var Gobang = function(id, gameId, authId, playerId) {
		this.ui = document.getElementById(id);
		this.gridSize = 40;

		this.grids = null;
		this.width = 0;
		this.height = 0;

		/*game status*/
		this.gameId = gameId;
		this.authId = authId;
		this.playerId = playerId;
		this.nextAuth = 0;
		this.rival = null;

		this.isReady = false;
		this.playing = false;
		this.step = 0;

		/*transport*/
		this.intevel = 2000;	//	sync server per 2 seconds
		this.request = null;
		this.response = null;

		this.onStateChange = null;
		
		this.initialize();
	};

	Gobang.prototype = {
		initialize : function() {
			this.width = this.ui.clientWidth / this.gridSize;
			this.height = this.ui.clientHeight / this.gridSize;

			this.grids = [];
			for ( var i = 0; i < this.height; i++) {
				this.grids[i] = [];
				for ( var j = 0; j < this.width; j++) {
					this.grids[i][j] = new Grid(j, i, this);
				}
			}

			var _self = this;
			this.request = new Ajax({
				onComplete : function(data) {
					_self.processDataPackage(data);
				}
			});
			this.response = new Ajax();

			this.syncServer();
		},

		syncServer : function() {
			var url = 'gobang?gameId=' + this.gameId;
			if (this.playing == false) {
				url += '&p=00';
			} else {
				url += '&p=01&step=' + this.step;
			}
			this.request.send(url);

			var _self = this;
			setTimeout(function() {
				_self.syncServer();
			}, this.intevel);
		},
		/*get data from server and parse it*/
		processDataPackage : function(dataText) {
			if (dataText.length > 0) {
				var data = eval('(' + dataText + ')');
				var protocol = data.protocol;
				if (protocol == '00') {		//waiting
					this.rival =data.rival;
					if (this.onStateChange) {
						this.onStateChange();
					}
					return;
				}
				if (protocol == '01') {
					this.rival = data.rival;
					this.startGame();
					return;
				}
				if (protocol == '02') {		//sync state
					if (this.authId == data.authId) {
						return;
					}
					this.grids[data.y][data.x].showChess(data.authId);
					this.step++;
					this.nextAuth = this.authId;
					return;
				}
				if (protocol == '03') { //game over
					if (!this.playing) {
						return;
					}
					var winner = data.winner;
					if (winner == -1) {
						alert('draw');
					} else if (winner == this.authId) {
						alert('you win!');
					} else if (winner == this.rival.authId) {
						alert('you lost');
					}

					/*reste game*/
					this.resetGame();

					return;
				}
			}
		},

		resetGame : function() {
			this.playing = false;
			this.isReady = false;
			this.nextAuth = 0;
			this.step = 0;
			for (var i = 0; i < this.height; i++) {
				for (var j = 0; j < this.width; j++) {
					this.grids[i][j].deactivate();
				}
			}
			if (this.onStateChange) {
				this.onStateChange();
			}
		},
		
		readyPlay : function() {
			if (this.isReady) {
				return;
			}
			this.response.async = false;
			this.response.onComplete = null;
			this.response.send("gobang?p=05&gameId=" + this.gameId);
			this.isReady = true;
			if (this.onStateChange) {
				this.onStateChange();
			}
		},
		
		startGame : function() {
			for ( var i = 0; i < this.height; i++) {
				for ( var j = 0; j < this.width; j++) {
					this.grids[i][j].activate();
				}
			}
			this.playing = true;
			if (this.onStateChange) {
				this.onStateChange();
			}
		},

		quit : function() {
			this.response.async = true;
			this.response.onComplete = null;
			this.response.send("gobang?p=04&gameId=" + this.gameId);
			location.href = "lobby";
		},
		
		putChess : function(chess) {
			if (!this.playing) {
				return;
			}
			if (this.nextAuth != this.authId) {
				return;
			}
			chess.showChess(this.authId);
			
			var url = "gobang?p=02&gameId=" + this.gameId + "&chess=" + chess.x + "," + chess.y;
			this.response.onComplete = null;
			this.response.send(url);
			this.nextAuth = this.rival.authId;
			this.step++;
		},
		
		getGridPos : function(x, y) {
			var left = this.gridSize * x;
			var top = this.gridSize * y;
			return {
				left : left,
				top : top
			};
		}
	};

//---------------------------------------------------------------------------------------
	var gameId = <%= gameId%>;
	var authId = <%= player.getAuthId()%>;
	var playerId = '<%= player.getId()%>';

	var game;
	window.onload = function() {
		game = new Gobang('chessboard', gameId, authId, playerId);
		game.onStateChange = function() {
			var myStatus = document.getElementById('myStatus');
			var rivalName = document.getElementById('rivalName');
			var rivalStatus = document.getElementById('rivalStatus');
			
			if (game.playing) {
				myStatus.innerText = 'playing';
			} else {
				if (game.isReady) {
					myStatus.innerText = 'ready';
				} else {
					myStatus.innerText = 'waiting ready';
				}
			}

			if (game.rival) {
				rivalName.innerText = game.rival.name;
				if (game.playing) {
					rivalStatus.innerText = 'playing';
				} else {
					rivalStatus.innerText = 'waiting';
				}
			} else {
				rivalName.innerText = 'N/A';
				rivalStatus.innerText = 'N/A';
			}
		};
	};

	function ready2Play() {
		game.readyPlay();
	}

	function quitGame() {
		game.quit();
	}
</script>
</head>
<body>
<div>game #<%= gameId%></div>
<div id='chessboard' class='chessboard'></div>
<div class="stateboard">
 <table style="width: 100%; height: 100%;">
  <tr><td valign="bottom">
    Player:&nbsp;<%= player.getName() %><br>
  	Status:&nbsp;<span id='myStatus'>N/A</span><br><br>
  	Rival:&nbsp;<span id='rivalName'>N/A</span><br>
  	Status:&nbsp;<span id='rivalStatus'>N/A</span>
  </td></tr>
 </table>
</div>
<div style="clear: both">
	<input type='button' value='ready' onclick='ready2Play()'>
	<input type='button' value='quit' onclick='quitGame()'>
</div>
</body>
</html>