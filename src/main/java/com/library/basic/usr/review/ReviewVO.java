package com.library.basic.usr.review;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewVO {

	private Long rev_code;
	private String usr_id;
	private int book_bno;
	private String rev_title;
	private String rev_content;
	private int rev_rate;
	private Date rev_date;
	
}
