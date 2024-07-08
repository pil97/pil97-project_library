package com.library.basic.usr.search;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.basic.admin.book.BookVO;
import com.library.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SearchService {

	private final SearchMapper searchMapper;
	
	// 자료검색 페이지
	public List<BookVO> searchPage(Criteria cri) {
		return searchMapper.searchPage(cri);
	};
	
	// 도서 개수
	public int getTotalCount(Criteria cri) {
		return searchMapper.getTotalCount(cri);
	};
	
}
