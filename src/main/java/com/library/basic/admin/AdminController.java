package com.library.basic.admin;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/*")
@Controller
public class AdminController {

	private final AdminService adminService;

	private final PasswordEncoder passwordEncoder;

	// 관리자 로그인 페이지
	@GetMapping("")
	public String adminLoginPage() {

		log.info("관리자 로그인 페이지");

		return "admin/adminlogin";
	}

	// 관리자 로그인 확인
	@PostMapping("/adminok")
	public String adminLoginOk(AdminVO vo, HttpSession session, RedirectAttributes rttr) throws Exception {
		log.info("관리자 정보 : " + vo);

		// 아이디 확인
		AdminVO dVo = adminService.adminLoginOk(vo.getAdmin_id());

		String url = "";
		String msg = "";

		// 아이디가 존재하면
		if (dVo != null) {
			// 비밀번호가 일치하면
			if (passwordEncoder.matches(vo.getAdmin_pw(), dVo.getAdmin_pw())) {				
				dVo.setAdmin_pw(""); 
				session.setAttribute("adminStatus", dVo);
				url = "adminmenu";
				log.info("로그인");
			} else {
				msg = "failPW";
			}			
			
		} else {
			msg = "failID";
		};
		
		rttr.addFlashAttribute("msg", msg);
		
		return "redirect:/admin/" + url;
	}
	
	// 관리자 메뉴 페이지
	@GetMapping("/adminmenu")
	public void amdinMenu() {
		
	}
	
	@GetMapping("/logout")
	public String adminLogout(HttpSession session){
		
		session.removeAttribute("adminStatus");
		
		return "redirect:/admin/";
	}
	
}
