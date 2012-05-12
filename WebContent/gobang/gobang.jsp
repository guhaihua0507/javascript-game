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
		this.onError = null;
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
						if (_self.onError) {
							_self.onError();
						} else {
							alert('cant get response from server');
						}
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

	var Gobang = function(id, gameId, playNo, playerId) {
		this.ui = document.getElementById(id);
		this.gridSize = 40;

		this.grids = null;
		this.width = 0;
		this.height = 0;

		/*game status*/
		this.gameId = gameId;
		this.playNo = playNo;
		this.playerId = playerId;
		this.currentPlayNo = 0;
		this.otherPlayer = null;

		this.isReady = false;
		this.playing = false;
		this.step = 0;

		/*transport*/
		this.interval = 2000;	//	sync server per 2 seconds
		this.connectError = false;
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
				},
				
				onError: function() {
					alert('can not access to server');
					_self.connectError = true;
				}
			});
			this.response = new Ajax();

			this.syncServer();
		},

		syncServer : function() {
			var url = 'gobang?gameId=' + this.gameId;
			url += "&status=" + (this.playing ? '2' : (this.isReady ? '1' : '0'));
			if (this.playing == false) {
				url += '&p=00';
			} else {
				url += '&p=01&step=' + this.step;
			}
			this.request.send(url);

			var _self = this;
			setTimeout(function() {
				if (!_self.connectError) {
					_self.syncServer();
				}
			}, this.interval);
		},
		/*get data from server and parse it*/
		processDataPackage : function(dataText) {
			if (dataText.length > 0) {
				var data = eval('(' + dataText + ')');
				var protocol = data.protocol;
				if (protocol == '00') {
					/*
					 * game is still waiting player to ready
					 */
					this.otherPlayer = data.rival;
				} else if (protocol == '01') {
					/*game start*/
					this.otherPlayer = data.rival;
					this.startGame();
				} else if (protocol == '02') {
					/*sync game status*/
					if (this.playNo == data.playNo) {
						return;
					}
					this.grids[data.y][data.x].showChess(data.playNo);
					this.step++;
					this.currentPlayNo = this.playNo;
				} else if (protocol == '03') {
					/*game is over*/
					if (!this.playing) {
						return;
					}
					//var winner = data.winner;
					var msg = data.msg;
					alert(msg);
					/*reste game*/
					this.resetGame();
				}
				
				this.onGameStateChange();
			}
		},

		resetGame : function() {
			this.playing = false;
			this.isReady = false;
			this.currentPlayNo = 0;
			this.step = 0;
			for (var i = 0; i < this.height; i++) {
				for (var j = 0; j < this.width; j++) {
					this.grids[i][j].deactivate();
				}
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
			this.onGameStateChange();
		},
		
		startGame : function() {
			for ( var i = 0; i < this.height; i++) {
				for ( var j = 0; j < this.width; j++) {
					this.grids[i][j].activate();
				}
			}
			this.playing = true;
		},

		quit : function() {
			this.response.async = false;
			this.response.onComplete = function() {
				location.href = "lobby";
			};
			this.response.onError = function() {
				location.href = "lobby";
			};
			this.response.send("gobang?p=04&gameId=" + this.gameId);
		},
		
		onGameStateChange: function() {
			if (this.onStateChange) {
				this.onStateChange();
			}
		},
		
		putChess : function(chess) {
			if (!this.playing) {
				return;
			}
			if (this.currentPlayNo != this.playNo) {
				return;
			}
			chess.showChess(this.playNo);
			
			var url = "gobang?p=02&gameId=" + this.gameId + "&chess=" + chess.x + "," + chess.y;
			this.response.onComplete = null;
			this.response.send(url);
			this.currentPlayNo = this.otherPlayer.playNo;
			this.step++;
			//notify state change
			this.onGameStateChange();
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
	var playNo = <%=player.getPlayNo()%>;
	var playerId = '<%= player.getUserId()%>';

	var game;
	window.onload = function() {
		game = new Gobang('chessboard', gameId, playNo, playerId);
		game.onStateChange = function() {
			var playerStatus = document.getElementById('playerStatus');
			var playerStatusImg = document.getElementById('playerStatusImg');
			
			var otherPlayerName = document.getElementById('otherPlayerName');
			var otherPlayerStatus = document.getElementById('otherPlayerStatus');
			var otherPlayerStatusImg = document.getElementById('otherPlayerStatusImg');
			
			if (game.playing) {
				playerStatus.innerHTML = 'Playing';
			} else {
				if (game.isReady) {
					playerStatus.innerHTML = 'Ready';
				} else {
					playerStatus.innerHTML = 'Waiting';
				}
			}

			if (game.otherPlayer) {
				otherPlayerName.innerHTML = game.otherPlayer.name;
				var status = game.otherPlayer.status;
				var statusStr = '';
				if (status == 0) {
					statusStr = 'Waiting';
				} else if (status == 1) {
					statusStr = 'Ready';
				} else if (status == 2) {
					statusStr = 'Playing';
				}
				otherPlayerStatus.innerHTML = statusStr;
			} else {
				otherPlayerName.innerHTML = 'N/A';
				otherPlayerStatus.innerHTML = 'N/A';
			}
			
			if (!game.playing) {
				playerStatusImg.style.display = "none";
				otherPlayerStatusImg.style.display = "none";
			} else {
				if (game.currentPlayNo == game.playNo) {
					playerStatusImg.style.display = "block";
				} else {
					playerStatusImg.style.display = "none";
				}
				
				if (game.currentPlayNo == game.otherPlayer.playNo) {
					otherPlayerStatusImg.style.display = "block";
				} else {
					otherPlayerStatusImg.style.display = "none";
				}
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
  	<table>
  		<tr height="40px" align="left">
  			<td width="20"><span id="playerStatusImg" style="display: none;"><img src="pic/waiting.gif"></span></td>
  			<td><span><%= player.getName() %></span></td>
  			<td><span id='playerStatus'>N/A</span></td>
  		</tr>
  		<tr height="40px" align="left">
  			<td><span id="otherPlayerStatusImg" style="display: none;"><img src="pic/waiting.gif"></span></td>
  			<td><span id='otherPlayerName'>N/A</span></td>
  			<td><span id='otherPlayerStatus'>N/A</span></td>
  		</tr>
  	</table>
  </td></tr>
 </table>
</div>
<div style="clear: both">
	<input type='button' value='ready' onclick='ready2Play()'>
	<input type='button' value='quit' onclick='quitGame()'>
</div>
</body>
</html>