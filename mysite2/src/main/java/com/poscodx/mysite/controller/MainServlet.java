package com.poscodx.mysite.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.action.main.MainAction;

public class MainServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected Action getAction(String actionName) {
		return new MainAction();
	}
  
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		s
//	}
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//		doGet(request, response);
//	}

}
