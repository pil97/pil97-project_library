package com.library.basic.usr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SNSUserDto {
	private String id;
	private String nickname;
	private String email;
	private String sns_type;
	
}
