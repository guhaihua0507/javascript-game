package com.ghh.common.game;

import java.util.ArrayList;
import java.util.List;

public class GameLobby {
	private List<Game> games = new ArrayList<Game>();
	
	/**
	 * add game list to lobby
	 * @param games
	 */
	public void addGame(List<Game> games) {
		games.addAll(games);
	}
	
	/**
	 * add game to lobby
	 * @param game
	 */
	public void addGame(Game game) {
		games.add(game);
	}
	
	public List<Game> getGames() {
		return games;
	}
	
	/**
	 * get game by game id
	 * @param id
	 * @return
	 */
	public Game getGame(Long id) {
		if (games == null) {
			return null;
		}
		for (Game g : games) {
			if (id.equals(g.getId())) {
				return g;
			}
		}
		return null;
	}
}
