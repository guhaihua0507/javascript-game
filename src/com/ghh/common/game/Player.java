package com.ghh.common.game;

import com.ghh.common.basic.User;

public class Player {
	public static final int	PLAYERSTATUS_WAITING	= 0;
	public static final int	PALYERSTATUS_READY		= 1;
	public static final int	PLAYERSTATUS_PLAYING	= 2;

	private Long			userId;
	private String			name;

	private int				status					= PLAYERSTATUS_WAITING;
	private int				playNo					= -1;

	public Player() {
	}

	public Player(User u) {
		this.userId = u.getId();
		this.name = u.getName();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPlayNo() {
		return playNo;
	}

	public void setPlayNo(int playNo) {
		this.playNo = playNo;
	}
}
