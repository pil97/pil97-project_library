package com.library.basic.mail;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/email/*")
public class EmailController {

	// EmailServiceImpl 주입 받음
	private final EmailService emailService;

	// 스프링이 밑에 작업을 자동으로 처리 해줌
	// EmailDTO dto = new EmailDTO();
	// dto.setReceiverMail("입력한 메일주소");
	// 클라이언트에서 입력 받은 메일주소가 EmailDTO dto에 들어감 - receiverMail
	@GetMapping("authcode")
	public ResponseEntity<String> authCode(String type, EmailDTO dto, HttpSession session) {

		log.info("dto: " + dto); // dto.toString() 호출

		ResponseEntity<String> entity = null;

		// 인증코드 생성
		String authCode = "";
		for (int i = 0; i < 6; i++) {
			authCode += String.valueOf((int) (Math.random() * 10));
		}

		log.info("인증코드: " + authCode);

		// 사용자가 자신의 메일에서 발급받은 인증코드를 읽고, 회원가입시 인증 확인란에 입력을 하게되면, 서버에서 비교목적으로 세션방식으로 인증코드를
		// 메모리에 저장해야함
		// getAttribute 사용시 "authCode" 이걸로 사용해야함 
		session.setAttribute("authCode", authCode); // 톰캣서버 내장 세션 30분

		try {
			// 메일 발송
			
			// 메일 제목 변경 
			if(type.equals("emailJoin")) {
				dto.setSubject("필 도서 회원가입 메일 인증코드입니다.");
			} else if (type.equals("emailID")) {
				dto.setSubject("필 도서 아이디 찾기 메일 인증코드입니다.");
			} else if (type.equals("emailPw")) {
				dto.setSubject("필 도서 비밀번호 찾기 메일 인증코드입니다.");
			}
			
			emailService.sendMail(type, dto, authCode);
			entity = new ResponseEntity<String>("success", HttpStatus.OK); // 200
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
		}

		return entity;

	}

	// 인증코드 확인
	@GetMapping("confirm_authcode")
	public ResponseEntity<String> confirmAuthCode(String authCode, HttpSession session) {
		ResponseEntity<String> entity = null;

		// session.setAttribute("authCode", authCode);
		// 세션이 유지되고 있는 동안
		if (session.getAttribute("authCode") != null) {
			if (authCode.equals(session.getAttribute("authCode"))) {
				entity = new ResponseEntity<String>("success", HttpStatus.OK);
				session.removeAttribute("authCode"); // 서버의 메모리 초기화 
			} else {
				entity = new ResponseEntity<String>("fail", HttpStatus.OK);
			}
		} else {
			// 세션이 소멸되었을 경우
			entity = new ResponseEntity<String>("request", HttpStatus.OK);
		}

		return entity;
	}

}
