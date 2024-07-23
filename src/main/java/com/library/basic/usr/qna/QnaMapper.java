package com.library.basic.usr.qna;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.order.OrderVO;

public interface QnaMapper {

	// QnA 작성
	void qnaWrite(QnaVO vo);
	
	// QnA 목록 
	List<QnaVO> qnaList(@Param("book_bno") int book_bno, @Param("cri")Criteria cri);
	
	// QnA 개수
	int getCountQnaByBookBno(int book_bno);
	
	// QnA 상세글
	QnaVO qnaForm(Long qna_code);
	
	// Qna 수정
	void qnaModify(QnaVO vo);
	
	// Qna 삭제
	void qnaDelete(Long qna_code);
	
	// 나의 QnA 목록
	List<MyQnaVO> myQnaList(@Param("usr_id") String usr_id, @Param("cri") Criteria cri);
	
	//   나의 QnA 목록 개수
	int getTotalCount(@Param("usr_id") String usr_id, @Param("cri") Criteria cri);
	
	
}
