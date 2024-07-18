package com.library.basic.admin.order;

import java.util.List;

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

}
