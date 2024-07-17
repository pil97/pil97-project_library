package com.library.basic.usr.kakaopay;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.library.basic.usr.UserVO;
import com.library.basic.usr.cart.CartProductVO;
import com.library.basic.usr.cart.CartService;
import com.library.basic.usr.order.OrderService;
import com.library.basic.usr.order.OrderVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/kakao/*")
@Controller
public class KakaoPayController {

	private final KakaoPayService kakaoPayService;	
	private final CartService cartService;	
	private final OrderService orderService;
	
	private OrderVO vo;	
	private String usr_id;
	
	@GetMapping("/kakaopayrequest")
	public void kakaoPayRequest() {

	}
	
	@ResponseBody
	@GetMapping(value = "/kakaopay")
	public ReadyResponse kakaoPay(OrderVO vo, HttpSession session) {
		
		log.info("주문자 정보 : " + vo);

		// 1. 결제 준비 요청(ready)
		// ready(String partnerOrderId, String partnerUserId, String itemName, int quantity, int totalAmount, int taxFreeAmount, int vatAmount)		
		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();
		this.usr_id = usr_id;
		
		List<CartProductVO> cartList = cartService.cartList(usr_id);
		
		String itemName = "";
		int quantity = 0;
		int totalAmount = 0;
		int taxFreeAmount = 0; 
		int vatAmount = 0;
		
		for(int i=0; i < cartList.size(); i++) {
			itemName += cartList.get(i).getBook_name() + "/";
			quantity += cartList.get(i).getCart_amount();
			totalAmount += cartList.get(i).getBook_deposit() * cartList.get(i).getCart_amount();
		};
		
		String partnerOrderId = usr_id;
		String partnerUserId = usr_id;
		
		ReadyResponse readyResponse = kakaoPayService.ready(partnerOrderId, partnerUserId, itemName, quantity, totalAmount, taxFreeAmount, vatAmount);											

		log.info("응답데이터" + readyResponse);
		
		this.vo = vo;

		return readyResponse;

	}
	
	// 성공
	@GetMapping("/approval")
	public void approval(String pg_token) {
		log.info("pg_token : " + pg_token);
		
		// 2. 결제승인 요청
		String approvalResponse = kakaoPayService.approve(pg_token);
		
		// 주문정보 저장
		// 최종결과 -> aid 값이 존재하면
		
		log.info("최종결과2 : " + approvalResponse);
		
		// 트랜잭션으로 처리 : 주문테이블, 주문상세테이블, 결제테이블, 장바구니 비우기
		if(approvalResponse.contains("aid")) {
			
			log.info("주문자 정보2 : " + vo);
			String payName = vo.getOrd_name();
			orderService.orderProcess(vo, usr_id, "카카오페이", "카카오페이", "카카오페이", payName, "완납");
		}
		
		log.info("최종결과 : " + approvalResponse);
	}
	
	// 취소
	@GetMapping("/cancel")
	public void cancel() {
		
	}
	
	// 실패
	@GetMapping("/fail")
	public void fail() {
		
	}
}