package com.library.basic.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/category/*")
@Controller
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping("catelist")
	public ResponseEntity<List<CategoryVO>> categoryList() throws Exception {

		ResponseEntity<List<CategoryVO>> entity = null;

		log.info("1차 카테고리코드: ");

		entity = new ResponseEntity<List<CategoryVO>>(categoryService.categoryAllList(), HttpStatus.OK);

		return entity;

	}

	@GetMapping("subcatelist")
	public ResponseEntity<List<CategoryVO>> subCateList(Integer c_pcode) throws Exception {

		ResponseEntity<List<CategoryVO>> entity = null;

		log.info("1차 카테고리코드: " + c_pcode);

		entity = new ResponseEntity<List<CategoryVO>>(categoryService.subCategoryList(c_pcode), HttpStatus.OK);

		return entity;
	}

}
