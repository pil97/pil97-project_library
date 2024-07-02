package com.library.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	
	// @ResponseBody	// 어노테이션이 사용이 안되면, return "index"는 데이터가 아니라 타임리프 파일명으로 인식
	@GetMapping("/")
	public String index() {
		log.info("기본 주소.");
		
		return "index";
	}
	
	@GetMapping("/test")
	public String test() {
		log.info("기본 주소.");
		
		return "test";
	}

}
