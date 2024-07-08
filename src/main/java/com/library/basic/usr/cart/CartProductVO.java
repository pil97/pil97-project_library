package com.library.basic.usr.cart;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartProductVO {
	
	private Long cart_code;
	private int book_bno;
	private int cart_amount;
	private String book_name;
	private int book_deposit;
	private String book_up_folder;
	private String book_img;
	
}
