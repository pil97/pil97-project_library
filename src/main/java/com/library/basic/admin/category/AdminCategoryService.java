package com.library.basic.admin.category;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminCategoryService {
	
	private final AdminCategoryMapper adminCategoryMapper;
	
	// 1차 카테고리 목록
	public List<AdminCategoryVO> getFirstCategoryList() {
		return adminCategoryMapper.getFirstCategoryList();
	};
	
	// 2차 카테고리 목록
	public List<AdminCategoryVO>getSecondCategoryList(int c_pcode) {
		return adminCategoryMapper.getSecondCategoryList(c_pcode);
	};
	
	// 2차 카테고리 정보를 이용한 1차 카테고리 정보
	public AdminCategoryVO getFirstCategoryBySecondCategory(int c_code) {
		return adminCategoryMapper.getFirstCategoryBySecondCategory(c_code);
	};

}
