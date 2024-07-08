package com.library.basic.usr.book;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.basic.admin.book.BookVO;
import com.library.basic.common.dto.Criteria;
import com.library.basic.common.dto.PageDTO;
import com.library.basic.common.util.FileManagerUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/book/*")
@Controller
public class BookController {

	private final BookService bookService;
	
	// 도서 이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;
//	
//	// 도서 리스트
//	@GetMapping("/booklist")
//	public void bookList()
//			throws Exception {
//		
//	}
	
	
	
	// 도서 리스트
	@GetMapping("/booklist")
	public void bookList(@ModelAttribute("c_code") int c_code, @ModelAttribute("c_name") String c_name, Criteria cri, Model model)
			throws Exception {
		log.info("책 리스트");
		log.info("2차 카테고리 코드 : " + c_code);
		log.info("2차 카테고리 이름 : " + c_name);
		
		cri.setAmount(9);
		
		List<BookVO> userBookList = bookService.bookList(c_code, cri);
		
		int totalCount = bookService.getCountBookByCategory(c_code);
		
		model.addAttribute("userBookList", userBookList);
		model.addAttribute("pageMaker", new PageDTO(cri, totalCount));
	}
	
	
	// 도서리스트 - 이미지 보여주기 <img src="메핑주소">
	@GetMapping("/imagedisplay")
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception {

//			log.info("이미지 업로드 경로 : " + uploadPath);
//			log.info("이미지 업로드 경로 : " + dateFolderName);
//			log.info("이미지 업로드 경로 : " + fileName );

		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}
	
	// 도서정보 - 모달창
	@GetMapping("/bookinfo")
	public ResponseEntity<BookVO> bookInfo(int book_bno) throws Exception {
		ResponseEntity<BookVO> entity = null;
		
		// db 연동
		BookVO vo = bookService.bookInfo(book_bno);
		
		entity = new ResponseEntity<BookVO>(vo, HttpStatus.OK);
		
		return entity;
	}
	
	// 도서정보2 - 모달창
	@GetMapping("/bookinfo2")
	public void bookInfo2(int book_bno, Model model) throws Exception {
				
		//db 연동
		BookVO vo = bookService.bookInfo(book_bno);
				
		model.addAttribute("bookVO", vo);		
	}
	
	// 도서 상세페이지
	@GetMapping("/bookdetail")
	public void bookDetail(int book_bno , Model model) throws Exception {
		
		// db 연동
		BookVO vo = bookService.bookInfo(book_bno);
		
		model.addAttribute("bookVO", vo);
		
	}
	
}
