package com.library.basic.usr.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.qna.MyQnaVO;

public interface ReviewMapper {

	// 리뷰 목록
	List<ReviewVO> reviewList(@Param("book_bno") int book_bno, @Param("cri") Criteria cri);

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

	// 나의 리뷰 목록
	List<MyReviewVO> myReviewList(@Param("usr_id") String usr_id, @Param("cri") Criteria cri);

	// 나의 리뷰 목록 개수
	int getTotalCount(@Param("usr_id") String usr_id, @Param("cri") Criteria cri);

}
