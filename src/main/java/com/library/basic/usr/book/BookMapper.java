package com.library.basic.usr.book;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.admin.book.BookVO;
import com.library.basic.common.dto.Criteria;

public interface BookMapper {
	
	// 책 리스트
	List<ReviewBookVO> bookList(@Param("c_code") int c_code, @Param("cri") Criteria cri);

	// 책 리스트 개수
	int getCountBookByCategory(int c_code);
	
	// 도서정보 - 모달창 및 도서 상세페이지
	BookVO bookInfo(int book_bno);
	
	// 도서 테이블 수량 감소
	void bookQuantityDecrease(@Param("book_bno") int book_bno, @Param("book_amount")int book_amount);
	
	// 도서 수량 확인
	int checkBookQuantity(int book_bno);
}
