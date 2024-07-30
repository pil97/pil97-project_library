package com.library.basic.usr;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.library.basic.common.dto.Criteria;
import com.library.basic.common.dto.PageDTO;
import com.library.basic.common.util.FileManagerUtils;
import com.library.basic.mail.EmailDTO;
import com.library.basic.mail.EmailService;
import com.library.basic.usr.kakaologin.KakaoLoginService;
import com.library.basic.usr.kakaologin.KakaoUserInfo;
import com.library.basic.usr.naverlogin.NaverLoginService;
import com.library.basic.usr.naverlogin.NaverResponse;
import com.library.basic.usr.order.OrderService;
import com.library.basic.usr.order.OrderVO;
import com.library.basic.usr.qna.MyQnaVO;
import com.library.basic.usr.qna.QnaService;
import com.library.basic.usr.review.MyReviewVO;
import com.library.basic.usr.review.ReviewService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RequestMapping("/user/*")
@Slf4j
@Controller
public class UserController {

	private final UserService userService;	
	private final NaverLoginService naverLoginService;
	private final KakaoLoginService kakaoLoginService;
	private final ReviewService reviewService;
	private final QnaService qnaService;
	private final OrderService orderService;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	// 도서 이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;

	// 회원가입 페이지
	@GetMapping("sign")
	public void signPage(HttpSession session, Model model) {
		// log.info("회원가입 페이지");
		
		UserVO vo = new UserVO();
		
		if (session.getAttribute("kakaoStatus") != null) {

			KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) session.getAttribute("kakaoStatus");

			// mypage에서 보여줄 정보를 선택적으로 작업
			vo = new UserVO();
			vo.setUsr_name(kakaoUserInfo.getNickname());
			vo.setUsr_email(kakaoUserInfo.getEmail());

			model.addAttribute("user", vo);
			model.addAttribute("msg", "kakaoLogin");

		} else if (session.getAttribute("naverStatus") != null) {

			NaverResponse naverUserInfo = (NaverResponse) session.getAttribute("naverStatus");
			
			// 1. user_table db 작업
			vo = new UserVO();
			vo.setUsr_name(naverUserInfo.getResponse().getName());
			vo.setUsr_email(naverUserInfo.getResponse().getEmail());
			model.addAttribute("user", vo);
			model.addAttribute("msg", "naverLogin");		
			
		} else {
			model.addAttribute("user", vo);
		}
	}

	// 회원가입 버튼
	@PostMapping("sign")
	public String signOk(UserVO vo, HttpSession session) throws Exception {

		// log.info("회원정보 : " + vo);

		// 비밀번호 암호화 변경
		vo.setUsr_password(passwordEncoder.encode(vo.getUsr_password()));

		// 회원가입 sns 로그인 여부 확인
		if (session.getAttribute("kakaoStatus") != null) {
			vo.setUsr_sns_type("kakao");
			// sns_table db 작업
			/*
			SNSUserDto dto = new SNSUserDto();
			dto.setId(vo.getUsr_id());
			dto.setEmail(vo.getUsr_email());
			dto.setSns_type(vo.getUsr_sns_type());
			*/
			
			// userService.snsUserInsert(dto);
			
		} else if (session.getAttribute("naverStatus") != null) {
			vo.setUsr_sns_type("naver");
			
			// sns_table db 작업
			/*
			SNSUserDto dto = new SNSUserDto();
			dto.setId(vo.getUsr_id());
			dto.setEmail(vo.getUsr_email());
			dto.setSns_type(vo.getUsr_sns_type());
			*/
			
			// userService.snsUserInsert(dto);
			
		} else {
			vo.setUsr_sns_type("x");
		}

		// 회원가입 정보 DB 저장
		userService.sign(vo);

		return "redirect:/user/login";
	}

	// 아이디 중복 유무 확인
	@GetMapping("idCheck")
	public ResponseEntity<String> idCheck(String usr_id) throws Exception {

		// log.info("아이디: " + usr_id);

		ResponseEntity<String> entity = null;

		String idUse = ""; // 클라이언트에서 dataType: text로 사용함

		// 아이디 중복 검사
		if (userService.idCheck(usr_id) != null) {
			idUse = "no"; // 사용 불가능
		} else {
			idUse = "yes"; // 사용 가능
		}

		entity = new ResponseEntity<String>(idUse, HttpStatus.OK);

		return entity;
	}

	// 로그인 페이지
	@GetMapping("login")
	public void loginPage() {
		// log.info("로그인 페이지");
	}

	// 로그인
	@PostMapping("/login")
	public String loginOk(LoginDTO dto, HttpSession session, RedirectAttributes rttr) throws Exception {

		UserVO vo = userService.login(dto.getUsr_id());

		String msg = "";
		String url = "/"; // "/" 메인 주소

		if (vo != null) {
			// 아이디가 존재하는 경우
			// 비밀번호 비교
			// 사용자가 입력한 비밀번호가 암호화된 형태에 해당하는 것이라면
			if (passwordEncoder.matches(dto.getUsr_password(), vo.getUsr_password())) {

				// 최근 로그인
				userService.lastLogin(vo.getUsr_id());
				
				// 로그인 여부를 확인 null 아니면 로그인
				session.setAttribute("loginStatus", vo);

			} else {
				// 사용자가 입력한 비밀번호가 암호화된 형태에 해당하지 않는 것이라면
				msg = "failPW";
				url = "/user/login";
			}

		} else {
			// 아이디가 존재하지 않을 경우
			msg = "failID";
			url = "/user/login"; // 로그인 폼 주소
		}

		// html에서 msg 변수를 사용 목적
		rttr.addFlashAttribute("msg", msg);

		return "redirect:" + url; // 메인으로 이동
	}

	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    String accessToken = (String) session.getAttribute("accessToken");

	    // log.info("access : " + accessToken);

	    if (accessToken != null && !"".equals(accessToken)) {
	        if (session.getAttribute("naverStatus") != null) {
	            // 네이버 로그아웃 처리
	            try {
	                log.info("네이버 로그아웃");
	                naverLoginService.getNaverTokenDelete(accessToken);
	            } catch (Exception e) {
	                log.error("네이버 로그아웃 중 오류 발생", e);
	            }
	            session.removeAttribute("naverStatus");
	        } else if (session.getAttribute("kakaoStatus") != null) {
	            // 카카오 로그아웃 처리
	            try {
	                log.info("카카오 로그아웃");
	                kakaoLoginService.kakaoLogout(accessToken);
	            } catch (JsonProcessingException e) {
	                log.error("카카오 로그아웃 중 오류 발생", e);
	            }
	            session.removeAttribute("kakaoStatus");
	        }

	        session.removeAttribute("accessToken");
	    }

	    // 로그인 세션 삭제
	    session.invalidate();

	    // log.info("로그아웃");

	    return "redirect:/";
	}



	// 아이디 찾기 페이지
	@GetMapping("/idfind")
	public void idFindPage() {

	}

	// 아이디 찾기
	@PostMapping("/idfind")
	public String idFindOk(String usr_name, String usr_email, String u_authcode, HttpSession session,
			RedirectAttributes rttr) throws Exception {

		// log.info("이름: " + usr_name);
		// log.info("이메일: " + usr_email);
		// log.info("인증코드: " + u_authcode);

		String url = "";
		String msg = "";

		// 인증코드 확인 작업
		if (u_authcode.equals(session.getAttribute("authCode"))) {

			// 아이디를 찾아 메일 발송
			String usr_id = userService.idFind(usr_name, usr_email);

			// 이름과 메일 주소 확인
			if (usr_id != null) {

				// 찾은 아이디 메일 발송 작업
				String subject = "필 도서관 아이디를 보냅니다.";
				EmailDTO dto = new EmailDTO("pilLibrary", "pilLibrary", usr_email, subject, usr_id);

				emailService.sendMail("emailIDResult", dto, usr_id);

				// 인증코드 세션 소멸
				session.removeAttribute("authCode");

				msg = "successID";
				url = "/user/login";
			} else {
				msg = "idFail";
				url = "/user/idfind";
			}

		} else {
			msg = "failAuth";
			url = "/user/idfind";
		}
		rttr.addFlashAttribute("msg", msg);
		rttr.addFlashAttribute("usr_name", usr_name);
		rttr.addFlashAttribute("usr_email", usr_email);

		return "redirect:" + url;
	}

	// 비밀번호 재발급 페이지
	@GetMapping("pwfind")
	public void pwFindPage() {

	}

	// 비밀번호 재발급
	@PostMapping("pwfind")
	public String pwFindOk(String usr_id, String usr_name, String usr_email, String u_authcode, HttpSession session,
			RedirectAttributes rttr) throws Exception {

		String url = "";
		String msg = "";

		// 인증코드 확인 작업
		if (u_authcode.equals(session.getAttribute("authCode"))) {
			// 사용자가 입력한 정보(3개 - 아이디, 이름, 이메일)를 조건으로 사용하여, 이메일을 db에서 가져온다.
			String d_email = userService.pwFind(usr_id, usr_name, usr_email);
			if (d_email != null) {

				// 비밀번호 임시코드를 생성하여, 암호화해서 사용자 아이디에 비밀번호를 수정한다.
				// 임시 비밀번호 생성(UUID 이용)
				String tempPw = userService.getTempPw();

				// 임시 비밀번호 생성 후 암호화
				String temp_enc_pw = passwordEncoder.encode(tempPw);

				// 암호화된 임시 비밀번호를 해당 아이디에 업데이트
				userService.tempPwUpdate(usr_id, temp_enc_pw);

				// 그리고, 임시코드를 메일로 발급한다.
				String subject = "필 도서관 임시비밀번호를 보냅니다.";
				EmailDTO dto = new EmailDTO("pilLibrary", "pilLibrary", usr_email, subject, usr_id);

				emailService.sendMail("emailPwResult", dto, tempPw);

				session.removeAttribute("authCode");

				url = "/user/login";
				msg = "successTempPw";
			} else {

				url = "/user/pwfind";
				msg = "failInput";
			}

		} else {
			url = "/user/pwfind";
			msg = "failAuth";
		}

		rttr.addFlashAttribute("msg", msg);
		rttr.addFlashAttribute("usr_id", usr_id);
		rttr.addFlashAttribute("usr_name", usr_name);
		rttr.addFlashAttribute("usr_email", usr_email);

		return "redirect:" + url;
	}

	// 마이페이지
	@GetMapping("mypage")
	public void mypage(HttpSession session, Model model) throws Exception {

		if (session.getAttribute("loginStatus") != null) {
			// 세션에 저장된 u_id 정보를 꺼내오는 방법
			// getAttribute(String name) Object -> UserInfoVO 형변환
			String u_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();

			UserVO vo = userService.login(u_id);

			model.addAttribute("user", vo);
		} else if (session.getAttribute("kakaoStatus") != null) {

			KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) session.getAttribute("kakaoStatus");

			// mypage에서 보여줄 정보를 선택적으로 작업
			UserVO vo = new UserVO();
			vo.setUsr_name(kakaoUserInfo.getNickname());
			vo.setUsr_email(kakaoUserInfo.getEmail());

			model.addAttribute("user", vo);
			model.addAttribute("msg", "kakaoLogin");

		} else if (session.getAttribute("naverStatus") != null) {

			NaverResponse naverUserInfo = (NaverResponse) session.getAttribute("naverStatus");

			UserVO vo = new UserVO();
			vo.setUsr_name(naverUserInfo.getResponse().getName());
			vo.setUsr_email(naverUserInfo.getResponse().getEmail());
			model.addAttribute("user", vo);
			model.addAttribute("msg", "naverLogin");
		}

	}

	// 내정보 수정
	@PostMapping("modify")
	public String modify(UserVO vo, HttpSession session, RedirectAttributes rttr) throws Exception {

		// 로그인 세션 확인
		if (session.getAttribute("loginStatus") == null)
			return "redirect:/user/login";

		// String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();

		// log.info("수정데이터: " + vo);

		userService.modify(vo);

		rttr.addFlashAttribute("msg", "successModify");

		return "redirect:/user/mypage";
	}

	// 비밀번호 변경 페이지
	@GetMapping("changepw")
	public void changPwPage() {

	}

	// 비밀번호 변경
	@PostMapping("changepw")
	public String changPw(String cur_pwd, String new_pwd, HttpSession session, RedirectAttributes rttr)
			throws Exception {

		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();

		UserVO vo = userService.login(usr_id);

		String msg = "";

		if (vo != null) {
			// 아이디가 존재하는 경우
			// 비밀번호 비교
			if (passwordEncoder.matches(cur_pwd, vo.getUsr_password())) {
				// 신규 비밀번호 변경 작업
				String usr_password = passwordEncoder.encode(new_pwd);
				userService.changePw(usr_id, usr_password);
				msg = "successPW";
			} else {
				// 사용자가 입력한 비밀번호가 암호화된 형태에 해당하지 않는 것이라면
				msg = "failPW";

			}

		}
		// jsp에서 msg 변수를 사용 목적
		rttr.addFlashAttribute("msg", msg);

		return "redirect:/user/changepw";
	}

	// 회원 탈퇴 페이지
	@GetMapping("delete")
	public void deletePage() {

	}

	// 회원 탈퇴
	@PostMapping("delete")
	public String delete(String usr_password, HttpSession session, RedirectAttributes rttr) throws Exception {

		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();

		UserVO vo = userService.login(usr_id);

		String msg = "";
		String url = "";

		// log.info("정보: " + vo);

		if (vo != null) {
			// 아이디가 존재하는 경우
			// 비밀번호 비교
			if (passwordEncoder.matches(usr_password, vo.getUsr_password())) {
				userService.delete(usr_id);
				session.invalidate();
				msg = "successDelete";
				url = "/";
			} else {
				// 사용자가 입력한 비밀번호가 암호화된 형태에 해당하지 않는 것이라면
				msg = "failDelete";
				url = "/user/delete";

			}
		}

		// html에서 msg 변수를 사용 목적
		rttr.addFlashAttribute("msg", msg);

		return "redirect:" + url;
	}

	// 회원 리뷰 목록 페이지
	@GetMapping("/myreviewlist")
	public void myReviewList(Criteria cri, HttpSession session, Model model) throws Exception{
		// 로그인 세션 아이디 확인
		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();

		// cri.setAmount(2);

		// 나의 QnA 목록
		List<MyReviewVO> myReviewList = reviewService.myReviewList(usr_id, cri);

		log.info("목록 " + myReviewList);

		// 나의 QnA 개수
		int totalCount = reviewService.getTotalCount(usr_id, cri);

		model.addAttribute("myReviewList", myReviewList);
		model.addAttribute("pageMaker", new PageDTO(cri, totalCount));

	}

	// 회원 QnA 목록 페이지
	@GetMapping("/myqnalist")
	public void myQnaList(Criteria cri, HttpSession session, Model model) throws Exception {

		// 로그인 세션 아이디 확인
		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();

		cri.setAmount(2);

		// 나의 QnA 목록
		List<MyQnaVO> myQnaList = qnaService.myQnaList(usr_id, cri);

		// log.info("목록 " + myQnaList);

		// 나의 QnA 개수
		int totalCount = qnaService.getTotalCount(usr_id, cri);

		model.addAttribute("myQnaList", myQnaList);
		model.addAttribute("pageMaker", new PageDTO(cri, totalCount));

	}
	
	// 나의 주문내역
	@GetMapping("/myorderlist")
	public void myOrderList(Criteria cri, @ModelAttribute("startDate") String startDate, @ModelAttribute("endDate") String endDate, Model model, HttpSession session) throws Exception {

		// 로그인 세션 아이디 확인
		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();
		
		// 페이지당 주문내역 2개 출력
		cri.setAmount(2);

		// 주문내역 목록
		List<OrderVO> myOrderList = orderService.myOrderList(cri, startDate, endDate, usr_id);

		// 주문내역 개수
		int totalCount = orderService.myOrderListGetTotalCount(cri, startDate, endDate, usr_id);

		model.addAttribute("myOrderList", myOrderList);
		model.addAttribute("pageMaker", new PageDTO(cri, totalCount));
	}

	// 도서 QnA 및 리뷰 목록 - 이미지 보여주기 <img src="메핑주소">
	@GetMapping("/imagedisplay")
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception {

		// log.info("이미지 업로드 경로 : " + uploadPath);
		// log.info("이미지 업로드 경로 : " + dateFolderName);
		// log.info("이미지 업로드 경로 : " + fileName );

		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}
	
}
