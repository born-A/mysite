package com.poscodx.mysite.controller.action.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poscodx.mysite.controller.ActionServlet.Action;
import com.poscodx.mysite.dao.BoardDao;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.PageVo;

public class SearchAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pageNum = 1;
		int amount = 5;
				

		if(request.getParameter("pageNum") != null) {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
				
		BoardDao dao = new BoardDao();
		
		String searchText = request.getParameter("kwd");
		List<BoardVo> list = new BoardDao().getSearch(searchText, pageNum, amount);
		int total = dao.getTotalSearchResults(searchText); 
		PageVo pageVo = new PageVo(pageNum, amount, total);
				

		request.setAttribute("pageVo", pageVo);
		
		for (BoardVo vo : list) {
		    vo.setContents(vo.getContents().replaceAll(">", "&gt;")
		                                  .replaceAll("<", "&lt;")
		                                  .replaceAll("\n", "<br/>"));
		}
		
		request.setAttribute("list", list);
		request.setAttribute("kwd", searchText);
		request
			.getRequestDispatcher("/WEB-INF/views/board/search_list.jsp")
			.forward(request, response);

	}

}
