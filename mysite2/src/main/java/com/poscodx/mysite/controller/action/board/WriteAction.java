package com.poscodx.mysite.controller.action.board;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action; 
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.dao.BoardDao;

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String contents = request.getParameter("contents");
		Long userNo = Long.parseLong(request.getParameter("userNo"));
		
		BoardVo vo = new BoardVo();
		vo.setTitle(title);
		vo.setContents(contents);
		vo.setUserNo(userNo);
		String result = new BoardDao().insert(vo);
		
		response.sendRedirect(request.getContextPath() + "/board?writeSuccess=" + result);
	}

}
