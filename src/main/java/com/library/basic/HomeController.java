package com.library.basic;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.library.basic.admin.book.BookVO;
import com.library.basic.admin.category.AdminCategoryService;
import com.library.basic.admin.category.AdminCategoryVO;
import com.library.basic.common.util.FileManagerUtils;
import com.library.basic.usr.search.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

	private final AdminCategoryService adminCategoryService;

	private final SearchService searchService;

	// 도서 이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;

	// @ResponseBody // 어노테이션이 사용이 안되면, return "index"는 데이터가 아니라 타임리프 파일명으로 인식
	@GetMapping("/")
	public String index(Model model) {

		List<AdminCategoryVO> cateList = adminCategoryService.getFirstCategoryList();		
		model.addAttribute("userCateList", cateList);
		
		List<BookVO> imgList = searchService.imageList();
		model.addAttribute("imgList", imgList);
		
		
		
		

		log.info("기본 주소.");

		return "index";
	}

	// 도서리스트 - 메인페이지 이미지 보여주기 <img src="메핑주소">
	@GetMapping("/imagedisplay")
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception {

		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}

}
