package com.library.basic.category;

import java.util.List;

public interface CategoryMapper {

	// 1차 카테고리
	List<CategoryVO> categoryAllList();

	// 2차 카테고리
	List<CategoryVO> subCategoryList(Integer c_pcode);

}
