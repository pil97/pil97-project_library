package com.library.basic.usr.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.common.dto.Criteria;

public interface ReviewMapper {
	
	// 리뷰 목록
	List<ReviewVO> reviewList(@Param("book_bno") int book_bno, @Param("cri")Criteria cri);
	
	// 리뷰 개수
	int getCountReviewByBookBno(int book_bno);
	
	// 도서리뷰 저장
	void reviewSave(ReviewVO vo);
	
	// 도서리뷰 삭제
	void reviewDelete(Long rev_code);
	
	// 도서리뷰 수정폼
	ReviewVO reviewModifyForm(Long rev_code);
	
	// 도서리뷰 수정
	void reviewModify(ReviewVO vo);

}
