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
	public List<MyQnaVO> adminQnaListt(Criteria cri, String startDate, String endDate) {
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
	
}
