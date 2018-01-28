package com.noklin.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		writeLoginWidget(resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().setAttribute(LoginFilter.AUTHORIZED, LoginFilter.AUTHORIZED);
		resp.sendRedirect("/");
	}
	
	private void writeLoginWidget(HttpServletResponse httpResponse) throws IOException{
		httpResponse.getWriter().write("<div><form action=\"login\" method=\"POST\">");
		httpResponse.getWriter().write("<input type=\"text\" name=\"login\" placeholder=\"enter login\">");
		httpResponse.getWriter().write("<input type=\"password\" name=\"password\" placeholder=\"enter password\">");
		httpResponse.getWriter().write("<button type=\"submit\">sign in</button>");
		httpResponse.getWriter().write("</form></div>");
	}
	
}