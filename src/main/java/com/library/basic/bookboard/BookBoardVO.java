package com.library.basic.bookboard;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class BookBoardVO {
	private Long b_bno;
	private String b_title;
	private String b_author;
	private String b_content;
	private String b_publishing_house;
	private String b_publication_date;
	private Date b_regdate;
	private Date b_updatedate;
	private int b_viewcount;
	private int b_bookquantity;
	
}
