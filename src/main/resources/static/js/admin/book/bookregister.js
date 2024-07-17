$(document).ready(function() {
	// 1차 카테고리 선택
	$("#btnFirstCategory").on("change", function() {
		let cateCode = $("#btnFirstCategory").val();

		// console.log(cateCode);

		// 2차 카테고리 주소
		let url = "/admin/category/secondcategory/" + cateCode;

		$.getJSON(url, function(secondCategoryResult) {
			let btnSecondCategory = $("#btnSecondCategory"); // 2차 카테고리 select 태그를 참조
			let optionStr = "";

			for (let i = 0; i < secondCategoryResult.length; i++) {
				optionStr += "<option value='" + secondCategoryResult[i].c_code + "'>" + secondCategoryResult[i].c_name + "</option>";
			}
			btnSecondCategory.find("option").remove();
			btnSecondCategory.append("<option>2차 카테고리 선택</option>");
			btnSecondCategory.append(optionStr);
		});
	});
});

// 상품이미지 미리보기
$("#uploadFile").on("change", function(e) {
	let file = e.target.files[0];

	let reader = new FileReader();
	reader.readAsDataURL(file);

	reader.onload = function(e) {
		$("#img_preview").attr("src", e.target.result);
	};
});

// ckeditor 환경설정.
$(document).ready(function() {
	// ckeditor 환경설정. 자바스크립트 Ojbect문법
	var ckeditor_config = {
		resize_enabled: false, // 박스 사이크 크기 조절
		enterMode: CKEDITOR.ENTER_BR, // enter 입력마다 br 태그 적용
		shiftEnterMode: CKEDITOR.ENTER_P,
		toolbarCanCollapse: true,
		removePlugins: "elementspath",
		//업로드 탭기능추가 속성. CKEditor에서 파일업로드해서 서버로 전송클릭하면 , 이 주소가 동작된다.
		// /admin/product/imageupload - controller 주소 참조
		filebrowserUploadUrl: "/admin/book/imageupload", // 이미지 업로드 버튼
	};

	// pro_content - 상품설명 id 참조
	CKEDITOR.replace("book_content", ckeditor_config);

	console.log("ckediotr 버전: ", CKEDITOR.version); // ckeditor 버전: 4.12.1 (Standard)
});