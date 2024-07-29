package com.library.basic.admin.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.UserVO;
import com.library.basic.usr.qna.MyQnaVO;

public interface AdminUserMapper {

	// 관리자 - 회원 목록
	List<Map<String, Object>> adminUserList(@Param("cri") Criteria cri, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	// 관리자 - 회원 목록 개수
	int adminUserListgetTotalCount(@Param("cri") Criteria cri, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	// 관리자 - 회원 상세 정보
	UserVO adminUserDetailInfo(String usr_id);
	
	// 관리자 - 회원 상세 정보 수정
	void userModify(UserVO vo);
	
	// 관리자 - 회원 아이디 삭제
	void userDelete(String usr_id);
	
}
