package com.library.basic.admin.staticanalysis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminStaticanalysisService {
	
	private final AdminStaticanalysisMapper adminStaticanalysisMapper;

	public List<Map<String, Object>> findDailyOrderStats() {
		return adminStaticanalysisMapper.findDailyOrderStatus();
	};
	
	// 1차 카테고리 월별 매출현황
	public List<Map<String, Object>> monthlySalesStatusByTopCategory(String ord_date) {
		return adminStaticanalysisMapper.monthlySalesStatusByTopCategory(ord_date);
	};
}
