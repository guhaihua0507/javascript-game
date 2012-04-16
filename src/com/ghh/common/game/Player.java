package com.ghh.common.game;

import com.ghh.common.basic.User;

public class Player {
	public static final int PLAYERSTATUS_WAITING = 0;
	public static final int PALYERSTATUS_READY = 1;
	public static final int PLAYERSTATUS_PLAYING = 2;

	private Long id;
	private String name;
	
	private int status = PLAYERSTATUS_WAITING;
	private int authId = -1;
	
	public Player() {
	}
	
	public Player(User u) {
		this.id = u.getId();
		this.name = u.getName();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public int getAuthId() {
		return authId;
	}
	public void setAuthId(int authId) {
		this.authId = authId;
	}
}
