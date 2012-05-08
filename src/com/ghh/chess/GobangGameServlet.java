package com.ghh.chess;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ghh.chess.action.ActionFactory;

/**
 * Servlet implementation class GobangGameServlet
 */
public class GobangGameServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;

	public GobangGameServlet() {
		super();
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String protocol = request.getParameter("p");
			if (protocol == null || protocol.length() == 0) {
				return;
			}
			GameAction action = ActionFactory.getAction(protocol);
			action.setRequest(request);
			action.setResponse(response);
			action.action();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
}
