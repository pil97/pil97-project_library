package com.library.basic.admin.qna;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.basic.common.dto.Criteria;
import com.library.basic.usr.qna.MyQnaVO;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class AdminQnaService {

	private final AdminQnaMapper adminQnaMapper;
	
	// 관리자 - QnA 목록
	public List<MyQnaVO> adminQnaList(Criteria cri, String startDate, String endDate) {
		return adminQnaMapper.adminQnaList(cri, startDate, endDate);
	};
	
	//  관리자 - QnA 목록 개수
	public int getTotalCountt(Criteria cri, String startDate, String endDate) {
		return adminQnaMapper.getTotalCount(cri, startDate, endDate);
	};
	// 관리자 - QnA 상세 정보
	public MyQnaVO adminQnaDetailInfo(Long qna_code) {
		return adminQnaMapper.adminQnaDetailInfo(qna_code);
	};
	
	// 도서 QnA 답변 등록
	public void qnaAnswerSave(String qna_code, String qna_answer) {
		adminQnaMapper.qnaAnswerSave(qna_code, qna_answer);
	};
	
	// 도서 QnA 삭제
	public void qnaDelete(String qna_code) {
		adminQnaMapper.qnaDelete(qna_code);
	};
	
}
