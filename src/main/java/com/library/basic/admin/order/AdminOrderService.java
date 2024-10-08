package com.library.basic.admin.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.order.OrderVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminOrderService {
	
	private final AdminOrderMapper adminOrderMapper;
	
	// 주문내역 목록
	public List<OrderVO> orderList(Criteria cri, String startDate, String endDate) {
		return adminOrderMapper.orderList(cri, startDate, endDate);
	}; 
	
	// 주문내역 개수
	public int getTotalCount(Criteria cri, String startDate, String endDate) {
		return adminOrderMapper.getTotalCount(cri, startDate, endDate);
	};
	
	// 주문자(수령인) 정보
	public OrderVO orderInfo(Long ord_code) {
		return adminOrderMapper.orderInfo(ord_code);
	};
	
	// 주문상세정보
	public List<OrderDetailInfoVO> orderDetailInfo(Long ord_code) {
		return adminOrderMapper.orderDetailInfo(ord_code);
	};
	
	// 주문상세 개별 삭제
	@Transactional
	public void orderBookDeleteProcess(Long ord_code, int book_bno) {
		
		// 1. 주문상세테이블 db 삭제
		adminOrderMapper.orderBookDelete(ord_code, book_bno);
		
		// 2. 주문테이블 금액 변경
		adminOrderMapper.orderBookPriceChange(ord_code);
				
		// 3. 결제테이블 금액 변경
		adminOrderMapper.paymentPriceChange(ord_code);
		
	}
	
	// 주문상세 내역 주문자 정보 수정
	public void orderDetailModify(OrderVO vo) {
		adminOrderMapper.orderDetailModify(vo);
	};
	
	// 해당 주문내역 상제
	@Transactional
	public void orderBookAllDeleteProcess(Long ord_code) {
		
		// 1. 주문상세테이블 db 삭제
		adminOrderMapper.orderDetailInfoDelete(ord_code);
		
		// 2. 주문테이블 db 삭제
		adminOrderMapper.orderInfoDelete(ord_code);
				
		// 3. 결제테이블 db 삭제
		adminOrderMapper.paymentInfoDelete(ord_code);
		
	}

}
