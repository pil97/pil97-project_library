package com.library.basic.usr.order;

import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
	
	// 주문테이블 db 저장
	void orderInsert(OrderVO vo);
	
	// 주문상세테이블 db 저장
	void orderDetailInsert(@Param("ord_code")Long ord_code, @Param("usr_id") String usr_id, @Param("book_deposit") int book_deposit);

}
