package com.poscodx.mysite.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.poscodx.mysite.service.BoardService;
import com.poscodx.mysite.vo.BoardVo;
import com.poscodx.mysite.vo.UserVo;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@RequestMapping("")
	public String index(
			Model model, 
			@RequestParam(value="pageNum", required=true, defaultValue="") Integer pageNum
			) {
		Map<String, Object> map = boardService.getContentsList(pageNum);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("pageVo", map.get("pageVo"));
		return "board/list";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String add(HttpSession session, Model model) {
		// Access Control
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/board";
		}
		model.addAttribute("userNo", authUser.getNo());
		return "board/write";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String add(BoardVo vo) {
		boardService.addContents(vo);
		return "redirect:/board";
	}
	
	@RequestMapping(value="/reply",method=RequestMethod.GET)
	public String reply(
			HttpSession session,
			@RequestParam(value="groupNo", required=true, defaultValue="") int groupNo, 
			@RequestParam(value="orderNo", required=true, defaultValue="") int orderNo,
			@RequestParam(value="depth", required=true, defaultValue="") int depth,
			Model model
		) {
		// Access Control
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/board";
		}
		
		model.addAttribute("map", Map.of("userNo",authUser.getNo(),"groupNo", groupNo, "orderNo", orderNo, "depth", depth));
		return "board/reply_write"; 
	}
	
	@RequestMapping(value="/reply",method=RequestMethod.POST)
	public String reply(
			BoardVo vo,
			@RequestParam(value="userNo", required=true, defaultValue="") long userNo, 
			@RequestParam(value="groupNo", required=true, defaultValue="") int groupNo, 
			@RequestParam(value="orderNo", required=true, defaultValue="") int orderNo,
			@RequestParam(value="depth", required=true, defaultValue="") int depth
		) {
		boardService.addReplyContents(vo, userNo, groupNo, groupNo, depth);
		return "redirect:/board";
	}
	
	@RequestMapping("/view/{no}")
	public String view(@PathVariable("no") Long no, Model model) {
		BoardVo vo = boardService.findContentsByNo(no);
		model.addAttribute("vo", vo);
		return "board/view";
	}
	
	@RequestMapping(value="/modify/{no}", method=RequestMethod.GET)
	public String modify(HttpSession session, @PathVariable("no") Long no, Model model) {
		// Access Control
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/board";
		}
				
		BoardVo vo = boardService.findContentsByNo(no);
		if(authUser == null || !authUser.getNo().equals(vo.getUserNo())) {
			return "redirect:/board";
		}
		
		model.addAttribute("no", no);
		model.addAttribute("userNo", authUser.getNo());
		model.addAttribute("vo", vo);
		return "board/modify";
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(
			BoardVo vo,
			@RequestParam(value="no", required=true, defaultValue="") Long no,
			@RequestParam(value="userNo", required=true, defaultValue="") Long userNo
			) {
		boardService.updateContents(vo, no, userNo);
		return "redirect:/board/view/"+no;
	}
	
	@RequestMapping("/delete/{no}")
	public String delete(HttpSession session, @PathVariable("no") Long no) {
		// Access Control
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		if(authUser == null) {
			return "redirect:/board";
		}
		
		BoardVo vo = boardService.findContentsByNo(no);
		if(authUser == null || !authUser.getNo().equals(vo.getUserNo())) {
			return "redirect:/board";
		}
		
		boardService.deleteContents(no, authUser.getNo());
		return "redirect:/board";
	}
}