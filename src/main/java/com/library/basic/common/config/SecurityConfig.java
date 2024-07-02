package com.library.basic.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 설정 목적으로 사용하는 클래스는 @Configuration을 적용  
// @EnableWebSecurity
public class SecurityConfig {
	
	// 스프링 시큐리티 설정 
	// 2.7과 3.x 버전 차이가 있음
	/*
	@Bean
	SecurityFilterChain sevuSecurityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf((csrf) -> csrf.disable());
			// .cors((c) -> c.disable());
			// .headers((headers) -> headers.disable());
		
		return http.build();

	}
	*/
	
	// 암호화 기능 bean 등록 passwordEncoder bean 자동 생성 
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
