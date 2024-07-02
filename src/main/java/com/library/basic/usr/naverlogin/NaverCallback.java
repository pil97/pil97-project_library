package com.library.basic.usr.naverlogin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NaverCallback {
	private String code;
	private String state; 
	private String error; 
	private String error_description;
}
