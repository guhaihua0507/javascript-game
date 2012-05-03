package com.ghh.chess.action;

import com.ghh.chess.GameAction;
import com.ghh.chess.GameContext;
import com.ghh.chess.Gobang;
import com.ghh.chess.Protocols;
import com.ghh.common.basic.DataPackage;
import com.ghh.common.basic.User;
import com.ghh.common.game.Game;
import com.ghh.common.game.GameLobby;
import com.ghh.common.game.Player;

/**
 * @author haihua.gu <br>
 * Create on May 10, 2010
 */

public class GameStateAction extends GameAction {

	@Override
	public void action() throws Exception {
		String id = getParameter("gameId");
		Long gameId = Long.parseLong(id);
		
		User user = (User) session.getAttribute("user");
		Long userId = user.getId();
		
		GameLobby lobby = GameContext.getContext().getLobby();
		Gobang game = (Gobang) lobby.getGame(gameId);
		
		if (game.getPlayer(userId) == null) {
			return;
		}
		
		Player other = getOtherPlayer(game, userId);
		String rival = null;
		if (other != null) {
			DataPackage otherPlayer = new DataPackage();
			otherPlayer.addValue("playerId", String.valueOf(other.getId()));
			otherPlayer.addValue("name", other.getName());
			otherPlayer.addValue("authId", String.valueOf(other.getAuthId()));
			rival = otherPlayer.getDataString();
		}
		
		int status = game.getStatus();
		if (status == Game.STATUS_WAITING) {
			DataPackage data = new DataPackage();
			data.addValue("protocol", Protocols.CLIENT_PROTOCOL_WAITING);
			if (other != null) {
				System.out.println("rival:" + other.getId());
				data.addObject("rival", rival);
			} else {
				System.out.println("no rival");
			}
			writeLine(data.getDataString());
			
			System.out.println("[game state] player:" + userId + ", game:#" + gameId);
			return;
		}
		
		if (status == Game.STATUS_PLAYING) {
			DataPackage data = new DataPackage();
			data.addValue("protocol", Protocols.CLIENT_RPOTOCOL_START);
			data.addObject("rival", rival);
			writeLine(data.getDataString());
			return;
		}
	}
	
	private Player getOtherPlayer(Gobang game, Long myId) {
		for (Player p : game.getPlayers()) {
			if (!p.getId().equals(myId)) {
				return p;
			}
		}
		return null;
	}
}

