package com.library.basic.admin.review;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.review.MyReviewVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminReviewService {

	private final AdminReviewMapper adminReviewMapper;
	
	// 관리자 - 리뷰 목록
	public List<MyReviewVO> adminReviewList(Criteria cri, String startDate, String endDate) {
		return adminReviewMapper.adminReviewList(cri, startDate, endDate);
	};
	
	//  관리자 - 리뷰 목록 개수
	public int adminReviewgetTotalCount(Criteria cri, String startDate, String endDate) {
		return adminReviewMapper.adminReviewgetTotalCount(cri, startDate, endDate);
	}
	
	// 관리자 - 리뷰 상세 정보
	public MyReviewVO adminReviewDetailInfo(Long rev_code) {
		return adminReviewMapper.adminReviewDetailInfo(rev_code);
	};
	
	// 괸리자 - 도서 리뷰 삭제
	public void reviewDelete(String rev_code) {
		adminReviewMapper.reviewDelete(rev_code);
	};
	
}
