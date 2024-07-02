package com.library.basic.usr.naverlogin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Response {
	private String id;
	private String nickname; 
	private String name; 
	private String email;
	private String gender;
	private String age; 
	private String birthday; 
	private String profile_image;
	private String birthyear; 
	private String mobile;
}
