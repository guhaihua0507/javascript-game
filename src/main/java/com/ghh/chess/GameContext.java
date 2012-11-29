package com.ghh.chess;

import com.ghh.common.game.GameLobby;

/**
 * @author haihua.gu <br>
 * @Create on May 10, 2010
 */

public class GameContext {
	private final static GameContext	context	= new GameContext();

	private final int					size	= 10;
	private GameLobby					lobby;

	public static GameContext getContext() {
		return context;
	}

	private GameContext() {
		lobby = new GameLobby();
		for (int i = 0; i < size; i++) {
			lobby.addGame(new Gobang(new Long(i)));
		}
	}

	public GameLobby getLobby() {
		return lobby;
	}
}