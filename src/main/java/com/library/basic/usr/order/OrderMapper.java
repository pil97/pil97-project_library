package com.library.basic.usr.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.common.dto.Criteria;

public interface OrderMapper {
	
	// 주문테이블 db 저장
	void orderInsert(OrderVO vo);
	
	// 주문상세테이블 db 저장
	void orderDetailInsert(@Param("ord_code")Long ord_code, @Param("usr_id") String usr_id);
	
	// 나의 주문내역 목록
	List<OrderVO> myOrderList(@Param("cri") Criteria cri, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("usr_id") String usr_id);

	// 나의 주문내역 개수
	int myOrderListGetTotalCount(@Param("cri") Criteria cri, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("usr_id") String usr_id);

}
