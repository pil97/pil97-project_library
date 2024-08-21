package com.library.basic.common.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.extern.slf4j.Slf4j;

// 스프링이 시작되면서 자동으로 생성자 호출 - 객체 생성 
@Slf4j // 로그 출력
@Configuration // bean 등록작업
@PropertySource("classpath:mail/email.properties") // application.properties에 말고 임의로 만든 파일인 경우 이 어노테이션 사용해야함 (인식 시켜주는기능)
public class EmailConfig {

	// 파일이 실행되는지 확인하기 위해 생성자 생성
	public EmailConfig() throws Exception {
		log.info("EmailConfig.java constructor called...");
	}

	// email.properties 파일의 설정 정보를 참조
	
	// 사용 안함 
	@Value("${spring.mail.transport.protocol}")
	private String protocol; // smtp 값이 들어옴
	
	// 사용 안함 
	@Value("${spring.mail.debug}")
	private boolean debug;

	// 사용 7개 
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private boolean auth; // true

	@Value("${spring.mail.properties.mail.starttls.enable}")
	private boolean starttls; // true
	
	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.port}")
	private int port;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${spring.mail.default-encoding}")
	private String encoding;

	// 스프링이 시작되면서 자동으로 메서드 호출 - 리턴값 반환 	
	// javaMailSender bean 생성 및 스프링 컨테이너에 등록 
	// bean 목적? 의존성 주입(DI) 목적 
	@Bean // javaMailSender 스프링에서 매일 발송하는 객체
	public JavaMailSender javaMailSender() {

		// JavaMailSenderImpl 클래스가 어떤 메일 서버를 이용하여 메일 발송할지 서버에 대한 정보를 구성하는 작업 
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		Properties properties = new Properties(); // https://dev-cini.tistory.com/82

		// 추가

		properties.put("mail.smtp.auth", auth);
		properties.put("mail.smtp.starttls.enable", starttls);

		mailSender.setHost(host);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setPort(port);
		mailSender.setJavaMailProperties(properties);
		mailSender.setDefaultEncoding(encoding);
		
		// log.info("메일서버: " + host);
		return mailSender;

	}

}
