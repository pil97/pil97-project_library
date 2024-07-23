package com.library.basic.usr.qna;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MyQnaVO {
	
	private Long qna_code;
	private String usr_id;
	private int book_bno;
	private String qna_type;
	private String qna_password;
	private String qna_title;
	private String qna_content;
	private String qna_check;
	private String qna_answer;
	private Date qna_update;
	private Date qna_date;
	private Date qna_answer_date;
	private String book_name;
	private String book_up_folder;
	private String book_img;

}
