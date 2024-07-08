package com.library.basic.usr.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.common.dto.Criteria;

public interface ReviewMapper {
	
	// 리뷰 목록
	List<ReviewVO> reviewList(@Param("book_bno") int book_bno, @Param("cri")Criteria cri);
	
	// 리뷰 개수
	int getCountReviewByBookBno(int book_bno);

}
