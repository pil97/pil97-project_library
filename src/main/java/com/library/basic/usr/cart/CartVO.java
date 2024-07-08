package com.library.basic.usr.cart;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartVO {
	private Long cart_code;
	private int book_bno;
	private String usr_id;
	private int cart_amount;
	private Date cart_date;
}
