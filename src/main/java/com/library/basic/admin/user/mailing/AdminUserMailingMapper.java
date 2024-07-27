package com.library.basic.admin.user.mailing;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.common.dto.Criteria;

public interface AdminUserMailingMapper {
	
	// 메일 내용 DB 저장
	void mailingSave(MailingVO vo);
	
	// 회원테이블에서 전체회원 메일정보를 읽어오는 작업
	String[] getAllUserMailAddress();

	// 메일 발송 횟수 업데이트
	void mailSendCountUpadte(int idx);
	
	// 메일 발송 목록
	List<MailingVO> mailingList(@Param("cri") Criteria cri, @Param("title") String title);
	
	// 메일 발송 목록 개수
	int mailingListgetTotalCount(String title);
}
