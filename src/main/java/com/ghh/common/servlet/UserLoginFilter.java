package com.ghh.common.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author hgu
 *
 */
public class UserLoginFilter implements Filter {
	private String contextPath = null;
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletrequest;
		HttpSession session = request.getSession();
		if (session.getAttribute("user") == null) {
			HttpServletResponse response = (HttpServletResponse) servletresponse;
			response.sendRedirect(contextPath + "/index.jsp");
		} else {
			filterchain.doFilter(servletrequest, servletresponse);
		}
	}

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {
		ServletContext ctx = filterconfig.getServletContext();
		contextPath = ctx.getContextPath();
		if (contextPath == null || contextPath.isEmpty()) {
			contextPath = "";
		}
	}
}
