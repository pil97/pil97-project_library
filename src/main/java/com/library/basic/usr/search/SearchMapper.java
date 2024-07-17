package com.library.basic.usr.search;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.admin.book.BookVO;
import com.library.basic.common.dto.Criteria;

public interface SearchMapper {
	
	// 자료검색 페이지
	List<BookVO> searchList(Criteria cri);
	
	// 도서 개수
	int getTotalCount(@Param("cri") Criteria cri);

	// 메인페이지 최신 이미지 업로드 
	List<BookVO> imageList();
	
}
