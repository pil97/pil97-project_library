package com.library.basic.usr.payment;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class PaymentVO {
	
	private Integer pay_id;
	private Long ord_code;
	private String usr_id;
	private String paymethod;
	private String pay_bankinfo;
	private String pay_account;
	private String pay_name;
	private int pay_price;
	private String pay_status;
	private Date pay_date;

}
