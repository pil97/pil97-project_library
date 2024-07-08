package com.library.basic.usr.cart;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CartMapper {
	
	// 장바구니 추가
	void cartAdd(CartVO vo);
	
	// 장바구니 목록
	List<CartProductVO> cartList(String usr_id);
	
	// 장바구니 삭제
	void cartDelete(Long cart_code);
	
	// 장바구니 도서 수량 변경
	void cartAmountChange(@Param("cart_code") Long cart_code, @Param("cart_amount") int cart_amount);
	
	// 장바구니 비우기
	void cartEmpty(String usr_id);
	
	// 선택된 체크 도서 삭제
	void checkedDelete(@Param("cart_code_arr") List<Long> cart_code_arr);
	

}
