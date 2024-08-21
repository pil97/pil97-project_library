package com.library.basic.admin.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.review.MyReviewVO;

public interface AdminReviewMapper {
	
	// 관리자 - 리뷰 목록
	List<MyReviewVO> adminReviewList(@Param("cri") Criteria cri, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	//  관리자 - 리뷰 목록 개수
	int adminReviewgetTotalCount(@Param("cri") Criteria cri, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	// 관리자 - 리뷰 상세 정보
	MyReviewVO adminReviewDetailInfo(Long rev_code);
	
	// 괸리자 - 도서 리뷰 삭제
	void reviewDelete(String rev_code);
	
	

}
