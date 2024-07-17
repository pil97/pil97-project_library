package com.library.basic.usr.search;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	@GetMapping("/searchpage")
	public void searchPage() {
	}

	// 자료검색
	@GetMapping("/searchlist/{keyword}/{page}")
	public ResponseEntity<Map<String, Object>> searchList(@PathVariable("keyword") String keyword, @PathVariable("page") int page) throws Exception {
		
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> map = new HashMap<>();
		
		// 1. 검색 목록
		Criteria cri = new Criteria();
		cri.setKeyword(keyword);
		cri.setPageNum(page);
		
		List<BookVO> searchList = searchService.searchList(cri);
		
		   if (searchList == null || searchList.isEmpty()) {
		        // Handle case where no search results were found
		        map.put("searchList", Collections.emptyList()); // Provide an empty list
		    } else {
		        map.put("searchList", searchList);
		    }
		
		// 2. 페이징 정보
		int searchCount = searchService.getTotalCount(cri);
		PageDTO pageMaker = new PageDTO(cri, searchCount);
		
		map.put("searchList", searchList);
		map.put("pageMaker", pageMaker);
		
		
		
		entity = new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		
		return entity;		
	}
}
