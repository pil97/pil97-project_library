package com.library.basic.usr.naverlogin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NaverToken {
	private String access_token;
	private String refresh_token; 
	private String token_type; 
	private Integer expires_in;
	private String error;
	private String error_description;
}
