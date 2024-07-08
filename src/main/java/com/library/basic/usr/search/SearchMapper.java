package com.library.basic.usr.search;

import java.util.List;

import com.library.basic.admin.book.BookVO;
import com.library.basic.common.dto.Criteria;

public interface SearchMapper {
	
	// 자료검색 페이지
	List<BookVO> searchPage(Criteria cri);
	
	// 도서 개수
	int getTotalCount(Criteria cri);

}
