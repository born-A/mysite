package com.poscodx.mysite.controller.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.UserDao;
import com.poscodx.mysite.vo.UserVo;

public class WriteFormAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		// Access Control
		if(session == null) {
			response.sendRedirect(request.getContextPath()+ "/board?writeSuccess=failed");
			return;
		}
		
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if(authUser == null) {
			response.sendRedirect(request.getContextPath()+  "/board?writeSuccess=failed");
			return;
		}
		
		request
			.getRequestDispatcher("/WEB-INF/views/board/write.jsp?userNo="+authUser.getNo())
			.forward(request, response);

	}

}
