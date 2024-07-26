package com.library.basic.admin.user.mailing;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MailingVO {

	private Integer idx;
	private String title;
	private String content;
	private String type;
	private String sendCount;
	private Date regDate;

}
