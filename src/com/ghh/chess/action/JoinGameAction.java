package com.ghh.chess.action;

import com.ghh.chess.GameAction;
import com.ghh.chess.GameContext;
import com.ghh.chess.Gobang;
import com.ghh.common.basic.User;
import com.ghh.common.game.GameLobby;
import com.ghh.common.game.Player;

/**
 * @author haihua.gu <br>
 * @create on May 10, 2010
 */

public class JoinGameAction extends GameAction {

	@Override
	public void action() throws Exception {
		Long gameId = Long.parseLong(getParameter("gameId"));
		User user = (User) session.getAttribute("user");
		Long userId = user.getId();

		GameLobby lobby = GameContext.getContext().getLobby();
		Gobang game = (Gobang) lobby.getGame(gameId);

		Player player = new Player(user);
		if (game.getPlayer(userId) != null || game.joinGame(player)) {
			request.setAttribute("gameId", game.getId());
			request.setAttribute("player", game.getPlayer(userId));

			request.getRequestDispatcher("/gobang/gobang.jsp").forward(request, response);
		} else {
			writeLine("can not join game!");
		}

		System.out.println("[join game] player:" + userId + ", game:#" + gameId);
	}
}
