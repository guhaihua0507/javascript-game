package com.ghh.chess.action;

import com.ghh.chess.GameAction;
import com.ghh.chess.Gobang;

/**
 * @author haihua.gu <br>
 * @Create on May 10, 2010
 */

public class ReadyAction extends GameAction {

	@Override
	public void action() throws Exception {
		Gobang game = getGame(getLongParameter("gameId"));
		Long userId = getUser().getId();
		game.readyPlayer(userId);
	}
}
