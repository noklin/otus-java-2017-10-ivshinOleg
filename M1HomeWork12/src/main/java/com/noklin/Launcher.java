package com.noklin;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.noklin.server.servlet.CahceSettingsServlet;
import com.noklin.server.servlet.LoginFilter;
import com.noklin.server.servlet.LoginServlet; 

public class Launcher{
	
	public static void main(String[] args) throws Exception{
		Server server = new Server(8080);
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextHandler.addServlet(LoginServlet.class, "/login");
		contextHandler.addServlet(CahceSettingsServlet.class, "/");
		contextHandler.addFilter(LoginFilter.class, "/", EnumSet.allOf(DispatcherType.class));
		server.setHandler(contextHandler);
		server.start();
		Application.INSTANCE.getCahce();
		server.join();
	}

} 