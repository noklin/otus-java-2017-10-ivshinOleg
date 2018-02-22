package com.noklin;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.ScopedProxyMode;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class User {

	private boolean authorized;
	private String login;

	public boolean isAuthorized() {
		return authorized;
	}
	
	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}