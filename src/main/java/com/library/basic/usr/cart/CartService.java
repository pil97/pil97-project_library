package com.library.basic.usr.cart;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CartService {

	private final CartMapper cartMapper;
	
	// 장바구니 추가
	public void cartAdd(CartVO vo) {
		cartMapper.cartAdd(vo);
	};
	
	// 장바구니 목록
	public List<CartProductVO> cartList(String usr_id) {
		return cartMapper.cartList(usr_id);
	};
	
	// 장바구니 삭제
	public void cartDelete(Long cart_code) {
		cartMapper.cartDelete(cart_code);
	};
	
	// 장바구니 도서 수량 변경
	public void cartAmountChange(Long cart_code, int cart_amount) {
		cartMapper.cartAmountChange(cart_code, cart_amount);
	};
	
	// 장바구니 비우기
	public void cartEmpty(String usr_id) {
		cartMapper.cartEmpty(usr_id);
	};
	
	// 선택된 체크 도서 삭제
	public void checkedDelete(List<Long> cart_code_arr) {
		cartMapper.checkedDelete(cart_code_arr);
	};
	
}
