$(document).ready(
	function() {



		// 카테고리 클릭시, 1차 카테고리 출력
		$("a.firstCategory").on("click", function(e) {
			e.preventDefault(); // a태그의 링크 기능 제거			
			let cur_a = $(this);

			// console.log("cur_a", cur_a);

			// return;

			$.ajax({
				url: "/category/catelist",
				type: "get",
				dataType: "json",
				success: function(result) {
					let cate_code = "";
					for (let i = 0; i < result.length; i++) {
						cate_code += '<a class="dropdown-item dropdown-toggle sub" id = "novelDropdown" role = "button" data - toggle="dropdown" aria - haspopup="true" aria - expanded="false" href = "'
							+ result[i].c_code
							+ '">'
							+ result[i].c_name
							+ "</a>";
					}

					// cur_a.next().empty();		
					// cur_a.next().append(cate_code);	
					$("div.secondCategory").empty().append(cate_code);

				}
			});
		});

		// 1차 카테고리 클릭시, 2차 카테고리 출력
		$("div.secondCategory").on("click", "a.sub", function(e) {
			e.preventDefault();
			let c_pcode = $(this).attr("href");
			let cur_a = $(this);

			console.log(c_pcode);

			$.ajax({
				url: "/category/subcatelist",
				type: "get",
				data: { c_pcode: c_pcode },
				dataType: "json",
				success: function(result) {

					console.log(result);
					let sub_code = "";
					for (let i = 0; i < result.length; i++) {
						sub_code += '<a class="dropdown-item" href="'
							+ result[i].c_code
							+ '">'
							+ result[i].c_name
							+ "</a>";
					}

					// cur_a.after(sub_code);					
					// cur_a.after(sub_code).addClass("show");
					cur_a.next('.dropdown-menu').empty().append(sub_code);
					cur_a.next('.dropdown-menu').addClass('show');
				},
			});
		});
	});