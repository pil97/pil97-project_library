package com.library.basic.admin.order;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.order.OrderVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminOrderService {
	
	private final AdminOrderMapper adminOrderMapper;
	
	// 주문내역 목록
	public List<OrderVO> orderList(Criteria cri) {
		return adminOrderMapper.orderList(cri);
	}; 
	
	// 주문내역 개수
	public int getTotalCount(Criteria cri) {
		return adminOrderMapper.getTotalCount(cri);
	};
	
	// 주문자(수령인) 정보
	public OrderVO orderInfo(Long ord_code) {
		return adminOrderMapper.orderInfo(ord_code);
	};
	
	// 주문상세정보
	public List<OrderDetailInfoVO> orderDetailInfo(Long ord_code) {
		return adminOrderMapper.orderDetailInfo(ord_code);
	};

}
