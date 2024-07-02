package com.library.basic.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

public class FileManagerUtils {

	// 기능? 현재 폴더를 운영체제에 맞게 문자열로 반환
	public static String getDateFolder() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 포맷 형식
		Date date = new Date(); // 오늘 날짜 정보

		String str = sdf.format(date); // "2024-05-16" 폴더명 문자열

		// file.separtor : 이 코드를 실행하는 운영체제별로 파일의 경로 구분자를 리턴

		// 유닉스, 맥, 리눅스 구분자 : / 예) "2024-05-16" -> "2024/05/16"
		// 윈도우즈 구분자 : \ 예) "2024-05-16" -> "2024\05\16"

		// separator 메서드가 / 이걸로 변환 시켜줌
		return str.replace("-", File.separator);
	}

	// 기능? 파일 업로드
	// String uploadFolder : 업로드 폴더명 -
	// /Users/apple/Desktop/springBoot/spring_work/upload/pds
	// String dateFolder : 날짜 폴더명 - "2024/05/16"
	// MltipartFile uploadFile : 클라이언트에서 전송한 파일

	public static String uploadFile(String uploadFolder, String dateFolder, MultipartFile uploadFile) {

		String realUploadFileName = ""; // 실제 업로드한 파일명

		// File 클래스 : JDK 제공. - 파일과 폴더 관련 기능을 제공

		// File file = new File(파일또는 폴더 정보 구성); file.명령어(속성과 메서드)
		// - 파일 또는 폴더 존재 여부 확인
		// - 파일 또는 폴더 생성
		// - 파일 또는 폴더 속성 확인

		// 업로드 할 폴더 file 객체
		File file = new File(uploadFolder, dateFolder); // 예) "/Users/apple/Desktop/springBoot/spring_work/upload/pds"
														// "2024/05/16"

		// "2024/05/16" 폴더가 존재하지 않으면, 폴더 생성
		// 새로운 날짜에 첫번쩨 파일업로드가 진행이 되면, 폴더 생성되고, 두번째 파일 업로드부터는 폴더가 생성되지 않는다
		if (file.exists() == false) {
			// "/Users/apple/Desktop/springBoot/spring_work/upload/pds" "2024/05/16"
			file.mkdirs();
		}

		// 클라이언트에서 보낸 파일명
		String clientFileName = uploadFile.getOriginalFilename(); // abc.png
		UUID uuid = UUID.randomUUID(); // 9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d

		// 9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d_abc.png
		realUploadFileName = uuid.toString() + "_" + clientFileName;

		// 예외처리 작업
		try {
			File saveFile = new File(file, realUploadFileName);
			// 파일 복사 (원본파일을 해상도 크기를 줄여 썸네일 이미지 생성하기)
			uploadFile.transferTo(saveFile);

			// Thumnail 작업
			if (checkImageType(saveFile)) {

				// Thumnail 파일명: s_9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d_abc.png 생성
				File thumnailFile = new File(file, "s_" + realUploadFileName);

				// saveFile 객체: 업로드 된 파일 정보
				// 임시 버퍼 기억 장소
				BufferedImage bo_img = ImageIO.read(saveFile);

				double ratio = 3;
				int width = (int) (bo_img.getWidth() / ratio);
				int height = (int) (bo_img.getHeight() / ratio);

				Thumbnails.of(saveFile).size(width, height).toFile(thumnailFile);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 실제 업로드 되는 파일명 반환
		return realUploadFileName; // 실제 업로드 되는 파일명: 9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d_abc.png
	}

	// 기능? 업로드 파일의 MIME 타입 확인, 즉 이미지 파일 또는 일반파일 여부를 체크
	public static boolean checkImageType(File saveFile) {

		boolean isImageType = false;

		try {
			// MIME: text/html, text/plain, image/jpeg, image/png, image/jpg....
			// 클라이언트에서 전송한 파일의 MIME 정보 추출
			String contentType = Files.probeContentType(saveFile.toPath());

			// contentType 변수의 내용이 "image" 문자열 시작여부 -> boolean 값 반환
			isImageType = contentType.startsWith("image");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isImageType;
	}

	// 기능? 이미지 파일을 웹브라우저 화면에 보이는 작업
	// <img src="abc.gif"> -> <img src="메핑주소"> 매핑주소를 통한 서버측에서 받아오는 바이트 배열을 이용하여,
	// 브라우저가 이미지를 표시한다
	// uploadPath : 서버 업로드 폴더 예)
	// /Users/apple/Desktop/springBoot/spring_work/upload/pds
	// fileName : 이미지 파일명
	// 파일 업로드되는 폴더가 프로젝트 외부에 존재하여, 보안적인 이슈가 있으므로, 업로드 파일들을 바이트 배열로 읽어서 클라이언트로 보낸다
	public static ResponseEntity<byte[]> getFile(String uploadPath, String fileName) throws Exception {

		ResponseEntity<byte[]> entity = null;

		File file = new File(uploadPath, fileName);

		if (!file.exists()) {
			return entity;
		}

		HttpHeaders headers = new HttpHeaders();

		// Files.probeContentType(file.toPath())) : MIME TYPE 정보 예) image/jpeg
		headers.add("Content-Type", Files.probeContentType(file.toPath()));

		entity = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

		return entity;
	}

	// 기능? 이미지 파일 삭제
	// String uploadPath : 서버 업로드 폴더
	// String folderName : 날짜 폴더명
	// String fileName : 파일명 (날짜 폴더명 포함)
	public static void delete(String uploadPath, String dateFolderName, String fileName, String type) {

		// 1. 썸네일 파일 예) /Users/apple/Desktop/springBoot/spring_work/upload/pds
		// "2024/05/16" s_9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d_abc.png
		File file1 = new File((uploadPath + "/" + dateFolderName + "/" + fileName).replace('/', File.separatorChar));
		if (file1.exists())
			file1.delete();

		if (type.equals("image")) {
			// 2. 원본 파일 예) /Users/apple/Desktop/springBoot/spring_work/upload/pds
			// substring(2) s_fmf 뺴고 원본파일명: 9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d_abc.png
			File file2 = new File(
					(uploadPath + "/" + dateFolderName + "/" + fileName.substring(2)).replace('/', File.separatorChar));
			if (file2.exists())
				file2.delete();
		}

	}

}
