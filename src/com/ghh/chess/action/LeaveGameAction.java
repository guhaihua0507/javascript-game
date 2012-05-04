package com.ghh.chess.action;

import com.ghh.chess.GameAction;
import com.ghh.chess.GameContext;
import com.ghh.chess.Gobang;
import com.ghh.common.basic.User;
import com.ghh.common.game.GameLobby;

/**
 * @author haihua.gu <br>
 * Create on May 18, 2010
 */

public class LeaveGameAction extends GameAction {

	@Override
	public void action() throws Exception {
		Long gameId = Long.parseLong(getParameter("gameId"));
		User user = (User) session.getAttribute("user");
		Long userId = user.getId();
		
		GameLobby lobby = GameContext.getContext().getLobby();
		Gobang game = (Gobang) lobby.getGame(gameId);
		
		game.leaveGame(userId);
		System.out.println("[leave game] player:" + userId + ", game:#" + gameId);
	}
}

