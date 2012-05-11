package com.ghh.chess;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ghh.common.basic.User;
import com.ghh.common.game.GameLobby;

/**
 * @author haihua.gu
 * @Create on May 10, 2010
 */

public abstract class GameAction {
	protected HttpServletRequest	request;
	protected HttpServletResponse	response;
	protected HttpSession			session;

	public void setRequest(HttpServletRequest request) {
		this.request = request;
		this.session = request.getSession();
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	protected void setAttribute(String name, String value) {
		request.setAttribute(name, value);
	}

	protected String getParameter(String name) {
		return request.getParameter(name);
	}

	protected Long getLongParameter(String name) {
		String val = getParameter(name);
		if (val == null || val.trim().isEmpty()) {
			return null;
		} else {
			return Long.valueOf(val);
		}
	}

	protected Integer getIntParameter(String name) {
		String val = getParameter(name);
		if (val == null || val.trim().isEmpty()) {
			return null;
		} else {
			return Integer.valueOf(val);
		}
	}

	protected User getUser() {
		User user = (User) request.getSession().getAttribute("user");
		return user;
	}
	
	protected Object getAttribute(String name) {
		return request.getAttribute(name);
	}

	protected void sendMessage(String s) throws IOException {
		response.getWriter().println(s);
	}

	protected Gobang getGame(Long gameId) {
		GameLobby lobby = GameContext.getContext().getLobby();
		Gobang game = (Gobang) lobby.getGame(gameId);
		return game;
	}

	public abstract void action() throws Exception;
}
