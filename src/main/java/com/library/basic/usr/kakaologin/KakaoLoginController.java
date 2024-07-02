package com.library.basic.usr.kakaologin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.basic.usr.SNSUserDto;
import com.library.basic.usr.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/oauth2/")
@Controller
public class KakaoLoginController {

	private final KakaoLoginService kakaoLoginService;
	
	private final UserService userService;

	@Value("${kakako.client.id}")
	private String clientId;

	@Value("${kakako.redirect.uri}")
	private String redirectUri;

	@Value("${kakako.client.secret}")
	private String clientSecret;

	// Step1. 인가 코드 받기
	// 주소 예)
	// https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}
	@GetMapping("/kakaologin")
	public String connect() {
		StringBuffer url = new StringBuffer();
		url.append("https://kauth.kakao.com/oauth/authorize?");
		url.append("response_type=code");
		url.append("&client_id=" + clientId);
		url.append("&redirect_uri=" + redirectUri);
		// 추가옵션. 다시 사요자 인증을 수행하고자 할 때 사용
		url.append("&prompt=login");

		log.info("인가코드 : " + url.toString());

		return "redirect:" + url.toString();
	}

	// Step2. 카카오 로그인 API Server에서 개발사이트 callback 주소 호출 카카오 개발자 사이트에서 애플리케이션 등록과정에서
	// 아래주소를 설정을 이미 한 상태
	@GetMapping("/callback/kakao")
	public String callback(String code, HttpSession session) {

		log.info("code : " + code);

		String accessToken = "";
		KakaoUserInfo kakaoUserInfo = null;

		try {
			// 카카오 로그인 API 서버에게 로그인 인증을 성공
			accessToken = kakaoLoginService.getAccessToken(code); // 인가코드를 통한 인증토큰을 요청
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			// 카카오 로그인 API 서버에서 보내 온 사용자 정보
			kakaoUserInfo = kakaoLoginService.getKakaoUserInfo(accessToken);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		log.info("access : " + accessToken);

		if (kakaoUserInfo != null) {

			log.info("사용자정보: " + kakaoUserInfo);

			// 인증을 세션 방식으로 처리
			session.setAttribute("kakaoStatus", kakaoUserInfo); // 인증여부 사용
			session.setAttribute("accessToken", accessToken); // 카카오 로그아웃 사용

			String sns_email = kakaoUserInfo.getEmail();

			String sns_login_type = userService.existUserInfo(sns_email);			

			// 둘 다 데이터가 존재하지 않으면 - 회원테이블 존재 안하고, 카카오 테이블에도 존재 안하면
			if (userService.existUserInfo(sns_email) == null && userService.snsUserCheck(sns_email) == null) {
				// SNS 테이블 데이터 삽입 작업
				SNSUserDto dto = new SNSUserDto();
				dto.setId(kakaoUserInfo.getId().toString());
				dto.setEmail(kakaoUserInfo.getEmail());
				dto.setNickname(kakaoUserInfo.getNickname());
				dto.setSns_type("kakao");

				userService.snsUserInsert(dto);

			}
		}

		return "redirect:/";
	}

	// 카카오톡 로그아웃 
	@GetMapping("/kakaologout")
	public String kakaoLogout(HttpSession session) {
						
		String accessToken = (String) session.getAttribute("accessToken");
				
		log.info("access : " + accessToken);
		
		if(accessToken != null && !"".equals(accessToken)) {
			try {				
				kakaoLoginService.kakaoLogout(accessToken);
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);				
			}
			
			session.removeAttribute("kakaoStatus");
			session.removeAttribute("accessToken");
		}
		
		log.info("로그아웃");
		
		return "redirect:/";
	}
	
}
