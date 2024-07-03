package com.library.basic.usr.book;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/book/*")
@Controller
public class BookController {

	private final BookService bookService;
	
	@GetMapping("/booklist")
	public void bookList() {
		log.info("책 리스트");
	}
	
}
