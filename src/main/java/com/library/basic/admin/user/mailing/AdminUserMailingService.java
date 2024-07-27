package com.library.basic.admin.user.mailing;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.library.basic.common.dto.Criteria;

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
	
	// 메일 발송 횟수 업데이트
	public void mailSendCountUpadte(int idx) {
		adminUserMailingMapper.mailSendCountUpadte(idx);
	};
	
	// 메일 발송 목록
	public List<MailingVO> mailingList(@Param("cri") Criteria cri, @Param("title") String title) {
		return adminUserMailingMapper.mailingList(cri, title);
	};
	
	// 메일 발송 목록 개수
	public int mailingListgetTotalCount(String title) {
		return adminUserMailingMapper.mailingListgetTotalCount(title);
	};

}
