package com.library.basic.admin.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.basic.common.dto.Criteria;
import com.library.basic.common.dto.PageDTO;
import com.library.basic.common.util.FileManagerUtils;
import com.library.basic.usr.qna.MyQnaVO;
import com.library.basic.usr.review.MyReviewVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/review/*")
@Controller
public class AdminReviewController {

	// 도서 이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;

	private final AdminReviewService adminReviewService;

	// 관리자 - QnA 목록 페이지
	@GetMapping("/reviewlist")
	public void adminReviewList(Criteria cri, @ModelAttribute("startDate") String startDate,
			@ModelAttribute("endDate") String endDate, Model model) throws Exception {

		cri.setAmount(2);

		// 관리자 - 리뷰 목록
		List<MyReviewVO> adReviewList = adminReviewService.adminReviewList(cri, startDate, endDate);

		// log.info("목록 " + adReviewList);

		// 관리자 - 리뷰 개수
		int totalCount = adminReviewService.adminReviewgetTotalCount(cri, startDate, endDate);

		model.addAttribute("adReviewList", adReviewList);
		model.addAttribute("pageMaker", new PageDTO(cri, totalCount));

	}

	// 도서 리뷰 목록 - 이미지 보여주기 <img src="메핑주소">
	@GetMapping("/imagedisplay")
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception {

		// log.info("이미지 업로드 경로 : " + uploadPath);
		// log.info("이미지 업로드 경로 : " + dateFolderName);
		// log.info("이미지 업로드 경로 : " + fileName);

		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}

	// 관리자 - 리뷰 상세 정보
	@GetMapping("/reviewinfo")
	public void reviewInfo(Long rev_code, Model model) throws Exception {

		// db 연동
		MyReviewVO vo = adminReviewService.adminReviewDetailInfo(rev_code);

		model.addAttribute("ReviewVO", vo);
	}

	// 괸리자 - 도서 리뷰 삭제
	@GetMapping("/reviewdelete")
	public ResponseEntity<String> reviewDelete(String rev_code) throws Exception {

		ResponseEntity<String> entity = null;

		// db연동
		adminReviewService.reviewDelete(rev_code);

		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		return entity;
	}

}
