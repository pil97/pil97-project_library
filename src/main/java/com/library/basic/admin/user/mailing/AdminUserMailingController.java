package com.library.basic.admin.user.mailing;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.basic.common.constants.Constants;
import com.library.basic.common.dto.Criteria;
import com.library.basic.common.util.FileManagerUtils;
import com.library.basic.mail.EmailDTO;
import com.library.basic.mail.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/user/mailing/*")
@Controller
public class AdminUserMailingController {
	
	private final AdminUserMailingService adminUserMailingService;
	private final EmailService emailService;
	
	// CKeditor 파일업로드 경로
	@Value("${file.ckdir}")
	private String uploadCKPath;
	
	// 회원목록(검색, 페이징 포함)

	// 회원 조회및 수정
	
	// 메일 발송 목록
	@GetMapping("/mailinglist")
	public void mailingList(Criteria cri, Model model) throws Exception {

	}
	
	// 메일 발송 폼(CkEditor 사용) - 구분 1. 광고/이벤트 2. 일반
	@GetMapping("/mailingform")
	public void mailingForm() {

	}
	
	// 메일 발송 프로세스
		@PostMapping("/mailingprocess")
		public String mailingProcess(MailingVO vo, RedirectAttributes rttr) throws Exception {
			
			String url = "";
			String msg = "";
						
			// 1. 메일 내용 DB 저장
			adminUserMailingService.mailingSave(vo);
			
			// 2, 메일 발송
			// 2.1. 회원테이블에서 전체회원 메일정보를 읽어오는 작업
			String[] emailArr = adminUserMailingService.getAllUserMailAddress();
			
			log.info("메일주소 : " + emailArr);
			
			// 메일 정보
			EmailDTO dto = new EmailDTO("PilLibrary", "PilLibrary", "", vo.getMailingTitle(), vo.getMailingContent());
 			
			// 메일 발송
			emailService.sendMail(dto, emailArr);
		
			rttr.addFlashAttribute("msg", "success");
			
			return "redirect:/admin/user/mailing/mailinglist";
		}
	
	
	
	// CKEditor 상품설명 이미지 업로드
		// 파라미터명 - upload : 에디터 이미지 업로드 클릭시 나오는 name 명
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

				String fileUrl = Constants.ROOTURL +  "/admin/user/mailing/display/" + fileName; // 메핑주소/이미지파일
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

		// <img src="매핑주소">
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
	
	

}
