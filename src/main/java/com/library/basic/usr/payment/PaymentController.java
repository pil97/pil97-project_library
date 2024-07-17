package com.library.basic.usr.payment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/payment/*")
@Controller
public class PaymentController {
	
	private final PaymentService paymentService;

}
