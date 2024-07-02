package com.library.basic.bookboard;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/board/*")
@Controller
@RequiredArgsConstructor
public class BookBoardController {

	private final BookBoardService bookBoardService;

	// 도서 등록 폼
	@GetMapping("write")
	public void write() {
		log.info("called write...");

	}

	// 도서 등록
	@PostMapping("write")
	public String write(BookBoardVO vo) {

		log.info("도서 등록 입력데이터: " + vo);

		// db저장.
		bookBoardService.write(vo);

		return "redirect:/board/list";
	}

	// 도서 목록 조회
	@GetMapping("list")
	public void list(Criteria cri, Model model) {

		// 데이타소스(list)를 jsp에서 사용할 경우에는 파라미터를 Model 를 사용한다.

		List<BookBoardVO> list = bookBoardService.listWithPaging(cri);

		log.info("게시물 목록데이타: " + list);

		// 1)게시물 목록 10건
		model.addAttribute("list", list);

		int total = bookBoardService.getTotalCount(cri);
		PageDTO pageDTO = new PageDTO(cri, total);

		log.info("페이징 기능데이타: " + pageDTO);

		// 2)페이징기능. 1 2 3 4 5 6 7 8 9 10 [next]
		model.addAttribute("pageMaker", pageDTO);

	}
	
		// 도서 상세페이지 및 수정페이지
		@GetMapping(value = { "get", "modify" })
		public void get(Long b_bno, @ModelAttribute("cri") Criteria cri, Model model) {
			log.info("게시물번호: " + b_bno);

			BookBoardVO boardVO = bookBoardService.get(b_bno);

			model.addAttribute("boardVO", boardVO);
		}

		// 게시글 수정
		@PostMapping("modify")
		public String modify(BookBoardVO vo) {
			log.info("수정내용: " + vo);

			bookBoardService.modify(vo);

			return "redirect:/board/list";
		}
}
