package com.library.basic.admin.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/admin/category/*")
@RequiredArgsConstructor
@Slf4j
@Controller
public class AdminCategoryController {
	
	private final AdminCategoryService adminCategoryservice;
		
	// 2차 카테고리 목록 
	@GetMapping("/secondcategory/{c_pcode}")
	public ResponseEntity<List<AdminCategoryVO>> getSecondCategoryList(@PathVariable("c_pcode") int c_pcode) throws Exception{
		
		log.info("1차 카테고리 코드 : " + c_pcode);
		
		ResponseEntity<List<AdminCategoryVO>> entity = null;
		
		entity = new ResponseEntity<List<AdminCategoryVO>>(adminCategoryservice.getSecondCategoryList(c_pcode), HttpStatus.OK);
		
		return entity;
	}
	


}
