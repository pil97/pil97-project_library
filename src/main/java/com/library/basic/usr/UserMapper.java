package com.library.basic.usr;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {

	// 회원가입
	void sign(UserVO vo);

	// 아이디 중복체크
	String idCheck(String usr_id);

	// 로그인
	UserVO login(String usr_id);
	
	// 네이버 로그인
	UserVO snsLogin(String snsEmail);

	// 아이디 찾기
	String idFind(@Param("usr_name") String usr_name, @Param("usr_email") String usr_email);

	// 비밀번호 재발급 확인 작업
	String pwFind(@Param("usr_id") String usr_id, @Param("usr_name") String usr_name,
			@Param("usr_email") String usr_email);

	// 임시 비밀번호 생성 후 DB 수정
	void tempPwUpdate(@Param("usr_id") String usr_id, @Param("temp_enc_pw") String temp_enc_pw);

	// 내정보 수정
	void modify(UserVO vo);

	// 비밀번호 변경
	void changePw(@Param("usr_id") String usr_id, @Param("new_pwd") String new_pwd);

	// 회원 탈퇴
	void delete(String usr_id);

	// SNS 유저 회원가입 확인
	String existUserInfo(String sns_email);

	// SNS 유저 중복 체크
	String snsUserCheck(String sns_email);

	// SNS 유저 정보 DB 저장
	void snsUserInsert(SNSUserDto dto);

}
