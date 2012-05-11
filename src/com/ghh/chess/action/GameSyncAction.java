package com.ghh.chess.action;

import org.json.JSONObject;

import com.ghh.chess.GameAction;
import com.ghh.chess.Gobang;
import com.ghh.chess.Protocols;
import com.ghh.chess.Step;
import com.ghh.common.game.Game;
import com.ghh.common.game.MessageBox;
import com.ghh.common.game.Player;

/**
 * @author hgu
 * 
 */
public class GameSyncAction extends GameAction {
	@Override
	public void action() throws Exception {
		Long gameId = getLongParameter("gameId");
		Integer playerStatus = getIntParameter("status");
		Integer stepNo = getIntParameter("step");
		
		Long userId = getUser().getId();
		
		Gobang game = getGame(gameId);
		if (game == null) {
			sendMessage("game not exist");
			return;
		}
		
		if (game.getStatus() == Game.STATUS_PLAYING) {
			/*is sync*/
			if (stepNo == game.getStepNo()) {
				return;
			}
			/*sync the last step*/
			if (stepNo == game.getStepNo() - 1) {
				Step step = game.getLastStep();
	            JSONObject data = new JSONObject();
	            data.put("protocol", Protocols.ClIENT_PROTOCOL_SYNC);
	            data.put("playNo", step.getPlayer().getPlayNo());
	            data.put("x", step.getX());
	            data.put("y", step.getY());
	            data.put("stepNo", step.getStepNo());
	            sendMessage(data.toString());
			}
		} else {
			if (playerStatus == Player.PLAYERSTATUS_PLAYING) {
				JSONObject data = new JSONObject();
                data.put("protocol", Protocols.CLIENT_PROTOCOL_GAMEOVER);
                data.put("winner", game.getWinner());
                
                MessageBox msg = game.fetchMessageBox(userId);
                if (msg != null) {
                	data.put("msg", msg.getMessage());
                } else {
                	Player player = game.getPlayer(userId);
                	if (player.getPlayNo() == game.getWinner()) {
                		data.put("msg", "You win!");
                	} else {
                		data.put("msg", "You lost!");
                	}
                }
                sendMessage(data.toString());
			}
		}
	}
}
