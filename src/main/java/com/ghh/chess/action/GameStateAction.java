package com.ghh.chess.action;

import org.json.JSONObject;

import com.ghh.chess.GameAction;
import com.ghh.chess.Gobang;
import com.ghh.chess.Protocols;
import com.ghh.common.game.Game;
import com.ghh.common.game.Player;

/**
 * @author haihua.gu <br>
 *         Create on May 10, 2010
 */

public class GameStateAction extends GameAction {

	@Override
	public void action() throws Exception {
		Long gameId = getLongParameter("gameId");
		Long userId = getUser().getId();
		Gobang game = getGame(gameId);

		if (game.getPlayer(userId) == null) {
			return;
		}

		Player other = getOtherPlayer(game, userId);
		JSONObject otherPlayer = null;
		if (other != null) {
			otherPlayer = new JSONObject();
			otherPlayer.put("playerId", other.getUserId());
			otherPlayer.put("name", other.getName());
			otherPlayer.put("playNo", other.getPlayNo());
			otherPlayer.put("status", other.getStatus());
		}

		int status = game.getStatus();
		if (status == Game.STATUS_WAITING) {
			JSONObject data = new JSONObject();
			data.put("protocol", Protocols.CLIENT_PROTOCOL_WAITING);
			if (other != null) {
				data.put("rival", otherPlayer);
			}
			sendMessage(data.toString());
		} else if (status == Game.STATUS_PLAYING) {
			JSONObject data = new JSONObject();
			data.put("protocol", Protocols.CLIENT_RPOTOCOL_START);
			data.put("rival", otherPlayer);
			sendMessage(data.toString());
		}
	}

	private Player getOtherPlayer(Gobang game, Long myId) {
		for (Player p : game.getPlayers()) {
			if (!p.getUserId().equals(myId)) {
				return p;
			}
		}
		return null;
	}
}
