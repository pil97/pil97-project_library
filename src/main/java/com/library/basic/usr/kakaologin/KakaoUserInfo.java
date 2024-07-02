package com.library.basic.usr.kakaologin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//카카오로부터 넘어오는 사용자정보를 담당하는 클래스
@AllArgsConstructor
@Getter
@Setter
@ToString
public class KakaoUserInfo {

	private Long id;
	private String nickname;
	private String email;
}
