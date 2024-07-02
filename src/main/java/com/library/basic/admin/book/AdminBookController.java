package com.library.basic.admin.book;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.basic.admin.category.AdminCategoryService;
import com.library.basic.admin.category.AdminCategoryVO;
import com.library.basic.common.dto.Criteria;
import com.library.basic.common.dto.PageDTO;
import com.library.basic.common.util.FileManagerUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/book/*")
@Controller
public class AdminBookController {

	private final AdminBookService adminBookService;

	private final AdminCategoryService adminCategoryService;

	// 도서 이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;

	// CKeditor 파일업로드 경로
	@Value("${file.ckdir}")
	private String uploadCKPath;

	// 도서등록 페이지
	@GetMapping("bookregister")
	public void bookRegisterPage(Model model) {

		List<AdminCategoryVO> cateList = adminCategoryService.getFirstCategoryList();
		model.addAttribute("cateList", cateList);

	}

	// 도서등록 업로드
	@PostMapping("/bookregister")
	public String bookRegister(BookVO vo, MultipartFile uploadFile) throws Exception {

		// 1. 도서이미지 업로드
		String dateFolder = FileManagerUtils.getDateFolder();
		String saveFileName = FileManagerUtils.uploadFile(uploadPath, dateFolder, uploadFile);

		vo.setBook_img(saveFileName);
		vo.setBook_up_folder(dateFolder);

		log.info("상품정보 : " + vo);

		// 2. 도서정보 DB저장
		adminBookService.bookRegister(vo);

		return "redirect:/admin/book/booklist";
	}

	// 도서등록 - 도서설명 이미지 업로드
	@PostMapping("/imageupload")
	public void imageUpload(HttpServletRequest request, HttpServletResponse response, MultipartFile upload) {

		OutputStream out = null;
		PrintWriter printWriter = null; // 서버에서 클라이언트에게 응답정보를 보낼때 사용 (업로드한 이미지 정보를 브라우저에게 보내는 작업용도)

		try {
			// 1. CKeditor를 이용한 파일업로드 처리
			String fileName = upload.getOriginalFilename(); // 업로드 할 클라이언트 파일 이름
			byte[] bytes = upload.getBytes(); // 업로드 할 파일의 바이트 배열

			// log.info("파일 이름: " + fileName);

			String ckUploadPath = uploadCKPath + fileName; // /Users/apple/Desktop/springBoot/spring_work/upload/ckeditor/
															// + "abc.gif"

			out = new FileOutputStream(ckUploadPath); // /Users/apple/Desktop/springBoot/spring_work/upload/ckeditor/abc.gif
														// 파일생성. 0byte

			out.write(bytes); // 빨대(스트림)의 공간에 업로드 할 파일의 바이트 배열을 채운 상태
			out.flush(); // /Users/apple/Desktop/springBoot/spring_work/upload/ckeditor/abc.gif 0 byte ->
							// 크기가 채워진 정상적인 파일로 인식

			// 2. 업로드한 이미지 파일에 대한 정보를 클라이언트에게 보내는 작업

			printWriter = response.getWriter();

			String fileUrl = "/admin/book/display/" + fileName; // 메핑주소/이미지파일
			// String fileUrl = fileName;

			log.info("파일 URL: " + fileUrl);

			// CKeditor 4.12에서는 파일 정보를 아래와 같이 구성해서 보내야함
			// ("filename :" + "abc.gif"(변수처리), "uploaded":1,
			// "url":"/ckupload/abc.gif"(변수처리)}
			// ("filename" :변수, "uploaded":1, "url":변수}
			// ckeditor 버전
			printWriter.println("{\"filename\" :\"" + fileName + "\", \"uploaded\":1, \"url\":\"" + fileUrl + "\"}"); // 스트림에
																														// 채움

			printWriter.flush();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (printWriter != null) {
				printWriter.close();
			}
		}

	}

	// 도서등록 - 도서설명 이미지 업로드 매핑주소
	@GetMapping("display/{fileName}")
	public ResponseEntity<byte[]> getFile(@PathVariable("fileName") String fileName) {

		ResponseEntity<byte[]> entity = null;

		log.info("파일이미지" + fileName);

		try {
			entity = FileManagerUtils.getFile(uploadCKPath, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return entity;
	}

	// 도서리스트
	@GetMapping("/booklist")
	public void booktList(Criteria cri, Model model) throws Exception {

		List<BookVO> bookList = adminBookService.booktList(cri);

		int totalCount = adminBookService.getTotalCount(cri);

		model.addAttribute("bookList", bookList);
		model.addAttribute("pageMaker", new PageDTO(cri, totalCount));
	}

	// 도서리스트 - 이미지 보여주기 <img src="메핑주소">
	@GetMapping("/imagedisplay")
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception {

		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}

	// 도서 수정 페이지
	@GetMapping("/bookedit")
	public void bookEditPage(@ModelAttribute("cri") Criteria cri, Integer book_bno, Model model) throws Exception {

		// 1차 카테고리 목록
		List<AdminCategoryVO> cateList = adminCategoryService.getFirstCategoryList();
		model.addAttribute("cateList", cateList);

		// 도서 정보(2차 카테고리)
		BookVO vo = adminBookService.bookEditPage(book_bno);
		model.addAttribute("bookVO", vo);

		// 기존 1차 카테고리 정보
		int c_code = vo.getC_code(); // 도서테이블에 존재하는 2차 카테고리 코드
		int c_pcode = adminCategoryService.getFirstCategoryBySecondCategory(c_code).getC_pcode();
		model.addAttribute("c_pcode", c_pcode);

		// 기존 2차 카테고리 정보
		model.addAttribute("subCateList", adminCategoryService.getSecondCategoryList(c_pcode));

	}

	// 도서 수정
	@PostMapping("/bookedit")
	public String bookEdit(BookVO vo, MultipartFile uploadFile, Criteria cri, RedirectAttributes rttr)
			throws Exception {

		log.info("상품 수정정보 : " + vo);

		String msg = "";

		// 도서이미지 변경(업로드) 유무
		if (!uploadFile.isEmpty()) {
			// 기존 상품이미지 삭제 - 날짜폴더명, 파일명
			FileManagerUtils.delete(uploadPath, vo.getBook_up_folder(), vo.getBook_img(), "image");

			// 변경이미지 업로드
			String dateFolder = FileManagerUtils.getDateFolder();
			String saveFileName = FileManagerUtils.uploadFile(uploadPath, dateFolder, uploadFile);

			// 새로운 파일명, 날짜 폴더명
			vo.setBook_img(saveFileName);
			vo.setBook_up_folder(dateFolder);

		}

		// db저장(update)
		adminBookService.bookEdit(vo);

		msg = "successEdit";
		rttr.addFlashAttribute("msg", msg);

		return "redirect:/admin/book/booklist" + cri.getListLink();
	}
	
	// 도서 삭제
	@PostMapping("/bookdelete")
	public String bookDelete(Integer book_bno, Criteria cri) {
		
		adminBookService.bookDelete(book_bno);
		
		
		return "redirect:/admin/book/booklist" + cri.getListLink();
	}

	// 체크상품 수정작업
	@PostMapping("/checkedmodify")
	public ResponseEntity<String> checkedModfiy(
			@RequestParam("book_bno_arr") List<Integer> book_bno_arr, 
			@RequestParam("book_author_arr") List<String> book_author_arr,
			@RequestParam("book_deposit_arr") List<Integer> book_deposit_arr,
			@RequestParam("book_publisher_arr") List<String> book_publisher_arr, 			
			@RequestParam("book_loan_arr") List<String> book_loan_arr) throws Exception {
	
		log.info("도서코드 : " + book_bno_arr);
		log.info("저자 : " + book_author_arr);
		log.info("도서보증금 : " + book_deposit_arr);
		log.info("출판사 : " + book_publisher_arr);		
		log.info("대출여부 : " + book_loan_arr);
		
		ResponseEntity<String> entity = null;
		
		adminBookService.checkedModify(book_bno_arr, book_author_arr, book_deposit_arr, book_publisher_arr, book_loan_arr);
		
		
		entity = new ResponseEntity<>("success", HttpStatus.OK);
		
		return entity;
	}
}
