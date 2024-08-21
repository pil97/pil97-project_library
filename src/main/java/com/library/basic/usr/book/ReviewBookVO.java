package com.library.basic.usr.book;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewBookVO {
	private Integer book_bno;
	private Integer c_code; // 2차 카테고리 코드
	private String book_name;
	private String book_author;
	private String book_publishing_date;
	private int book_deposit;
	private int book_deposit_discount;
	private String book_publisher;
	private String book_content;
	private String book_up_folder;
	private String book_img;
	private int book_amount;
	private String book_loan;
	private Date book_date;
	private Date book_updatedate;
	private String book_isbn;
	private int book_page;
	private int reviewCount;

}
