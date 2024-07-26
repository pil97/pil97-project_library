package com.library.basic.admin.qna;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.qna.MyQnaVO;

public interface AdminQnaMapper {
	
	// 관리자 - QnA 목록
	List<MyQnaVO> adminQnaList(@Param("cri") Criteria cri, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	//  관리자 - QnA 목록 개수
	int getTotalCount(@Param("cri") Criteria cri, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	// 관리자 - QnA 상세 정보
	MyQnaVO adminQnaDetailInfo(Long qna_code);
	
	// 도서 QnA 답변 등록
	void qnaAnswerSave(@Param("qna_code") String qna_code, @Param("qna_answer") String qna_answer);
	
	// 도서 QnA 삭제
	void qnaDelete(String qna_code);

}
