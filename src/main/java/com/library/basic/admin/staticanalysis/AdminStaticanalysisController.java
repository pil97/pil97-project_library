package com.library.basic.admin.staticanalysis;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/staticanalysis/*")
@Controller
public class AdminStaticanalysisController {
	
	private final AdminStaticanalysisService adminStaticanalysisService;
	
	// 현재 날짜 및 연도, 월 정보
	@GetMapping("/orderstatus")
	public void getOrderStatus(Model model) {
				
		// 현재 날짜 
		LocalDate now = LocalDate.now();
		
		log.info("현재 날짜 : " + now);
		
		// 현재 연도
		int year = now.getYear();
		
		// 현재 월
		int month = now.getMonthValue();

		model.addAttribute("year", year);
		model.addAttribute("month", month);

	}
	
	// 1차 카테고리 월별 매출현황
	@GetMapping("/monthlysalesstatusbytopcategory")
	@ResponseBody
	public List<Map<String, Object>> getMonthlySalesStatusByTopCategory(int year, int month) {

		String ord_date = String.format("%s/%s", year, (month < 10 ? "0" + String.valueOf(month) : month));

		List<Map<String, Object>> listObjMap = adminStaticanalysisService.monthlySalesStatusByTopCategory(ord_date);
		
		log.info("값: " + listObjMap); 

		return listObjMap;

	}
	
	// 현재 날짜 및 연도, 월 정보
	@GetMapping("/orderstatus2")
	public void getOrderStatus2(Model model) {
				
		// 현재 날짜 
		LocalDate now = LocalDate.now();
		
		log.info("현재 날짜 : " + now);
		
		// 현재 연도
		int year = now.getYear();
		
		// 현재 월
		int month = now.getMonthValue();

		model.addAttribute("year", year);
		model.addAttribute("month", month);

	}
	
	// 2차 카테고리 월별 매출현황
	@GetMapping("/monthlysalesstatusbysubcategory")
	@ResponseBody
	public List<Map<String, Object>> getMonthlySalesStatusBySubCategory(int year, int month) {

		String ord_date = String.format("%s/%s", year, (month < 10 ? "0" + String.valueOf(month) : month));

		List<Map<String, Object>> listObjMap = adminStaticanalysisService.monthlySalesStatusBySubCategory(ord_date);
		
		log.info("값: " + listObjMap); 

		return listObjMap;

	}
	

}
