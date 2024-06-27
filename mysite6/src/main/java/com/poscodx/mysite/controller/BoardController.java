package com.poscodx.mysite.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
	public String add(Authentication authentication, Model model) {
		UserVo authUser = (UserVo)authentication.getPrincipal();
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
			Authentication authentication,
			@RequestParam(value="groupNo", required=true, defaultValue="") int groupNo, 
			@RequestParam(value="orderNo", required=true, defaultValue="") int orderNo,
			@RequestParam(value="depth", required=true, defaultValue="") int depth,
			Model model
		) {
		UserVo authUser = (UserVo)authentication.getPrincipal();
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
		boardService.addReplyContents(vo, userNo, groupNo, orderNo, depth);
		return "redirect:/board";
	}
	
	@RequestMapping("/view/{no}")
	public String view(@PathVariable("no") Long no, Model model) {
		BoardVo vo = boardService.getContents(no);
		boardService.increaseHit(no);
		model.addAttribute("vo", vo);
		return "board/view";
	}
	
	@RequestMapping(value="/modify/{no}", method=RequestMethod.GET)
	public String modify(Authentication authentication, @PathVariable("no") Long no, Model model) {	
		UserVo authUser = (UserVo)authentication.getPrincipal();
		BoardVo vo = boardService.getContents(no);
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
	public String delete(Authentication authentication, @PathVariable("no") Long no) {
		UserVo authUser = (UserVo)authentication.getPrincipal();
		boardService.deleteContents(no, authUser.getNo());
		return "redirect:/board";
	}
	
	@RequestMapping("/search")
	public String search(
			Model model, 
			@RequestParam(value="pageNum", required=true, defaultValue="") Integer pageNum,
			@RequestParam(value="kwd", required=true, defaultValue="") String kwd
	) {
		Map<String, Object> map = boardService.getSearchContentsList(pageNum, kwd);
		model.addAttribute("list", map.get("list"));
		model.addAttribute("pageVo", map.get("pageVo"));
		model.addAttribute("kwd", map.get("kwd"));
		model.addAttribute("pageNum", pageNum);
		return "board/search_list";
	}
}
