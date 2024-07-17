package com.library.basic.usr.qna;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.basic.common.dto.Criteria;
import com.library.basic.common.dto.PageDTO;
import com.library.basic.usr.UserVO;
import com.library.basic.usr.review.ReviewVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/qna/*")
@Controller
public class QnaController {
	
	private final QnaService qnaService;
	private final PasswordEncoder passwordEncoder;
	
	// QnA 작성
	@PostMapping(value = "/qnawrite", consumes = { "application/json" }, produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> qnaWrite(@RequestBody QnaVO vo, HttpSession session) throws Exception {
		
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
		vo.setQna_password(passwordEncoder.encode(vo.getQna_password()));
		
		log.info("도서 QnA 데이터 : " + vo);
		
		qnaService.qnaWrite(vo);
		
		
		entity = new ResponseEntity<String>("success", HttpStatus.OK);
		
		return entity;
	}		
	
	// QnA 목록
	@GetMapping("/qnalist/{book_bno}/{page}")
	public ResponseEntity<Map<String, Object>> qnaList(@PathVariable("book_bno") int book_bno,
			@PathVariable("page") int page) throws Exception {
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> map = new HashMap<>();
		
		log.info("QnA 리스트 기능 시작");
		log.info("번호" + book_bno);
		log.info("패이지 " + page);
		
		// 1. QnA 목록
		Criteria cri = new Criteria();
		cri.setPageNum(page);
		cri.setAmount(10);
		
		List<QnaVO> qnaList = qnaService.qnaList(book_bno, cri);
		
		// 2. 페이징 정보
		int qnaCount = qnaService.getCountQnaByBookBno(book_bno);
		PageDTO pageMaker = new PageDTO(cri, qnaCount);

		map.put("qnaList", qnaList);
		map.put("qnaPageMaker", pageMaker);
		
		log.info("qnaPageMaker : " + pageMaker);

		entity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

		
		return entity;
	}
	
	// QnA 상세글
	@GetMapping("/qnaform/{qna_code}")
	public ResponseEntity<QnaVO> qnaForm(@PathVariable("qna_code") Long qna_code) throws Exception {
		
		ResponseEntity<QnaVO> entity = null;
		
		QnaVO vo = qnaService.qnaForm(qna_code);		
		
		entity = new ResponseEntity<QnaVO>(vo, HttpStatus.OK);

		return entity;
		
	}
	
	// QnA 비밀번호 확인
	@GetMapping("/qnacheckpwd")
	public ResponseEntity<String> qnaCheckPwd(QnaVO vo, String qna_password, HttpSession session) throws Exception {
		
		log.info("입력한 비밀번호" + qna_password);
		
		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();
			
		ResponseEntity<String> entity = null;
		
		if (usr_id != null) {
			// 비밀번호 비교
			if (passwordEncoder.matches(qna_password, vo.getQna_password())) {
				entity = new ResponseEntity<String>("success", HttpStatus.OK);
			}
		} else {
			entity = new ResponseEntity<String>("fail", HttpStatus.OK);
		}
		log.info(usr_id);
		return entity;
	}
	
	
}
