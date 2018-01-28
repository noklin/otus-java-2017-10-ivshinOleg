package com.noklin.server.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter extends HttpFilter{

	@Override
	public void doFilter(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain) {
		Object isAuthorized = httpRequest.getSession().getAttribute(AUTHORIZED);
		try{
			if(isAuthorized == null){ 
				httpResponse.sendRedirect("/login");
			}else{
				chain.doFilter(httpRequest, httpResponse);
			}
		}catch(IOException | ServletException ex){
			httpResponse.setStatus(500);
			ex.printStackTrace();
		}
	}

	public static final String AUTHORIZED = "AUTHORIZED";
}