package com.library.basic.bookboard;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookBoardService {
	
	private final BookBoardMapper bookBoardMapper;

	// 도서 등록
	public void write(BookBoardVO vo) {
		bookBoardMapper.write(vo);
	};
	
	// 도서 목록 조회
	public List<BookBoardVO> listWithPaging(Criteria cri) {
		return bookBoardMapper.listWithPaging(cri);
	};
	
	// 총 데이타개수
	public int getTotalCount(Criteria cri) {
		return bookBoardMapper.getTotalCount(cri);
	};
	
	// 도서 조회
	public BookBoardVO get(Long b_bno) {
		return bookBoardMapper.get(b_bno);
	};

	// 도서 수정
	public void modify(BookBoardVO vo) {
		bookBoardMapper.modify(vo);		
	};
	
}
