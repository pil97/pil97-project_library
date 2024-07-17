package com.library.basic.usr.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.basic.common.dto.Criteria;
import com.library.basic.common.dto.PageDTO;
import com.library.basic.usr.UserVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/review/*")
@RestController
public class ReviewController {

	private final ReviewService reviewService;

	// 리뷰목록
	@GetMapping("/reviewlist/{book_bno}/{page}")
	public ResponseEntity<Map<String, Object>> reviewList(@PathVariable("book_bno") int book_bno,
			@PathVariable("page") int page) throws Exception {
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> map = new HashMap<>();

		// 1. 후기목록
		Criteria cri = new Criteria();
		cri.setPageNum(page);
		cri.setAmount(10);

		List<ReviewVO> reviewList = reviewService.reviewList(book_bno, cri);

		// 2. 페이징 정보
		int reviewCount = reviewService.getCountReviewByBookBno(book_bno);
		PageDTO pageMaker = new PageDTO(cri, reviewCount);

		map.put("reviewList", reviewList);
		map.put("pageMaker", pageMaker);

		entity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

		return entity;
	}

	// 도서리뷰 작성
	@PostMapping(value = "/reviewsave", consumes = { "application/json" }, produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> reviewSave(@RequestBody ReviewVO vo, HttpSession session) throws Exception {

		ResponseEntity<String> entity = null;
		
		// 로그인 세션 확인
		UserVO loggedInUser = (UserVO) session.getAttribute("loginStatus");
		if (loggedInUser == null || loggedInUser.getUsr_id() == null) {
			entity = new ResponseEntity<String>("fail", HttpStatus.OK);
			return entity;
		}

		// 로그인 확인 후,
		String usr_id = loggedInUser.getUsr_id();
		vo.setUsr_id(usr_id);

		log.info("도서리뷰 데이터 : " + vo);

		// db연동 
		reviewService.reviewSave(vo);

		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		return entity;
	}

	// 도서리뷰 삭제
	@DeleteMapping("/reviewdelete/{rev_code}")
	public ResponseEntity<String> reviewDelete(@PathVariable("rev_code") Long rev_code) throws Exception {
		ResponseEntity<String> entity = null;

		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		reviewService.reviewDelete(rev_code);

		return entity;
	}
	
	// 도서리뷰 수정폼
	@GetMapping("/reviewmodifyform/{rev_code}")
	public ResponseEntity<ReviewVO> reviewModifyForm(@PathVariable("rev_code") Long rev_code) throws Exception {
		ResponseEntity<ReviewVO> entity = null;
	
		ReviewVO vo = reviewService.reviewModifyForm(rev_code);
		
		entity = new ResponseEntity<ReviewVO>(vo, HttpStatus.OK);

		return entity;
	}
	
	// 도서리뷰 수정
	@PutMapping("/reviewmodify")
	public ResponseEntity<String> reviewModify(@RequestBody ReviewVO vo) throws Exception {
		
		log.info("수정정보" + vo);
		
		ResponseEntity<String> entity = null;
		
		reviewService.reviewModify(vo);
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		return entity;
	}

}
