package com.ghh.common.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ghh.common.basic.User;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sId = request.getParameter("userId");
		String name = request.getParameter("name");

		Long id = Long.valueOf(sId);
		User user = new User();
		user.setId(id);
		user.setName(name);

		request.getSession().setAttribute("user", user);
		response.sendRedirect("gamelist.html");
	}
}
