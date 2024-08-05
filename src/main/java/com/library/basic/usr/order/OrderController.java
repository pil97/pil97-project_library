package com.library.basic.usr.order;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.basic.usr.UserService;
import com.library.basic.usr.UserVO;
import com.library.basic.usr.cart.CartProductVO;
import com.library.basic.usr.cart.CartService;
import com.library.basic.usr.cart.CartVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/order/*")
@Controller
public class OrderController {

	private final OrderService orderService;
	private final CartService cartService;
	private final UserService userService;

	// 주문정보 페이지
	@GetMapping("/orderlist")
	public String orderList(@RequestParam(value = "type", defaultValue = "direct") String type , CartVO vo, Model model, HttpSession session) throws Exception {

		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();
		vo.setUsr_id(usr_id);

		// 1. 장바구니 저장
		// 바로구매 및 도서리스트에서 구매
		if (!type.equals("cartorder")) {
			cartService.cartAdd(vo);
		}				

		// 2. 주문내역
		List<CartProductVO> cartList = cartService.cartList(usr_id);
		int totalPrice = 0;
		for (CartProductVO d_vo : cartList) {
			d_vo.setBook_up_folder(d_vo.getBook_up_folder().replace("\\", "/"));
			totalPrice += d_vo.getCart_amount() * d_vo.getBook_deposit();
		}
			
		model.addAttribute("cartList", cartList);
		model.addAttribute("totalPrice", totalPrice);

		return "/user/order/orderlist";
	}
	
	// 주문자 정보와 동일
	@GetMapping("ordersame")
	public ResponseEntity<UserVO> orderSame(HttpSession session) throws Exception {
		
		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();		
		
		UserVO vo = userService.login(usr_id);
		
		ResponseEntity<UserVO> entity = null;
		
		entity = new ResponseEntity<UserVO>(vo, HttpStatus.OK);
		
		return entity;
	}

	// 무통장 입금
	@PostMapping("/ordernobank")
	public String orderNoBank(OrderVO vo, 
							  @RequestParam("pay_bankinfo") String pay_bankinfo,
							  @RequestParam("pay_account") String pay_account,
							  @RequestParam("pay_name") String pay_name, 
							  @RequestParam("book_bno") List<Integer> book_bno, 
							  @RequestParam("book_amount") List<Integer> book_amount, 
							  HttpSession session) throws Exception {
		
		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();
		
		vo.setUsr_id(usr_id);
				
		orderService.orderProcess(vo, usr_id, "무통장입금", pay_bankinfo, pay_account, pay_name, "미납", book_bno, book_amount);		
						
		return "redirect:/user/order/ordercomplete";
	}
	
	// 주문완료
	@GetMapping("/ordercomplete")
	public void orderComplete() {
		
	}
}
