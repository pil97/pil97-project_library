package com.library.basic.common.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.library.basic.common.interceptor.AdminInterceptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AdminMvcConfig implements WebMvcConfigurer{
	
	private final AdminInterceptor adminInterceptor;

	// 인터셉터 메핑주소 설정에서 제외되는 경로 작업
	// 즉, 인터셉터가 동작되지 않게 하는 주소 
	private static final String[] EXCULDE_PATHS = {
			"/admin/",
			"/admin/adminok",
			"/admin/category/secondcategory/*"
			
	};
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminInterceptor)
				.addPathPatterns("/admin/**") 			// admin으로 시작하는 하위레벨의 깊이에 상관없이 모든 주소를 설정 
				.excludePathPatterns(EXCULDE_PATHS);	// 위의 설정에서 제외든 주소 설정 
	}

	
	
}
