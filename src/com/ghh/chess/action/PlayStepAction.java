package com.ghh.chess.action;

import com.ghh.chess.GameAction;
import com.ghh.chess.GameContext;
import com.ghh.chess.Gobang;
import com.ghh.common.basic.User;
import com.ghh.common.game.GameLobby;

/**
 * @author haihua.gu <br>
 * Create on May 11, 2010
 */

public class PlayStepAction extends GameAction {

	@Override
	public void action() throws Exception {
		Long gameId = Long.parseLong(getParameter("gameId"));
		User user = (User) request.getSession().getAttribute("user");
		Long userId = user.getId();
		
		GameLobby lobby = GameContext.getContext().getLobby();
		Gobang game = (Gobang) lobby.getGame(gameId);
		
		String chess = getParameter("chess");
		String[] pos = chess.split(",");
		int x = Integer.valueOf(pos[0]);
		int y = Integer.valueOf(pos[1]);
		game.putChess(userId, x, y);
	}
}

