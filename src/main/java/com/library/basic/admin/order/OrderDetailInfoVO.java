package com.library.basic.admin.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDetailInfoVO {
	
	private Long ord_code;
	private Integer book_bno;
	private int dt_amount;
	private int dt_price;
	private int book_deposit;
	private String book_name;
	private String book_up_folder;
	private String book_img;
	

}
