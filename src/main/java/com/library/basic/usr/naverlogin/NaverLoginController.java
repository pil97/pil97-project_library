package com.library.basic.usr.naverlogin;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.basic.usr.SNSUserDto;
import com.library.basic.usr.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/oauth2/*")
@RequiredArgsConstructor
@Controller
public class NaverLoginController {

	private final NaverLoginService naverLoginService;

	private final UserService userService;

	@GetMapping("naverlogin")
	public String connect() throws UnsupportedEncodingException {

		String url = naverLoginService.getNaverAuthorizeUrl();

		return "redirect:" + url;

	}

	// step2.
	// callback 주소 생성작업
	@GetMapping("/callback/naver")
	public String callback(NaverCallback callback, HttpSession session) throws JsonMappingException, Exception {

		if (callback.getError() != null) {
			log.info(callback.getError_description());
		}

		// JSON 포맷의 응답데이터
		String responseToken = naverLoginService.getNaverTokerUrl(callback);

		ObjectMapper objectMapper = new ObjectMapper();
		NaverToken naverToken = objectMapper.readValue(responseToken, NaverToken.class);

		log.info("토큰정보 : " + naverToken.toString());

		// 엑세스 토큰을 이용한 사용자 정보 받아오기
		String responseUser = naverLoginService.getNaverUserByToken(naverToken);
		NaverResponse naverResponse = objectMapper.readValue(responseUser, NaverResponse.class);

		log.info("사용자 정보 : " + naverResponse.toString());

		String sns_email = naverResponse.getResponse().getEmail();

		if (naverResponse != null) {
			session.setAttribute("naverStatus", naverResponse);
			session.setAttribute("accessToken", naverToken.getAccess_token());
		}

		// SNS 회원가입 중복 체크
		if (userService.existUserInfo(sns_email) == null && userService.snsUserCheck(sns_email) == null) {
			// true, SNS 테이블 데이터 삽입 작업
			SNSUserDto dto = new SNSUserDto();
			dto.setId(naverResponse.getResponse().getId());
			dto.setEmail(naverResponse.getResponse().getEmail());
			dto.setNickname(naverResponse.getResponse().getName());
			dto.setSns_type("naver");

			userService.snsUserInsert(dto);

		}

		return "redirect:/";

	}

	@GetMapping("/naverlogout")
	public String naverLogout(HttpSession session) {

		String accessToken = (String) session.getAttribute("accessToken");

		log.info("access : " + accessToken);

		if (accessToken != null && !"".equals(accessToken)) {
			try {
				log.info("로그아웃");
				naverLoginService.getNaverTokenDelete(accessToken);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			session.removeAttribute("naverStatus");
			session.removeAttribute("accessToken");
		}

		log.info("로그아웃");

		return "redirect:/";
	}

}
