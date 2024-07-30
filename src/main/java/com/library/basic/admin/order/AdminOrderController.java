package com.library.basic.admin.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.basic.common.dto.Criteria;
import com.library.basic.common.dto.PageDTO;
import com.library.basic.common.util.FileManagerUtils;
import com.library.basic.usr.order.OrderVO;
import com.library.basic.usr.payment.PaymentService;
import com.library.basic.usr.payment.PaymentVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/order/*")
@Controller
public class AdminOrderController {

	private final AdminOrderService adminOrderService;
	private final PaymentService paymentService;

	// 도서 이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;

	// 주문내역
	@GetMapping("/orderlist")
	public void orderList(Criteria cri, @ModelAttribute("startDate") String startDate, @ModelAttribute("endDate") String endDate, Model model) throws Exception {

		// 페이지당 주문내역 2개 출력
		cri.setAmount(2);

		// 주문내역 목록
		List<OrderVO> vo = adminOrderService.orderList(cri, startDate, endDate);

		// 주문내역 개수
		int totalCount = adminOrderService.getTotalCount(cri, startDate, endDate);

		model.addAttribute("orderList", vo);
		model.addAttribute("pageMaker", new PageDTO(cri, totalCount));
	}

	// 주문상세정보
	@GetMapping("/orderdetailinfo")
	public ResponseEntity<Map<String, Object>> orderDetailInfo(Long ord_code) throws Exception {

		ResponseEntity<Map<String, Object>> entity = null;

		Map<String, Object> map = new HashMap<>();

		// 주문자(수령인) 정보
		OrderVO vo = adminOrderService.orderInfo(ord_code);
		map.put("OrderVO", vo);

		// 주문상세정보
		List<OrderDetailInfoVO> orderDetailList = adminOrderService.orderDetailInfo(ord_code);
		map.put("orderDetailList", orderDetailList);

		// 결제정보
		PaymentVO pVo = paymentService.orderPayInfo(ord_code);
		map.put("PaymentVO", pVo);

		entity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

		return entity;
	}

	// 도서리스트 - 이미지 보여주기 <img src="메핑주소">
	@GetMapping("/imagedisplay")
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception {

		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}

	// 주문상세 개별 삭제
	@GetMapping("/orderbookdelete")
	public ResponseEntity<String> orderBookDelete(Long ord_code, int book_bno) throws Exception {

		// log.info("주문번호: " + ord_code);
		// log.info("책번호: " + book_bno);
		
		
		ResponseEntity<String> entity = null;

		// db연동
		 adminOrderService.orderBookDeleteProcess(ord_code, book_bno);

		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		return entity;
	}
	
	// 주문상세 내역 주문자 정보 수정
	@PostMapping("/orderdetailmodify")
	public ResponseEntity<String> orderDetailModify(OrderVO vo) throws Exception {

		// log.info("수정정보 : " + vo);
		
		ResponseEntity<String> entity = null;

		// db연동		
		adminOrderService.orderDetailModify(vo);

		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		return entity;
	}
	
	// 해당 주문내역 삭제
	@GetMapping("/orderbookalldelete")
	public ResponseEntity<String> orderBookAllDelete(Long ord_code) throws Exception {

		// log.info("주문번호: " + ord_code);		
		
		
		ResponseEntity<String> entity = null;

		// db연동
		 adminOrderService.orderBookAllDeleteProcess(ord_code);

		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		return entity;
	}

}
