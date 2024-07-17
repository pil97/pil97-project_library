package com.library.basic.usr.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.basic.common.util.FileManagerUtils;
import com.library.basic.usr.UserVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/cart/*")
@Controller
public class CartController {

	private final CartService cartService;

	// 도서 이미지 업로드 경로
	@Value("${file.product.image.dir}")
	private String uploadPath;

	// 장바구니 추가
	@GetMapping("/cartadd")
	public ResponseEntity<String> cartAdd(CartVO vo, HttpSession session) throws Exception {

		log.info("장바구니 정보 : " + vo);

		ResponseEntity<String> entity = null;

		// 로그인 세션 확인
		UserVO loggedInUser = (UserVO) session.getAttribute("loginStatus");
		if (loggedInUser == null || loggedInUser.getUsr_id() == null) {
			entity = new ResponseEntity<String>("fail", HttpStatus.OK);
			return entity;
		}
		
		// 로그인 확인 후,
		String usr_id = loggedInUser.getUsr_id();
		vo.setUsr_id(usr_id);

		// db연동
		cartService.cartAdd(vo);

		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		return entity;
	}

	// 장바구니 목록
	@GetMapping("/cartlist")
	public void cartList(HttpSession session, Model model) throws Exception {

		// 로그인 세션 아이디 확인
		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();

		// db연동
		List<CartProductVO> cartList = cartService.cartList(usr_id);
		// mac은 사용안함, but window는 아래 코드 사용해야함
		// cartList.forEach(vo ->
		// vo.setBook_up_folder(vo.getBook_up_folder().replace("\\", "/")));

		model.addAttribute("cartList", cartList);

	}

	// 장바구니 목록 - 이미지 보여주기 <img src="메핑주소">
	@GetMapping("/imagedisplay")
	public ResponseEntity<byte[]> imageDisplay(String dateFolderName, String fileName) throws Exception {

//			log.info("이미지 업로드 경로 : " + uploadPath);
//			log.info("이미지 업로드 경로 : " + dateFolderName);
//			log.info("이미지 업로드 경로 : " + fileName );

		return FileManagerUtils.getFile(uploadPath + dateFolderName, fileName);
	}

	// 장바구니 삭제
	@GetMapping("/cartdelete")
	public String cartDelete(Long cart_code) throws Exception {

		cartService.cartDelete(cart_code);

		return "redirect:/user/cart/cartlist";
	}

	// 장바구니 도서 수량 변경
	@GetMapping("/cartamountchange")
	public String cartAmountChange(Long cart_code, int cart_amount) throws Exception {

		cartService.cartAmountChange(cart_code, cart_amount);

		return "redirect:/user/cart/cartlist";
	}

	// 장바구니 비우기
	@GetMapping("/cartempty")
	public String cartEmpty(HttpSession session) throws Exception {

		// 로그인 세션 아이디 확인
		String usr_id = ((UserVO) session.getAttribute("loginStatus")).getUsr_id();

		cartService.cartEmpty(usr_id);

		return "redirect:/user/cart/cartlist";
	}

	// 선택된 체크 도서 삭제
	@PostMapping("/checkeddelete")
	public ResponseEntity<String> checkedDelete(@RequestParam("cart_code_arr") List<Long> cart_code_arr) throws Exception {

		ResponseEntity<String> entity = null;
		
		log.info("장바구니 코드 : " + cart_code_arr);
		
		cartService.checkedDelete(cart_code_arr);
		

		entity = new ResponseEntity<String>("success", HttpStatus.OK);

		return entity;

	}

}
