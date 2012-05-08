package com.ghh.common.game;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {
	public final static int	STATUS_WAITING	= 0;
	public final static int	STATUS_PLAYING	= 1;

	protected int			status			= STATUS_WAITING;
	private Long			gameId;

	private List<Integer>	playerPool		= new ArrayList<Integer>();
	private List<Player>	players			= new ArrayList<Player>();

	/**
	 * construction
	 * 
	 * @param gameId
	 *            - game id
	 */
	public Game(Long gameId) {
		this.gameId = gameId;
		int limit = getPlayerLimit();
		for (int i = 0; i < limit; i++) {
			playerPool.add(i);
		}
	}

	public Long getId() {
		return gameId;
	}

	/**
	 * get player by playerId
	 * 
	 * @param userId
	 * @return
	 */
	public Player getPlayer(Long userId) {
		for (Player p : players) {
			if (p.getUserId().equals(userId)) {
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
	 * @param userId
	 */
	public void readyPlayer(Long userId) {
		Player player = getPlayer(userId);
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
		Integer auth = playerPool.remove(0);
		player.setPlayNo(auth);
	}

	private void releaseAuth(Player player) {
		Integer playNo = player.getPlayNo();
		if (playNo == -1) {
			return;
		}
		playerPool.add(playNo);
	}

	protected void startGame() {
		status = STATUS_PLAYING;
	}

	protected void resetGame() {
		status = STATUS_WAITING;
	}
}
