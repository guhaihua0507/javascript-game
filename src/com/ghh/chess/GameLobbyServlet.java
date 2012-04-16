package com.ghh.chess;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ghh.common.game.GameLobby;

/**
 * Servlet implementation class GameLobbyServlet
 */
public class GameLobbyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		GameLobby lobby = GameContext.getContext().getLobby();
		request.setAttribute("gamelist", lobby.getGames());
		request.getRequestDispatcher("/game/lobby.jsp").forward(request, response);
	}
}
