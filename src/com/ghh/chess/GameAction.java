package com.ghh.chess;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author haihua.gu <br>
 * Create on May 10, 2010
 */

public abstract class GameAction {
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;

	public void setRequest(HttpServletRequest request) {
		this.request = request;
		session = request.getSession();
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
	
	protected Object getAttribute(String name) {
		return request.getAttribute(name);
	}
	
	protected void writeLine(String s) throws IOException {
		response.getWriter().println(s);
	}
	
	public abstract void action() throws Exception;
}

