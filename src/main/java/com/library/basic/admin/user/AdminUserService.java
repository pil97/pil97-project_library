package com.library.basic.admin.user;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.UserVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminUserService {
	
	private final AdminUserMapper adminUserMapper;
	
	// 관리자 - 회원 목록
	public List<Map<String, Object>> adminUserList(Criteria cri, String startDate, String endDate) {
		return adminUserMapper.adminUserList(cri, startDate, endDate);
	};
	
	// 관리자 - 회원 목록 개수
	public int adminUserListgetTotalCount(Criteria cri, String startDate, String endDate) {
		return adminUserMapper.adminUserListgetTotalCount(cri, startDate, endDate);
	}
	
	// 관리자 - 회원 상세 정보
	public UserVO adminUserDetailInfo(String usr_id) {
		return adminUserMapper.adminUserDetailInfo(usr_id);
	};
	
	// 관리자 - 회원 상세 정보 수정
	public void userModify(UserVO vo) {
		adminUserMapper.userModify(vo);
	};
	
	// 관리자 - 회원 아이디 삭제
	public void userDelete(String usr_id) {
		adminUserMapper.userDelete(usr_id);
	};	
}
