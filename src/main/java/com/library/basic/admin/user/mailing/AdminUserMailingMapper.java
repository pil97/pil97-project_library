package com.library.basic.admin.user.mailing;

public interface AdminUserMailingMapper {
	
	// 메일 내용 DB 저장
	void mailingSave(MailingVO vo);
	
	// 회원테이블에서 전체회원 메일정보를 읽어오는 작업
	String[] getAllUserMailAddress();

}
