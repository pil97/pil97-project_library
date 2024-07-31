package com.library.basic.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.library.basic.admin.AdminVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminInterceptor implements HandlerInterceptor {
	// 3개 메서드가 이벤트 특징을 가지고 있다
	// 순서 메핑주소가 요청 발생 -> 1. preHandle 메서드 2. Controller URL 주소의 메서드 3. postHandle
	// 메서드 4. afterCompletion
	// preHandle -> Controller -> postHandle -> JSP

	// Controller로 요청이 들어가기 전에 가로채어 다음 메서드가 호출(실행)된다.
	// 그리고 리턴값이 true가 되면, 컨트롤러로 진행이 이루어진다.

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		log.info("preHandle");

		boolean result = false;

		HttpSession session = request.getSession();
		AdminVO vo = (AdminVO) session.getAttribute("adminStatus");

		if (vo == null) { // 요청이 인증되지 않은 상태를 의미

			result = false;

			if (isAjaxRequest(request)) { // ajax 요청이라는 의미
				response.sendError(400);
			} else {
				// 원래 요청한 주소를 세션으로 저장하는 기능
				getTargetUrl(request);
				response.sendRedirect("/admin/");
			}

		} else {
			result = true;
		}

		// true -> 요청했던 주소로 이동
		return result;
	}

	// 원래 요청한 주소
	private void getTargetUrl(HttpServletRequest request) {

		String uri = request.getRequestURI(); // /userinfo/mypage
		String query = request.getQueryString(); // ? 물음표 뒤의 문자열. ?userid=user01

		if (query == null || query.equals("null")) {
			query = "";
		} else {
			query = "?" + query;
		}

		String targetUrl = uri + query;

		log.info(targetUrl);

		// 사용자 요청이 GET 방식일 경우
		if (request.getMethod().equals("GET")) {
			request.getSession().setAttribute("targetUrl", targetUrl);
		}

	}

	// 사용자 요청이 ajax 요청인지 구분
	private boolean isAjaxRequest(HttpServletRequest request) {
		boolean isAjax = false;

		String header = request.getHeader("AJAX");

		if (header != null && header.equals("true")) {
			isAjax = true;
		}

		return isAjax;
	}

}
