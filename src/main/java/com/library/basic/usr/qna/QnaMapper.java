package com.library.basic.usr.qna;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.common.dto.Criteria;

public interface QnaMapper {

	// QnA 작성
	void qnaWrite(QnaVO vo);
	
	// QnA 목록 
	List<QnaVO> qnaList(@Param("book_bno") int book_bno, @Param("cri")Criteria cri);
	
	// QnA 개수
	int getCountQnaByBookBno(int book_bno);
	
	// QnA 상세글
	QnaVO qnaForm(Long qna_code);
	
	
	
}