package com.noklin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
	
	@Value("${auth.adm.login}")
	private String admLogin;
	@Value("${auth.adm.password}")
	private String admpassword;
	
	@Override
	public boolean authorize(String login, String password) {
		return login.equals(admLogin) && password.equals(admpassword);
	}

}