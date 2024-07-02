$(document).ready(
	function() {



		// 카테고리 클릭시, 1차 카테고리 출력
		$("select.mainCategory").on("click", function(e) {
			e.preventDefault(); // a태그의 링크 기능 제거			
			let cur_a = $(this);

			console.log("cur_a", cur_a);

			// return;

			$.ajax({
				url: "/category/catelist",
				type: "get",
				dataType: "json",
				success: function(result) {
					let cate_code = "'<option>1차 카테고리 선택</option>";
					for (let i = 0; i < result.length; i++) {
						cate_code += '<option value=' + result[i].c_code + '">'
							+ result[i].c_name
							+ "</option>";
					}
					cur_a.html(cate_code);
				},
			});
		});

		// 1차 카테고리 출력
		$("select.mainCategory2").on("change", function(e) {
			e.preventDefault();
			let selectedOption = $(this).find("option:selected");
			console.log("1차 카테고리", selectedOption.val());
			console.log("1차 카테고리", selectedOption.text());
		});


		// 1차 카테고리 클릭시, 2차 카테고리 출력
		$("select.mainCategory").on("change", function(e) {
			e.preventDefault();
			let selectedOption = $(this).find("option:selected");
			let c_pcode = selectedOption.val();

			// Request the 2차 카테고리 based on selected 1차 카테고리
			$.ajax({
				url: "/category/subcatelist",
				type: "get",
				data: { c_pcode: c_pcode }, // Send the selected 1차 카테고리 value
				dataType: "json",
				success: function(result) {
					let sub_code = '<option>2차 카테고리 선택</option>';
					for (let i = 0; i < result.length; i++) {
						sub_code += '<option value=' + result[i].c_pcode + '">'
							+ result[i].c_name + "</option>";
					}
					$("select.subCategory").html(sub_code); // Update the 2차 카테고리 dropdown
				},
			});
		});
	});