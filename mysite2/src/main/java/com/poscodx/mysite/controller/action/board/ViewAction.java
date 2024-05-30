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

public class ViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		
		BoardDao dao = new BoardDao();
		
		String sno = request.getParameter("no");
		Long no = Long.parseLong(sno);
		BoardVo vo = dao.findByNo(no);
		
		
		if(authUser == null || !authUser.getNo().equals(vo.getUserNo())) {
			dao.increaseHit(no);
		}
		
		vo.setContents(vo.getContents().replaceAll(">", "&gt;")
                 .replaceAll("<", "&lt;")
                 .replaceAll("\n", "<br/>"));
		
		request.setAttribute("vo", vo);
		request
			.getRequestDispatcher("/WEB-INF/views/board/view.jsp")
			.forward(request, response);
	}

}
