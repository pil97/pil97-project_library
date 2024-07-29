package com.library.basic.admin.qna;

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
import com.library.basic.usr.qna.QnaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/qna/*")
@Controller
public class AdminQnaController {

	private final AdminQnaService adminQnaService;

	// 도서 이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;

	// 관리자 - QnA 목록 페이지
	@GetMapping("/qnalist")
	public void adminQnaList(Criteria cri, @ModelAttribute("startDate") String startDate,
			@ModelAttribute("endDate") String endDate, Model model) throws Exception {

		cri.setAmount(2);

		// 관리자 - QnA 목록
		List<MyQnaVO> adQnaList = adminQnaService.adminQnaList(cri, startDate, endDate);

		// log.info("목록 " + adQnaList);

		// 관리자 - QnA 개수
		int totalCount = adminQnaService.getTotalCountt(cri, startDate, endDate);

		model.addAttribute("adQnaList", adQnaList);
		model.addAttribute("pageMaker", new PageDTO(cri, totalCount));

	}

	// 도서 QnA 목록 - 이미지 보여주기 <img src="메핑주소">
	@GetMapping("/imagedisplay")
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception {

		// log.info("이미지 업로드 경로 : " + uploadPath);
		// log.info("이미지 업로드 경로 : " + dateFolderName);
		// log.info("이미지 업로드 경로 : " + fileName);

		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}

	// 관리자 - QnA 상세 정보
	@GetMapping("/qnainfo")
	public void qnaInfo(Long qna_code, Model model) throws Exception {

		// db 연동
		MyQnaVO vo = adminQnaService.adminQnaDetailInfo(qna_code);

		model.addAttribute("QnaVO", vo);
	}

	// 도서 QnA 답변 등록
	@GetMapping("/qnaanswersave")
	public ResponseEntity<String> qnaAnswerSave(String qna_code, String qna_answer) throws Exception {

		ResponseEntity<String> entity = null;

		// db연동
		adminQnaService.qnaAnswerSave(qna_code, qna_answer);

		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		return entity;
	}

	// 도서 QnA 삭제
	@GetMapping("/qnadelete")
	public ResponseEntity<String> qnaDelete(String qna_code) throws Exception {

		ResponseEntity<String> entity = null;

		// db연동
		adminQnaService.qnaDelete(qna_code);

		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		return entity;
	}
}
