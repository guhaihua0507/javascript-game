package com.ghh.chess.action;

import com.ghh.chess.GameAction;
import com.ghh.chess.GameContext;
import com.ghh.chess.Gobang;
import com.ghh.chess.Protocols;
import com.ghh.chess.Step;
import com.ghh.common.basic.DataPackage;
import com.ghh.common.basic.User;
import com.ghh.common.game.Game;
import com.ghh.common.game.GameLobby;

/**
 * @author haihua.gu <br>
 *         Create on May 10, 2010
 */

public class SyncStateAction extends GameAction {

	@Override
	public void action() throws Exception {
		Long gameId = Long.parseLong(getParameter("gameId"));
		int stepNo = Integer.parseInt(request.getParameter("step"));

		GameLobby lobby = GameContext.getContext().getLobby();
		Gobang game = (Gobang) lobby.getGame(gameId);

		if (game.getStepNo() <= stepNo) {
			if (game.getStatus() == Game.STATUS_WAITING) {
				DataPackage data = new DataPackage();
				data.addValue("protocol", Protocols.CLIENT_PROTOCOL_GAMEOVER);
				data.addValue("winner", String.valueOf(game.getWinner()));
				writeLine(data.getDataString());
			}
		} else if (game.getStepNo() == stepNo + 1) {
			Step step = game.getLastStep();
			DataPackage data = new DataPackage();
			data.addValue("protocol", Protocols.ClIENT_PROTOCOL_SYNC);
			data.addValue("authId", String.valueOf(step.getPlayer().getAuthId()));
			data.addValue("x", String.valueOf(step.getX()));
			data.addValue("y", String.valueOf(step.getY()));
			data.addValue("serialNo", String.valueOf(step.getSerialNo()));

			writeLine(data.getDataString());

			System.out.println("[sync state] player:" + ((User) session.getAttribute("user")).getId() + ", step:#" + stepNo);
		}
	}
}
