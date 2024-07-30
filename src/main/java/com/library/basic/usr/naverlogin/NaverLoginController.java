package com.library.basic.usr.naverlogin;

import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.basic.usr.UserService;
import com.library.basic.usr.UserVO;

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
			// log.info(callback.getError_description());
		}

		// JSON 포맷의 응답데이터
		String responseToken = naverLoginService.getNaverTokerUrl(callback);

		ObjectMapper objectMapper = new ObjectMapper();
		NaverToken naverToken = objectMapper.readValue(responseToken, NaverToken.class);

		// log.info("토큰정보 : " + naverToken.toString());

		// 엑세스 토큰을 이용한 사용자 정보 받아오기
		String responseUser = naverLoginService.getNaverUserByToken(naverToken);
		NaverResponse naverResponse = objectMapper.readValue(responseUser, NaverResponse.class);

		// log.info("사용자 정보 : " + naverResponse.toString());

		String sns_email = naverResponse.getResponse().getEmail();

		if (naverResponse != null) {
			session.setAttribute("naverStatus", naverResponse);
			session.setAttribute("accessToken", naverToken.getAccess_token());
		}
		
	    // SNS 회원가입 중복 체크
	    boolean isUserExist = userService.existUserInfo(sns_email) != null;
	    // boolean isSNSUserExist = userService.snsUserCheck(sns_email) != null;
		
		// SNS 회원가입 중복 체크
	    if (!isUserExist) {
	        // 회원가입 페이지로 리디렉션
	        return "redirect:/user/sign";
	    } else {
	        // 이미 가입된 사용자, 메인 페이지로 리디렉션
	    	UserVO vo = userService.snsLogin(naverResponse.getResponse().getEmail());	    	
	        session.setAttribute("loginStatus", vo);
	        return "redirect:/";
	    }
		
		

		

	}

	@GetMapping("/naverlogout")
	public String naverLogout(HttpSession session) {

		String accessToken = (String) session.getAttribute("accessToken");

		// log.info("access : " + accessToken);

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
