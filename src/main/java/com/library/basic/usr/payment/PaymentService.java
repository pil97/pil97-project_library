package com.library.basic.usr.payment;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {
	
	private final PaymentMapper paymentMapper;
	
	// 관리자 - 주문상세관리 결제정보
	public PaymentVO orderPayInfo(Long ord_code) {
		return paymentMapper.orderPayInfo(ord_code);
	};

}
