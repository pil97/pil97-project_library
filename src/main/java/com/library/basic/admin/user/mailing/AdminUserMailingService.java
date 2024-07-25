package com.library.basic.admin.user.mailing;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminUserMailingService {
	
	private final AdminUserMailingMapper adminUserMailingMapper;
	
	// 메일 내용 DB 저장
	public void mailingSave(MailingVO vo) {
		adminUserMailingMapper.mailingSave(vo);
	};
	
	// 회원테이블에서 전체회원 메일정보를 읽어오는 작업
	public String[] getAllUserMailAddress() {
		return adminUserMailingMapper.getAllUserMailAddress();		
	};

}
