package com.ghh.common.game;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {
	public final static int		STATUS_WAITING	= 0;
	public final static int		STATUS_PLAYING	= 1;

	protected int				status			= STATUS_WAITING;
	protected Long				gameId;

	protected List<Integer>		playNoPool		= new ArrayList<Integer>();
	protected List<Player>		players			= new ArrayList<Player>();
	protected List<MessageBox>	message			= new ArrayList<MessageBox>();

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
			playNoPool.add(i);
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
		if (status == Game.STATUS_PLAYING) {
			for (Player p : players) {
				if (p.getStatus() != Player.PLAYERSTATUS_PLAYING) {
					continue;
				}
				MessageBox msg = new MessageBox();
				msg.setMessage("Player " + player.getName() + " left game.");
				msg.setPlayerId(p.getUserId());
				message.add(msg);
			}
		}
		players.remove(player);
		releasePlayerNo(player);
		resetGame();
	}

	public synchronized MessageBox fetchMessageBox(Long userId) {
		for (MessageBox msg : message) {
			if (msg.getPlayerId() == userId.longValue()) {
				message.remove(msg);
				return msg;
			}
		}
		return null;
	}
	
	private void grantAuth(Player player) {
		Integer auth = playNoPool.remove(0);
		player.setPlayNo(auth);
	}

	private void releasePlayerNo(Player player) {
		Integer playNo = player.getPlayNo();
		if (playNo == -1) {
			return;
		}
		playNoPool.add(playNo);
	}

	protected void startGame() {
		status = STATUS_PLAYING;
		message.clear();
		for (Player p : players) {
			p.setStatus(Player.PLAYERSTATUS_PLAYING);
		}
	}

	protected void resetGame() {
		status = STATUS_WAITING;
		for (Player p : players) {
			p.setStatus(Player.PLAYERSTATUS_WAITING);
		}
	}
}
