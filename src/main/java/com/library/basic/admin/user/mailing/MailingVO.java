package com.library.basic.admin.user.mailing;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class MailingVO {

	private Integer mailingIdx;
	private String mailingTitle;
	private String mailingContent;
	private String mailingType;
	private Date mailingRegDate;
	
}
