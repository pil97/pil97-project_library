package com.library.basic.usr;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserVO {
	private String usr_id;
	private String usr_name;
	private String usr_email;
	private String usr_password;
	private String usr_zipcode;
	private String usr_addr;
	private String usr_deaddr;
	private String usr_phone;
	private String usr_receive;
	private int usr_point;
	private String usr_sns_type; 
	private Date usr_lastlogin;
	private Date usr_datesub;
	private Date usr_updatedate;
}
