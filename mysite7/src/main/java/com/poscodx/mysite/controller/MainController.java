package com.poscodx.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poscodx.mysite.vo.UserVo;

@Controller
public class MainController {
	@RequestMapping({"/", "/main"})
	public String index() {
		return "main/index";
	}
	
	@ResponseBody
	@RequestMapping("/msg01")
	public String message01() {
		return "Hello World";
	}
	
	@ResponseBody
	@RequestMapping("/msg02")
	public String message02(String name) {
		return "안녕~ " + name;
	}

	@ResponseBody
	@RequestMapping("/msg03")
	public Object message03() {
		UserVo vo = new UserVo();
		vo.setNo(1L);
		vo.setName("둘리");
		vo.setEmail("dooly@gmail.com");
		
		return vo; 
	}
}