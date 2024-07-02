package com.library.basic.usr.kakaologin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaoLoginService {

	@Value("${kakako.client.id}")
	private String clientId;

	@Value("${kakako.redirect.uri}")
	private String redirectUri;

	@Value("${kakako.client.secret}")
	private String clientSecret;

	@Value("${kakao.oauth.tokenuri}")
	private String tokenUri;

	@Value("${kakao.oauth.userinfouri}")
	private String userInfoUri;

	@Value("${kakao.user.logout}")
	private String kakaoLogout;

	// Step2. 토큰 받기
	public String getAccessToken(String code) throws JsonProcessingException {
		// 요청 보내기
		// 1. Http header 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// 2. Http body 생성
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", clientId);
		body.add("redirect_uri", redirectUri);
		body.add("code", code);
		body.add("client_secret", clientSecret);

		// 3. Header + Body 정보를 Entity로 구성
		HttpEntity<MultiValueMap<String, String>> tokenKakaoRequest = new HttpEntity<MultiValueMap<String, String>>(
				body, headers);

		// 4. Http 요청 보내기
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = restTemplate.exchange(tokenUri, HttpMethod.POST, tokenKakaoRequest,
				String.class);

		// 5. Http 응답(JSON) -> Access Token 추출(Parsing)작업
		String responseBody = response.getBody();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);

		return jsonNode.get("access_token").asText();
	}

	// 액세스토큰을 이용한 사용자정보 받아오기
	public KakaoUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {

		// 1)Header 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// 2)Body 생성안함. API 매뉴얼에서 필수가 아님.

		// 3)Header + Body 정보를 Entity로 구성.
		HttpEntity<MultiValueMap<String, String>> userInfoKakaoRequest = new HttpEntity<>(headers);

		// 4)Http 요청
		RestTemplate restTemplate = new RestTemplate();

		// 5)Http 응답
		ResponseEntity<String> responseEntity = restTemplate.exchange(userInfoUri, HttpMethod.POST,
				userInfoKakaoRequest, String.class);

		String responseBody = responseEntity.getBody();

		log.info("사용자정보 응답데이터 : " + responseBody);

		ObjectMapper objctMapper = new ObjectMapper();
		JsonNode jsonNode = objctMapper.readTree(responseBody);

		// 인증 토큰을 이용한 카카오 사용자에 대한 정보를 받아옴
		Long id = jsonNode.get("id").asLong();
		String nickname = jsonNode.get("properties").get("nickname").asText();
		String email = jsonNode.get("kakao_account").get("email").asText();		

		return new KakaoUserInfo(id, nickname, email);
	}
	
	// 카카오 로그아웃(연결끊기) - https://kauth.kakao.com/oauth/logout
		public void kakaoLogout(String accessToken) throws JsonProcessingException{
							
			// Http Header 생성
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + accessToken);
			headers.add("Content-type", "application/x-www-form-urlencoded"); 
			
			// Http 요청 작업
			HttpEntity<MultiValueMap<String, String>> kakaoLogoutRequest = new HttpEntity<>(headers);
			
			// Http 요청하기
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.exchange(kakaoLogout, HttpMethod.POST, kakaoLogoutRequest, String.class);
			
			// 리턴된 정보 : JSON 포맷 문자열
			String responseBody = response.getBody();
			log.info("responseBody : " + responseBody);
		
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(responseBody);
			
			Long id = jsonNode.get("id").asLong();
			
			log.info("id : " + id);
			
		}
		
}
