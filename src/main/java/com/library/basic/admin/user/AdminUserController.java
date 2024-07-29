package com.library.basic.admin.user;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.basic.common.dto.Criteria;
import com.library.basic.common.dto.PageDTO;
import com.library.basic.usr.UserVO;
import com.library.basic.usr.qna.MyQnaVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/user/*")
@Controller
public class AdminUserController {

	private final AdminUserService adminUserService;

	// 관리자 - 회원 목록 페이지
	@GetMapping("/userlist")
	public void adminUserList(Criteria cri, @ModelAttribute("startDate") String startDate,
			@ModelAttribute("endDate") String endDate, Model model) throws Exception {

		cri.setAmount(1);

		// 관리자 - 회원 목록
		List<Map<String, Object>> adUserList = adminUserService.adminUserList(cri, startDate, endDate);

		log.info("목록 " + adUserList);

		// 관리자 - 회원 목록 개수
		int totalCount = adminUserService.adminUserListgetTotalCount(cri, startDate, endDate);

		 model.addAttribute("adUserList", adUserList);
		 model.addAttribute("pageMaker", new PageDTO(cri, totalCount));

	}
	
	// 관리자 - 회원 상세 정보
	@GetMapping("/userinfo")
	public void qnaInfo(String usr_id, Model model) throws Exception {

		// db 연동
		UserVO vo = adminUserService.adminUserDetailInfo(usr_id);

		model.addAttribute("adUserVO", vo);
	}
	
	// 관리자 - 회원 상세 정보 수정
	@PostMapping("/usermodify")
	public String userModify(UserVO vo, RedirectAttributes rttr) throws Exception {

		// log.info("수정정보 : " + vo);
		
		// db연동
		adminUserService.userModify(vo);
		
		rttr.addFlashAttribute("msg", "modify");

		return "redirect:/admin/user/userlist";
	}

	// 관리자 - 회원 아이디 삭제
	@GetMapping("/userdelete")
	public ResponseEntity<String> userDelete(String usr_id) throws Exception {

		ResponseEntity<String> entity = null;

		// db연동
		adminUserService.userDelete(usr_id);

		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		return entity;
	}

}
