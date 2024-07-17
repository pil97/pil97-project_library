package com.library.basic.usr.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.basic.usr.cart.CartMapper;
import com.library.basic.usr.payment.PaymentMapper;
import com.library.basic.usr.payment.PaymentVO;

import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderMapper orderMapper;
	private final PaymentMapper paymentMapper;
	private final CartMapper cartMapper;

	@Transactional
	public void orderProcess(OrderVO vo, String usr_id, String payMethod, String pay_bankinfo, String pay_account, 
			String pay_name, String p_status) {
				
		// 1. 주문테이블 db 저장
		vo.setUsr_id(usr_id);
		
		log.info("입력한 정보 : " + vo);
		
		orderMapper.orderInsert(vo);

		// 2. 주문상세테이블 db 저장
		orderMapper.orderDetailInsert(vo.getOrd_code(), usr_id, vo.getOrd_price());

		// 3. 결제테이블 db 저장
		PaymentVO paymentVO = PaymentVO.builder()
								.ord_code(vo.getOrd_code())
								.usr_id(usr_id)			
								.paymethod(payMethod)
								.pay_bankinfo(pay_bankinfo)
								.pay_account(pay_account)
								.pay_name(pay_name)
								.pay_price(vo.getOrd_price())	
								.pay_status(p_status)
								.build();

		paymentMapper.paymentInsert(paymentVO);

		// 4. 해당 아이디 장바구니 테이블 삭제
		cartMapper.cartEmpty(usr_id);
	}

}
