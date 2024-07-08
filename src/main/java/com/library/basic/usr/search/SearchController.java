package com.library.basic.usr.search;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.basic.admin.book.BookVO;
import com.library.basic.common.dto.Criteria;
import com.library.basic.common.dto.PageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/search/*")
@Controller
public class SearchController {

	private final SearchService searchService;
	
	// 자료검색 페이지
	@GetMapping("/searchlist")
	public void list(Criteria cri, Model model) {
		List<BookVO> list = searchService.searchPage(cri);
		
		// 1. 도서 목록
		model.addAttribute("list", list);
		
		int total = searchService.getTotalCount(cri);
		PageDTO pageDTO = new PageDTO(cri, total);
		
	
	}
}
