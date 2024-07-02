package com.library.basic.admin.book;

import java.util.List;

import com.library.basic.common.dto.Criteria;

public interface AdminBookMapper {

	// 도서 등록 
	void bookRegister (BookVO vo);
	
	// 도서 리스트
	List<BookVO> bookList(Criteria cri);
	
	// 도서 리스트 개수
	int getTotalCount(Criteria cri);
	
	// 도서 수정 페이지 정보
	BookVO bookEditPage(Integer book_bno);
	
	// 도서 수정
	void bookEdit(BookVO vo);
	
	// 도서 삭제
	void bookDelete(Integer book_bno);
	
	// 체크상품 수정작업
	void checkedModify(List<BookDTO> bookModifyList);
}
