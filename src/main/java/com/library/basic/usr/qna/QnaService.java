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
	
	// Qna 수정
	public void qnaModify(QnaVO vo) {
		qnaMapper.qnaModify(vo);
	};
	
	// Qna 삭제
	public void qnaDelete(Long qna_code) {
		qnaMapper.qnaDelete(qna_code);
	};
	
	// 나의 QnA 목록
	public List<MyQnaVO> myQnaList(String usr_id, Criteria cri) {
		return qnaMapper.myQnaList(usr_id, cri);
	};
	
	//  QnA 목록 개수
	public int getTotalCount(String usr_id, Criteria cri) {
		return qnaMapper.getTotalCount(usr_id, cri);
	};
}
