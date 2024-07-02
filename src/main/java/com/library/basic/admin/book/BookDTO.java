package com.library.basic.admin.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BookDTO {
	
	private Integer book_bno;
	private String book_author;
	private int book_deposit;
	private String book_publisher;	
	private String book_loan;
	
}
