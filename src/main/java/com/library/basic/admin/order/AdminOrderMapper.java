package com.library.basic.admin.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.order.OrderVO;

public interface AdminOrderMapper {

	// 주문내역 목록
	List<OrderVO> orderList(Criteria cri);

	// 주문내역 개수
	int getTotalCount(Criteria cri);

	// 주문자(수령인) 정보
	OrderVO orderInfo(Long ord_code);

	// 주문상세정보
	List<OrderDetailInfoVO> orderDetailInfo(Long ord_code);

	// 주문상품 개별 삭제 - 1. 주문상세테이블 db 삭제
	void orderBookDelete(@Param("ord_code") Long ord_code, @Param("book_bno") int book_bno);

	// 주문상품 개별 삭제 - 2. 주문테이블 금액 변경
	void orderBookPriceChange(@Param("ord_code") Long ord_code, @Param("ord_price") int changePrice);
	
	// 주문상품 개별 삭제 - 3. 결제테이블 금액 변경
	void paymentPriceChange(@Param("ord_code") Long ord_code, @Param("pay_price") int changePrice);
	
	// 주문상세 내역 주문자 정보 수정
	void orderDetailModify(OrderVO vo);

}
