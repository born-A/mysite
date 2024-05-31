package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.UserVo;

public class ModifyFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		// Access Control
		if(session == null) {
			response.sendRedirect(request.getContextPath());
			return;
		}
		
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		Long no = Long.parseLong(request.getParameter("no"));
		
		BoardVo vo = new BoardDao().findByNo(no);
		
		if(authUser == null || !authUser.getNo().equals(vo.getUserNo())) {
			response.sendRedirect(request.getContextPath() + "/board?modifySuccess=failed");
			return;
		}
		
		request.setAttribute("vo", vo);
		request.setAttribute("no", no);
		request.setAttribute("userNo", authUser.getNo());
		request
			.getRequestDispatcher("/WEB-INF/views/board/modify.jsp")
			.forward(request, response);
	}

}
