package com.library.basic.usr.naverlogin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NaverLoginService {

	@Value("${naver.client.id}")
	private String clientId;

	@Value("${naver.redirect.uri}")
	private String redirectUri;

	@Value("${naver.client.secret}")
	private String clientSecret;

	// 3.4.2 네이버 로그인 연동 URL 생성하기
	// https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=CLIENT_ID&state=STATE_STRING&redirect_uri=CALLBACK_URL
	// 위에 url을 반환하기 위해 작업
	public String getNaverAuthorizeUrl() throws UnsupportedEncodingException {

		UriComponents uriComponents = UriComponentsBuilder
				.fromUriString("https://nid.naver.com/oauth2.0/authorize?")
				.queryParam("response_type", "code")
				.queryParam("client_id", clientId)
				.queryParam("state", URLEncoder.encode("1234", "UTF-8"))
				.queryParam("redirect_uri", URLEncoder.encode(redirectUri, "UTF-8"))
				.build();

		return uriComponents.toString();
	}

	// https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=jyvqXeaVOVmV&client_secret=527300A0_COq1_XV33cf&code=EIc5bFrl4RibFls1&state=9kgsGTfH4j7IyAkg
	// 3.4.3 네이버 로그인 연동 결과 Callback 정보
	public String getNaverTokerUrl(NaverCallback callback) {

		try {

			UriComponents uriComponents;

			uriComponents = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/token")
					.queryParam("grant_type", "authorization_code")
					.queryParam("client_id", clientId)
					.queryParam("client_secret", clientSecret)
					.queryParam("code", callback.getCode())
					.queryParam("state", URLEncoder.encode(callback.getState(), "UTF-8"))
					.build();

			// access token을 받기 위한 요청 보내기 작업 후, 결과값이 JSON으로 응답
			URL url = new URL(uriComponents.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			int responseCode = conn.getResponseCode();
			BufferedReader br;

			// 입력스트림 작업
			// conn.getInputStream() : 바이트 스트림
			// InputStreamReader 클래스 : byte 기반의 스트림을 문자 기반 스트림으로 변환하는 기능
			// BufferedReader : 버퍼기능 제공 보조 스트림
			if (responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}

			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}

			br.close();

			// log.info("응답데이터 : " + response.toString());

			return response.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	/*
	 * 3.4.5 접근 토큰을 이용하여 프로필 API 호출하기 Authorization : 접근 토큰(access token)을 전달하는 헤더
	 * 다음과 같은 형식으로 헤더 값에 접근 토큰(access token)을 포함합니다. 토큰 타입은 "Bearer"로 값이 고정되어 있습니다.
	 * Authorization: {토큰 타입] {접근 토큰]
	 * 사용자 정보 받아오기 
	 */

	public String getNaverUserByToken(NaverToken naverToken) {
		String accessToken = naverToken.getAccess_token();
		String tokenType = naverToken.getToken_type();

		try {
			URL url = new URL("https://openapi.naver.com/v1/nid/me");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", tokenType + " " + accessToken);

			int responseCode = conn.getResponseCode();
			BufferedReader br;

			// 입력스트림 작업
			// conn.getInputStream() : 바이트 스트림
			// InputStreamReader 클래스 : byte 기반의 스트림을 문자 기반 스트림으로 변환하는 기능
			// BufferedReader : 버퍼기능 제공 보조 스트림
			if (responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}

			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}

			br.close();

			// log.info("사용자 정보 응답데이터 : " + response.toString());

			return response.toString();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	// 5-3 참고
	public void getNaverTokenDelete(String access_token) {

		try {

			UriComponents uriComponents;

			uriComponents = UriComponentsBuilder
					.fromUriString("https://nid.naver.com/oauth2.0/token")
					.queryParam("grant_type", "delete")
					.queryParam("client_id", clientId)
					.queryParam("client_secret", clientSecret)
					// .queryParam("code", callback.getCode())
					// .queryParam("state", URLEncoder.encode(callback.getState(), "UTF-8"))
					.queryParam("access_token", URLEncoder.encode(access_token, "UTF-8"))
					.build();

			// access token을 받기 위한 요청 보내기 작업 후, 결과값이 JSON으로 응답
			
			URL url = new URL(uriComponents.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");		
			
			int responseCode = conn.getResponseCode();
			
			// log.info("응답코드 : " + responseCode);
			
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
