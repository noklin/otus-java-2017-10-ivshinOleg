package com.noklin.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.noklin.Application;
import com.noklin.cache.Cache;
import com.noklin.entity.UserDataSet;

public class CahceSettingsServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		writeCacheWidget(resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long maxSize = Long.parseLong(req.getParameter("maxSize"));
		long ttl = Long.parseLong(req.getParameter("ttl"));
		long maxIdle = Long.parseLong(req.getParameter("maxIdle"));
		Cache<Long, UserDataSet> cache = Application.INSTANCE.getCahce();
		cache.setMaxIdle(maxIdle);
		cache.setTimeToLive(ttl);
		cache.setMaxSize(maxSize);
		resp.sendRedirect("/");
	}
	
	private void writeCacheWidget(HttpServletResponse httpResponse) throws IOException{
		Cache<Long, UserDataSet> cache = Application.INSTANCE.getCahce();
		long maxSize = cache.getMaxSize();
		long ttl = cache.getTimeToLive();
		long maxIdle = cache.getMaxIdle();
		long hitCount = cache.getHitCount();
		long missCount = cache.getMissCount();
		long size = cache.getSize();
		httpResponse.getWriter().write(String.format(html,maxSize,ttl,maxIdle,size,hitCount,missCount));
	}
	
	private final String html = 
		"<div><form action=\"\\\" method=\"POST\">"+
			"<table border=\"1\">" + 
				"<caption>Cache info</caption>" + 
				"<tr>" + 
					"<th>Max size</th>" +
					"<th>Time to live</th>" + 
					"<th>Max idle</th>" +
					"<th>Current size</th>" +
					"<th>Hit count</th>" +
					"<th>Miss count</th>" +
				"</tr>" +
				"<tr>" +
					"<td><input name=\"maxSize\" value=\"%d\"></td>"+
					"<td><input name=\"ttl\" value=\"%d\"></td>"+
					"<td><input name=\"maxIdle\" value=\"%d\"></td>"+
					"<td>%d</td>"+
					"<td>%d</td>"+
					"<td>%d</td>" +
				"</tr>" + 
				"<tr><td><button type=\"submit\">save changes</button></td></tr>" + 
			"</table>"+
		"</form></div>";
}