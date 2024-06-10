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
		boardRepository.updateGroupNo(vo.getNo().intValue());
	}
	
	public void addReplyContents(BoardVo vo, Long userNo, int groupNo, int orderNo, int depth) {
		boardRepository.updateOrderNo(groupNo, orderNo);
		vo.setGroupNo(groupNo);
		vo.setOrderNo(orderNo);
		vo.setDepth(depth);

		boardRepository.insertReplyBoard(vo);
		boardRepository.updateGroupNo(groupNo);
	}
	
	public BoardVo getContents(Long no) {
		BoardVo vo = boardRepository.findByNo(no);
		return vo;
	}
	
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
	
	public Map<String, Object> getSearchContentsList(Integer pageNumber, String kwd) {
		int pageNum = 1;
		int amount = 5;
				

		if(pageNumber != null) {
			pageNum = pageNumber;
		}
		
		List<BoardVo> list = boardRepository.getSearch(kwd, pageNum, amount);
		int total = boardRepository.getTotalSearchResults(kwd); 
		PageVo pageVo = new PageVo(pageNum, amount, total);
				
		for (BoardVo vo : list) {
		    vo.setContents(vo.getContents().replaceAll(">", "&gt;")
		                                  .replaceAll("<", "&lt;")
		                                  .replaceAll("\n", "<br/>"));
		}
		
		return Map.of("pageVo", pageVo, "list", list, "kwd", kwd);
	}
	
	public void increaseHit(Long no){
		boardRepository.increaseHit(no);
	}
 }
