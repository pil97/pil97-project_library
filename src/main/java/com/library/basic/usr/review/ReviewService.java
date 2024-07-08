package com.library.basic.usr.review;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewService {

	private final ReviewMapper reviewMapper;
	
	// 리뷰 목록
	public List<ReviewVO> reviewList(int book_bno, Criteria cri) {
		return reviewMapper.reviewList(book_bno, cri);
	};
	
	// 리뷰 개수
	public int getCountReviewByBookBno(int book_bno) {
		return reviewMapper.getCountReviewByBookBno(book_bno);
	};
	
}
