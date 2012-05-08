package com.ghh.chess.action;

import com.ghh.chess.GameAction;
import com.ghh.chess.GameContext;
import com.ghh.chess.Gobang;
import com.ghh.common.basic.User;
import com.ghh.common.game.GameLobby;

/**
 * @author haihua.gu <br>
 * @Create on May 10, 2010
 */

public class ReadyAction extends GameAction {

	@Override
	public void action() throws Exception {
		Long gameId = Long.parseLong(getParameter("gameId"));
		User user = (User) request.getSession().getAttribute("user");
		Long userId = user.getId();

		GameLobby lobby = GameContext.getContext().getLobby();
		Gobang game = (Gobang) lobby.getGame(gameId);

		game.readyPlayer(userId);

		System.out.println("[ready to play] player:" + userId + ", game:#" + gameId);
	}
}
