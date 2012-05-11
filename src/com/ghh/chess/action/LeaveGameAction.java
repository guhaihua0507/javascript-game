package com.ghh.chess.action;

import com.ghh.chess.GameAction;
import com.ghh.chess.Gobang;

/**
 * @author haihua.gu <br>
 * @Create on May 18, 2010
 */

public class LeaveGameAction extends GameAction {

	@Override
	public void action() throws Exception {
		Long gameId = Long.parseLong(getParameter("gameId"));
		Long userId = getUser().getId();

		Gobang game = getGame(gameId);
		game.leaveGame(userId);
	}
}
