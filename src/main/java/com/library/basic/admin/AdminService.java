package com.library.basic.admin;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminService {

	private final AdminMapper adminMapper;
	
	// 관리자 로그인 확인
	public AdminVO loginOk(String admin_id) {
		return adminMapper.loginOk(admin_id);
	};
	
	
}


