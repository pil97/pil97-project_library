package com.library.basic.usr.book;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.admin.book.BookVO;
import com.library.basic.common.dto.Criteria;

public interface BookMapper {
	
	// 책 리스트
	List<BookVO> bookList(@Param("c_code") int c_code, @Param("cri") Criteria cri);

	// 책 리스트 개수
	int getCountBookByCategory(int c_code);
	
	// 도서정보 - 모달창 및 도서 상세페이지
	BookVO bookInfo(int book_bno);
}
