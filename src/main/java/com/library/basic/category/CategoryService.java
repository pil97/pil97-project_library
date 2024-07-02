package com.library.basic.category;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService {

	private final CategoryMapper categoryMapper;

	 // 1차 카테고리
	public List<CategoryVO> categoryAllList() {

		return categoryMapper.categoryAllList();
	};

//	List<CategoryVO> categoryAllList(Integer c_code) {
//		return categoryAllList(c_code);
//	};
	
	// 2차 카테고리
	public List<CategoryVO> subCategoryList(Integer c_pcode) {
		return categoryMapper.subCategoryList(c_pcode);
	};

}
