package com.poscodx.mysite.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.poscodx.mysite.service.SiteService;

public class SiteInterceptor implements HandlerInterceptor{
	private SiteService siteService;
	
	public SiteInterceptor(SiteService siteService) {
		this.siteService = siteService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

}
