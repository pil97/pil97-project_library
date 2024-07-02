package com.library.basic.usr;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserMapper userMapper;

	// 회원가입
	public void sign(UserVO vo) {
		userMapper.sign(vo);
	}

	// 아이디 중복체크
	public String idCheck(String usr_id) {

		return userMapper.idCheck(usr_id);
	};

	// 로그인
	public UserVO login(String usr_id) {

		return userMapper.login(usr_id);
	};

	// 아이디 찾기
	public String idFind(String usr_name, String usr_email) {
		return userMapper.idFind(usr_name, usr_email);
	};

	// 비밀번호 재발급 확인 작업
	public String pwFind(String usr_id, String usr_name, String usr_email) {
		return userMapper.pwFind(usr_id, usr_name, usr_email);
	};

	// 임시 비밀번호 생성(UUID 이용)
	public String getTempPw() {
		String tempPw = UUID.randomUUID().toString().replaceAll("-", ""); // - 를 제거
		tempPw = tempPw.substring(0, 10); // 10자리
		return tempPw;
	}
	
	// 임시 비밀번호 생성 후 DB 수정
	public void tempPwUpdate(String usr_id, String temp_enc_pw) {
		userMapper.tempPwUpdate(usr_id, temp_enc_pw);
	};

	// 내정보 수정
	public void modify(UserVO vo) {
		userMapper.modify(vo);
	};
	
	// 비밀번호 변경
	void changePw(String usr_id, String new_pwd) {
		userMapper.changePw(usr_id, new_pwd);		
	};
	
	// 회원 탈퇴
	public void delete(String usr_id) {
		userMapper.delete(usr_id);
	};
	
	// SNS 유저 회원가입 확인 
	public String existUserInfo(String sns_email) {
		return userMapper.existUserInfo(sns_email);
	}
	
	// SNS 유저 중복 체크
	public String snsUserCheck(String sns_email) {
		return userMapper.snsUserCheck(sns_email);
	};
	
	// SNS 유저 정보 DB 저장
	public void snsUserInsert(SNSUserDto dto) {
		userMapper.snsUserInsert(dto);
	};
}
