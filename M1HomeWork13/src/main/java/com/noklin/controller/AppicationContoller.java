package com.noklin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.noklin.AuthService;
import com.noklin.User;
import com.noklin.cache.Cache;
import com.noklin.entity.UserDataSet;

@Controller
public class AppicationContoller {
	
	@Autowired
	private Cache<String,UserDataSet> cache;
	@Autowired
	private AuthService authService;
	@Autowired
	private User user;
	
	@GetMapping("/")
	public String  index(Model model) {
		if(!user.isAuthorized()) {
			return "login";
		}
		model.addAttribute("ttl", cache.getTimeToLive());
		model.addAttribute("maxSize", cache.getMaxSize());
		model.addAttribute("maxIdle", cache.getMaxIdle());
		model.addAttribute("size", cache.getSize());
		model.addAttribute("ttl", cache.getTimeToLive());
		model.addAttribute("hitCount", cache.getHitCount());
		model.addAttribute("missCount", cache.getMissCount());
		return "index";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	
	@PostMapping("/auth")
	public RedirectView auth(Model model, 
			@RequestParam String login, 
			@RequestParam String password) {
		boolean authorized = authService.authorize(login, password);
		user.setAuthorized(authorized);
		return new RedirectView(authorized ? "/" : "/login", true);
	}
	
	@PostMapping("/cache")
	public RedirectView auth(Model model, 
			@RequestParam Long ttl, 
			@RequestParam Long maxSize, 
			@RequestParam Long maxIdle) {
		
		cache.setMaxIdle(maxIdle);
		cache.setTimeToLive(ttl);
		cache.setMaxSize(maxSize);
		return new RedirectView("/", true);
	}
}
