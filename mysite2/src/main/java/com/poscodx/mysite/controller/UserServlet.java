package com.poscodx.mysite.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.dao.UserDao;
import com.poscodx.mysite.vo.UserVo;

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("a");
		
		if("joinform".equals(action)) {
			request
				.getRequestDispatcher("/WEB-INF/views/user/joinform.jsp")
				.forward(request, response);
		} else if ("join".equals(action)) {
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			UserVo vo = new UserVo();
			vo.setName(name);
			vo.setEmail(email);
			vo.setPassword(password);
			vo.setGender(gender);
			vo.setJoinDate(LocalDateTime.now().toString());

			System.out.println(vo);
			new UserDao().insert(vo);
			
			response.sendRedirect(request.getContextPath()+"/user?a=joinsuccess");
			
		} else if ("joinsuccess".equals(action)) {
			request
			.getRequestDispatcher("/WEB-INF/views/user/joinsuccess.jsp")
			.forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath());
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}