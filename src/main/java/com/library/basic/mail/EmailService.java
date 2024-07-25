package com.library.basic.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.library.basic.common.constants.Constants;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	// EmailConfig.java 파일의 javaMailSender() 메서드가 스프링 시스템에서 실행되어, 리턴되는 타입의 객체
	// 즉, bean을 생성 및 등록 작업을 하고, 아래 객체에 주입을 해준다.
	// EmailServiceImpl bean이 생성됨 -> EmailController 주입
	private final JavaMailSender mailSender;

	private final SpringTemplateEngine templateEngine;

	public void sendMail(String type, EmailDTO dto, String authCode) {

		// mail 이름
		type = Constants.MAILFOLDERNAME + "/" + type;

		// 메일 구성 정보 담당(받는 사람, 보내는 사람, 받는 사람 메일주소, 본문 내용)
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(dto.getReceiverMail()); // 메일 수신자
			mimeMessageHelper.setFrom(new InternetAddress(dto.getSenderMail(), dto.getSenderMail()));
			mimeMessageHelper.setSubject(dto.getSubject()); // 메일 제목
			mimeMessageHelper.setText(setContext(authCode, type), true); // 메일 본문 내용, HTML 여부

			// 메일 발송 기능
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 관리자 - 이메일 발송		
	public void sendMail(EmailDTO dto, String[] emailArr) {
		// 메일 구성 정보 담당(받는 사람, 보내는 사람, 받는 사람 메일주소, 본문 내용)
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(emailArr); // 메일 수신자 
			mimeMessageHelper.setFrom(new InternetAddress(dto.getSenderMail(), dto.getSenderMail()));
			mimeMessageHelper.setSubject(dto.getSubject()); // 메일 제목 
			mimeMessageHelper.setText(dto.getMessage(), true); // 메일 본문 내용, HTML 여부 
			
			// 메일 발송 기능 
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}

	// thymeleaf를 통한 html 적용
	// String code : 인증코드
	// String type : email.html
	private String setContext(String code, String type) {
		Context context = new Context();
		context.setVariable("code", code);

		return templateEngine.process(type, context);
	}

}
