package com.library.basic.common.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.library.basic.common.interceptor.LoginInterceptor;

import lombok.RequiredArgsConstructor;

// 용도? LoginInterceptor 인터셉터 클래스를 위한 설정 

@RequiredArgsConstructor
@Component // webMvcConfig bean 생성(스프링 객체) - bean을 관리하는 곳 : 스프링 컨테이너 
public class UserMvcConfig implements WebMvcConfigurer {

	private final LoginInterceptor loginInterceptor;

	// 어떤 메핑주소를 인터럽트 할 것인가? 설정 
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(loginInterceptor)
				.addPathPatterns(
						"/user/mypage",
						"/user/myorderlist",
						"/user/myqnalist",
						"/user/myreviewlist",
						"/user/changepw", 
						"/user/delete",						
						"/user/cart/cartlist",
						"/user/cart/cartadd",
						"/user/checklogin",
						"/user/order/orderlist",
						"/user/order/ordercomplete",
						"/user/kakoa/approval"
					);
		
		// 특정 주소가 인증된 경우에만 사용 할 때 : /userinfo/* -> userinfo로 시작하는 바로 밑에 레벨 모든 주소에 대해 인터셉터 사용
		// /userinfo/** : userinfo에 시작하는 모든레벨에 모든 주소에 대해 인터럽트 사용 
	}

}
