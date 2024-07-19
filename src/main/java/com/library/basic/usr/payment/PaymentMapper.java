package com.library.basic.usr.payment;

public interface PaymentMapper {
	
	// 사용자 - 결제테이블 db 저장
	void paymentInsert(PaymentVO vo);
	
	// 관리자 - 주문상세관리 결제정보
	PaymentVO orderPayInfo(Long ord_code);

}
