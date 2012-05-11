package com.ghh.common.game;
/**
 * @author hgu
 *
 */
public class MessageBox {
	private String message;
	private Long playerId;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}
}
