package com.library.basic.admin.book;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.library.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminBookService {

	private final AdminBookMapper adminBookMapper;
	
	// 도서 등록 
	public void bookRegister (BookVO vo) {
		adminBookMapper.bookRegister(vo);
	};
	
	// 도서 리스트
	public List<BookVO> booktList(Criteria cri) {
		return adminBookMapper.bookList(cri);
	};
	
	// 도서 리스트 개수
	public int getTotalCount(Criteria cri) {
		return adminBookMapper.getTotalCount(cri);
	};
	
	// 도서 수정 페이지 정보
	public BookVO bookEditPage(Integer book_bno) {
		return adminBookMapper.bookEditPage(book_bno);
	};
	
	// 도서 수정
	public void bookEdit(BookVO vo) {
		adminBookMapper.bookEdit(vo);
	};
	
	// 도서 삭제
	public void bookDelete(Integer book_bno) {
		adminBookMapper.bookDelete(book_bno);
	};
	
	// 체크상품 수정작업
	public void checkedModify(
			List<Integer> book_bno_arr, 
			List<String> book_author_arr, 
			List<Integer> book_deposit_arr, 
			List<String> book_publisher_arr,
			List<String> book_loan_arr) {
		
		List<BookDTO> bookModifyList = new ArrayList<>();
		
		for(int i = 0; i < book_bno_arr.size(); i++) {
			BookDTO bookDTO = new BookDTO(
					book_bno_arr.get(i),
					book_author_arr.get(i),					
					book_deposit_arr.get(i),
					book_publisher_arr.get(i),
					book_loan_arr.get(i)
				);
			bookModifyList.add(bookDTO);
		}
		
		adminBookMapper.checkedModify(bookModifyList);
	};
	
}
