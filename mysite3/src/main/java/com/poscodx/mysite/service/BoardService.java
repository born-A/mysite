package com.poscodx.mysite.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscodx.mysite.repository.BoardRepository;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.PageVo;

@Service
public class BoardService {
	@Autowired
	public BoardRepository boardRepository;
	
	public void addContents(BoardVo vo) {
		boardRepository.insert(vo);
	}
	
	public void addReplyContents(BoardVo vo, Long userNo, int groupNo, int orderNo, int depth) {
		boardRepository.updateOrderNo(groupNo, orderNo);
		boardRepository.insertReplyBoard(vo, groupNo, orderNo, depth);
	}
	
	public BoardVo getContets(Long no) {
		return boardRepository.findByNo(no);
	}
	
//	public BoardVo getContents(Long boardNo, Long userNo) {
//		
//	}
	
	public void updateContents(BoardVo vo, Long no, Long userNo) {
		vo.setNo(no);
		vo.setUserNo(userNo);
		boardRepository.modify(vo);
	}
	
	public void deleteContents(Long boardNo, Long userNo) {
		boardRepository.deleteByNoAndUserNo(boardNo, userNo);
	}
	
	public Map<String, Object> getContentsList(Integer pageNumber) {
		int pageNum = 1;
		int amount = 5;
				

		if(pageNumber != null) {
			pageNum = pageNumber;
		}
				
			
		List<BoardVo> list = boardRepository.getList(pageNum, amount);
		int total = boardRepository.getTotal(); 
		PageVo pageVo = new PageVo(pageNum, amount, total);
		
		for (BoardVo vo : list) {
		    vo.setContents(vo.getContents().replaceAll(">", "&gt;")
		                                  .replaceAll("<", "&lt;")
		                                  .replaceAll("\n", "<br/>"));
		}
		
		return Map.of("pageVo", pageVo, "list", list);
	}

	public BoardVo findContentsByNo(Long no) {
		return boardRepository.findByNo(no);
	}
}
