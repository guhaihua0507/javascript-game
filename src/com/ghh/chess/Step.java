package com.ghh.chess;

import com.ghh.common.game.Player;

/**
 * @author haihua.gu <br>
 * Create on May 10, 2010
 */

public class Step {

	private Player player;
	private int x;
	private int y;
	private int serialNo;
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
}

