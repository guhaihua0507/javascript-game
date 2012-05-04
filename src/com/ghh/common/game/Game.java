package com.ghh.common.game;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {
	public final static int STATUS_WAITING = 0;
	public final static int STATUS_PLAYING = 1;

	private Long gameId;
	protected int status = STATUS_WAITING;

	private List<Integer> authPool = new ArrayList<Integer>();
	private List<Player> players = new ArrayList<Player>();

	/**
	 * construction
	 * 
	 * @param id
	 *            - game id
	 */
	public Game(Long id) {
		this.gameId = id;
		int limit = getPlayerLimit();
		for (int i = 0; i < limit; i++) {
			authPool.add(i);
		}
	}

	public Long getId() {
		return gameId;
	}

	/**
	 * get player by playerId
	 * 
	 * @param id
	 * @return
	 */
	public Player getPlayer(Long id) {
		for (Player p : players) {
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * get all players
	 * 
	 * @return
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * size of game
	 * 
	 * @return
	 */
	protected abstract int getPlayerLimit();

	/**
	 * get game status
	 * 
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * player ready for game
	 * 
	 * @param id
	 */
	public void ready4Game(Long id) {
		Player player = getPlayer(id);
		if (player == null) {
			return;
		}
		player.setStatus(Player.PALYERSTATUS_READY);

		if (players.size() != getPlayerLimit()) {
			return;
		}
		for (Player p : players) {
			if (p.getStatus() != Player.PALYERSTATUS_READY) {
				return;
			}
		}
		startGame();
	}

	/**
	 * join game
	 * 
	 * @param player
	 * @return
	 */
	public boolean joinGame(Player player) {
		if (players.size() == getPlayerLimit()) {
			return false;
		}
		synchronized (this) {
			if (players.size() == getPlayerLimit()) {
				return false;
			}
			grantAuth(player);
			players.add(player);
		}
		return true;
	}

	/**
	 * leave game
	 * 
	 * @param playerId
	 */
	public synchronized void leaveGame(Long playerId) {
		Player player = getPlayer(playerId);
		if (player == null) {
			return;
		}
		players.remove(player);
		releaseAuth(player);
		resetGame();
	}

	private void grantAuth(Player player) {
		Integer auth = authPool.remove(0);
		player.setAuthId(auth);
	}

	private void releaseAuth(Player player) {
		Integer authId = player.getAuthId();
		if (authId == -1) {
			return;
		}
		authPool.add(authId);
	}

	protected void startGame() {
		status = STATUS_PLAYING;
	}

	protected void resetGame() {
		status = STATUS_WAITING;
	}
}
