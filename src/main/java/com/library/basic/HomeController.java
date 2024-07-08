package com.library.basic;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.library.basic.admin.category.AdminCategoryService;
import com.library.basic.admin.category.AdminCategoryVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {
	
	private final AdminCategoryService adminCategoryService;
	
	// @ResponseBody	// 어노테이션이 사용이 안되면, return "index"는 데이터가 아니라 타임리프 파일명으로 인식
	@GetMapping("/")
	public String index(Model model) {
		
		List<AdminCategoryVO> cateList = adminCategoryService.getFirstCategoryList();

		model.addAttribute("userCateList", cateList);
		
		log.info("기본 주소.");
		
		return "index";
	}
	
	

}
