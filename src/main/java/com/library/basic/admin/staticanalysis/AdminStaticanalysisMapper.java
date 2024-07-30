package com.library.basic.admin.staticanalysis;

import java.util.List;
import java.util.Map;

public interface AdminStaticanalysisMapper {
	
	List<Map<String, Object>> findDailyOrderStatus();
	
	// 1차 카테고리 월별 매출현황
	List<Map<String, Object>> monthlySalesStatusByTopCategory(String ord_date);

}
