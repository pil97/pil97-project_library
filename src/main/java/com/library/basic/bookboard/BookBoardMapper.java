package com.library.basic.bookboard;

import java.util.List;

public interface BookBoardMapper {

	// 도서 등록
	void write(BookBoardVO vo);

	// 도서 목록 조회
	List<BookBoardVO> listWithPaging(Criteria cri);

	// 총 데이타개수
	int getTotalCount(Criteria cri);

	// 도서 조회
	BookBoardVO get(Long b_bno);

	// 도서 수정
	void modify(BookBoardVO vo);
}
