package com.ghh.chess.action;

import com.ghh.chess.GameAction;
import com.ghh.chess.Protocols;

/**
 * @author haihua.gu <br>
 * Create on May 10, 2010
 */

public class ActionFactory {
	private ActionFactory() {
		
	}
	
	public static GameAction getAction(String protocol) {
		if (Protocols.PROTOCOL_GAMESTATE.equals(protocol)) {
			return new GameStateAction();
		}
		if (Protocols.PROTOCOL_SYNCSTATE.equals(protocol)) {
			return new SyncStateAction();
		}
		if (Protocols.PROTOCOL_JOINGAME.equals(protocol)) {
			return new JoinGameAction();
		}
		if (Protocols.PROTOCOL_READYPLAY.equals(protocol)) {
			return new ReadyAction();
		}
		if (Protocols.PROTOCOL_PLAYASTEP.equals(protocol)) {
			return new PlayStepAction();
		}
		if (Protocols.PROTOCOL_LEAVEGAME.equals(protocol)) {
			return new LeaveGameAction();
		}
		return null;
	}
}

