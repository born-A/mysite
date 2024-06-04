package com.poscodx.mysite.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public String handler(Exception e) {
		//1. 로깅(logging)
		System.out.println(e);
		
		//2. 사과(종료)
		return "errors/exception";
	}
}