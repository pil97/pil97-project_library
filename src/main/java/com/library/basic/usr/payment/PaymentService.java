package com.library.basic.usr.payment;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {
	
	private final PaymentMapper paymentMapper;

}
