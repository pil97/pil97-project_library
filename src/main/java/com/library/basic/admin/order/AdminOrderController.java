package com.library.basic.admin.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.basic.common.dto.Criteria;
import com.library.basic.common.dto.PageDTO;
import com.library.basic.usr.order.OrderVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/order/*")
@Controller
public class AdminOrderController {

	private final AdminOrderService adminOrderService;

	// 도서 이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;

	// 주문내역 
	@GetMapping("orderlist")
	public void orderList(Criteria cri, Model model) throws Exception {

		// 페이지당 주문내역 2개 출력
		cri.setAmount(2);

		// 주문내역 목록
		List<OrderVO> vo = adminOrderService.orderList(cri);

		// 주문내역 개수
		int totalCount = adminOrderService.getTotalCount(cri);

		model.addAttribute("orderList", vo);
		model.addAttribute("pageMaker", new PageDTO(cri, totalCount));
	}
	
	// 주문상세정보
	@GetMapping("orderdetailinfo")
	public void orderDetailInfo(Long ord_code, Model model) throws Exception {
		
		// 주문자(수령인) 정보
		OrderVO vo = adminOrderService.orderInfo(ord_code);
		
		// 주문상세정보
		List<OrderDetailInfoVO> dVo = adminOrderService.orderDetailInfo(ord_code);
		
		// 결제정보
		
		
	}

}
