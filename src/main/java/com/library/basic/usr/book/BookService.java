package com.library.basic.usr.book;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.library.basic.admin.book.BookVO;
import com.library.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookService {

	private final BookMapper bookMapper;
	
	// 책 리스트
	public List<ReviewBookVO> bookList(int c_code, Criteria cri) {
		return bookMapper.bookList(c_code, cri);
	};

	// 책 리스트 개수
	public int getCountBookByCategory(int c_code) {
		return bookMapper.getCountBookByCategory(c_code);
	};
	
	// 도서정보 - 모달창 및 도서 상세페이지
	public BookVO bookInfo(int book_bno) {
		return bookMapper.bookInfo(book_bno);				
	};
	
	// 도서 수량 확인
	public int checkBookQuantity(int book_bno) {
		return bookMapper.checkBookQuantity(book_bno);
	}; 
	
}
