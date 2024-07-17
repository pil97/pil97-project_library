package com.library.basic.usr.kakaopay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaoPayService {
	@Value("${kakaopay.api.secret.key}")
	private String kakaopaySecretKey;

	@Value("${cid}")
	private String cid;

	@Value("${approval}")
	private String approval;

	@Value("${cancel}")
	private String cancel;

	@Value("${fail}")
	private String fail;

	private String tid;
	private String partnerUserId;
	private String partnerOrderId;
	
    // 1. 결제 준비 요청 
    public ReadyResponse ready(String partnerOrderId, String partnerUserId, String itemName, int quantity, 
    							int totalAmount, int taxFreeAmount, int vatAmount) {
    	
        // 1. 요청 헤더 준비 - Request header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.set("Content-Type", "application/json;charset=utf-8");

        // 2. 요청 파라미터 준비 - Request param
        // @Builder lombok 사용해야함 
        ReadyRequest readyRequest = ReadyRequest.builder()
                .cid(cid)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(partnerUserId)
                .itemName(itemName)
                .quantity(quantity)
                .totalAmount(totalAmount)
                .taxFreeAmount(taxFreeAmount)
                .vatAmount(vatAmount)
                .approvalUrl(approval)	// 성공 -> 카카오페이 서버에서 이 주소를 찾아온다
                .cancelUrl(cancel)		// 취소 -> 카카오페이 서버에서 이 주소를 찾아온다
                .failUrl(fail)			// 실패 -> 카카오페이 서버에서 이 주소를 찾아온다
                .build();

        // 3. Send reqeust
        HttpEntity<ReadyRequest> entityMap = new HttpEntity<>(readyRequest, headers);
             
        ResponseEntity<ReadyResponse> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                entityMap,
                ReadyResponse.class
        );
        
        ReadyResponse readyResponse = response.getBody();                      

        // 주문번호와 TID를 매핑해서 저장해놓는다.
        this.tid = readyResponse.getTid();	// 전역변수 작업 - approve 메서드에서 사용하기 때문
        
        this.partnerOrderId = partnerOrderId;
        
        this.partnerUserId = partnerUserId;
        
        return readyResponse;
    }

    // 2. 결제 승인 요청
    public String approve(String pgToken) {
        // ready할 때 저장해놓은 TID로 승인 요청
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request param
        ApproveRequest approveRequest = ApproveRequest.builder()
                .cid(cid)
                .tid(tid)
                .partnerOrderId(partnerOrderId)
                .partnerUserId(partnerUserId)
                .pgToken(pgToken)
                .build();

        // Send Request
        HttpEntity<ApproveRequest> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    entityMap,
                    String.class
            );

            // 승인 결과를 저장한다.
            String approveResponse = response.getBody();
            return approveResponse;
        } catch (HttpStatusCodeException ex) {
            return ex.getResponseBodyAsString();
        }
    }
}
