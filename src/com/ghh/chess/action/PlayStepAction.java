package com.ghh.chess.action;

import com.ghh.chess.GameAction;
import com.ghh.chess.Gobang;

/**
 * @author haihua.gu <br>
 * @Create on May 11, 2010
 */

public class PlayStepAction extends GameAction {

	@Override
	public void action() throws Exception {
		Long gameId = Long.parseLong(getParameter("gameId"));
		Long userId = getUser().getId();
		Gobang game = getGame(gameId);

		String chess = getParameter("chess");
		String[] pos = chess.split(",");
		int x = Integer.valueOf(pos[0]);
		int y = Integer.valueOf(pos[1]);
		game.putChess(userId, x, y);
	}
}
