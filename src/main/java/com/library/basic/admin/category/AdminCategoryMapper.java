package com.library.basic.admin.category;

import java.util.List;

public interface AdminCategoryMapper {

	// 1차 카테고리 목록
	List<AdminCategoryVO> getFirstCategoryList();
	
	// 2차 카테고리
	List<AdminCategoryVO>getSecondCategoryList(int c_pcode);
	
	// 2차 카테고리 정보를 이용한 1차 카테고리 정보
	AdminCategoryVO getFirstCategoryBySecondCategory(int c_code);
}
