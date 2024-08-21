package com.library.basic.usr.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.book.BookMapper;
import com.library.basic.usr.cart.CartMapper;
import com.library.basic.usr.payment.PaymentMapper;
import com.library.basic.usr.payment.PaymentVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderMapper orderMapper;
	private final PaymentMapper paymentMapper;
	private final CartMapper cartMapper;
	private final BookMapper bookMapper;

	@Transactional
	public void orderProcess(OrderVO vo, String usr_id, String payMethod, String pay_bankinfo, String pay_account, 
			String pay_name, String p_status, List<Integer> book_bno, List<Integer> book_amount) {
				
		// 1. 주문테이블 db 저장
		vo.setUsr_id(usr_id);
		
		log.info("입력한 정보 : " + vo);
		
		orderMapper.orderInsert(vo);

		// 2. 주문상세테이블 db 저장
		orderMapper.orderDetailInsert(vo.getOrd_code(), usr_id);

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
							
		// 5. 도서 테이블 수량 감소	  
		for (int i = 0; i < book_bno.size(); i++) { 
			
			bookMapper.bookQuantityDecrease(book_bno.get(i), book_amount.get(i));
	    
		}
	    	  		    	    		     
	    	
	}
	
	// 나의 주문내역 목록
	public List<OrderVO> myOrderList(Criteria cri, String startDate, String endDate, String usr_id) {
		return orderMapper.myOrderList(cri, startDate, endDate, usr_id);
	};

	// 나의 주문내역 개수
	public int myOrderListGetTotalCount(Criteria cri, String startDate, String endDate, String usr_id) {
		return orderMapper.myOrderListGetTotalCount(cri, startDate, endDate, usr_id);				
	}	
}
