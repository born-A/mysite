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

public class ReplyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		// Access Control
		if(session == null) {
			response.sendRedirect(request.getContextPath());
			return;
		}
		
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if(authUser == null) {
			response.sendRedirect(request.getContextPath() + "/board");
			return;
		}
		
        String title =request.getParameter("title");
        String contents = request.getParameter("contents");
        int groupNo = Integer.parseInt(request.getParameter("groupNo"));
        int depth = Integer.parseInt(request.getParameter("depth"));
        int orderNo = Integer.parseInt(request.getParameter("orderNo"));
        Long userNo = Long.parseLong(request.getParameter("userNo"));

        BoardDao dao = new BoardDao();
        dao.updateOrderNo(groupNo, orderNo);
        
        BoardVo vo = new BoardVo();
        vo.setContents(contents);
        vo.setTitle(title);
        vo.setGroupNo(groupNo);
        vo.setDepth(depth);
        vo.setOrderNo(orderNo);
        vo.setUserNo(userNo);

        dao.updateOrderNo(groupNo, orderNo);
        dao.insertReplyBoard(vo, groupNo, orderNo, depth);
        
        response.sendRedirect(request.getContextPath() + "/board");
	}

}
