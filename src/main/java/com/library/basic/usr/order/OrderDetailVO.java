package com.library.basic.usr.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDetailVO {
	
	private Long ord_code;
	private int book_bno;
	private int dt_amount;
	private int dt_price;
}
