package com.library.basic.usr.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;
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
	
	// 도서리뷰 저장
	public void reviewSave(ReviewVO vo) {
		reviewMapper.reviewSave(vo);
	};
	
	// 도서리뷰 삭제
	public void reviewDelete(Long rev_code) {
		reviewMapper.reviewDelete(rev_code);
	};
	
	// 도서리뷰 수정폼
	public ReviewVO reviewModifyForm(Long rev_code) {
		return reviewMapper.reviewModifyForm(rev_code);
	};
	
	// 도서리뷰 수정
	public void reviewModify(ReviewVO vo) {
		reviewMapper.reviewModify(vo);
	};
	
	// 나의 리뷰 목록
	public List<MyReviewVO> myReviewList(String usr_id, Criteria cri) {
		return reviewMapper.myReviewList(usr_id, cri);
	};

	// 나의 리뷰 목록 개수
	public int getTotalCount(String usr_id, Criteria cri) {
		return reviewMapper.getTotalCount(usr_id, cri);
	}
	
	// 도서리스트 - 리뷰 개수
	public int reviewCount(int book_bno) {
		return reviewMapper.reviewCount(book_bno);
	};
}
