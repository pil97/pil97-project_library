package com.library.basic.usr.qna;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.basic.common.dto.Criteria;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QnaService {

	private final QnaMapper qnaMapper;
	
	// QnA 작성
	public void qnaWrite(QnaVO vo) {
		qnaMapper.qnaWrite(vo);
	};
	
	// QnA 목록
	public List<QnaVO> qnaList(int book_bno, Criteria cri) {
		return qnaMapper.qnaList(book_bno, cri);				
	};
	
	// QnA 개수
	public int getCountQnaByBookBno(int book_bno) {
		return qnaMapper.getCountQnaByBookBno(book_bno);
	};
	
	// QnA 상세글
	public QnaVO qnaForm(Long qna_code) {
		return qnaMapper.qnaForm(qna_code);
	};
	
}
