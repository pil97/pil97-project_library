$(document).ready(function () {
        $(".dropdown-menu a.dropdown-toggle").on("click", function (e) {
          e.preventDefault();
          e.stopPropagation();

          if (!$(this).next().hasClass("show")) {
            $(this).parents(".dropdown-menu").first().find(".show").removeClass("show");
          }

          var $subMenu = $(this).next(".dropdown-menu");

          $subMenu.toggleClass("show");

          $(this)
            .parents("li.nav-item.dropdown.show")
            .on("hidden.bs.dropdown", function (e) {
              $(".dropdown-submenu .show").removeClass("show");
            });

          return false;
        });

        // 1차 카테고리 선택
        $("a#firstCategory").on("click", function (e) {
          e.preventDefault();

          let curFirstCategory = $(this);

          let c_code = $(this).data("c_code");

          // console.log(curFirstCategory);
          // console.log(c_code);

          // return;

          // 2차 카테고리 주소
          let url = "/admin/category/secondcategory/" + c_code;

          $.getJSON(url, function (secondCategoryResult) {
            let str = '<ul class="dropdown-menu" id="secondCategory">';

            for (let i = 0; i < secondCategoryResult.length; i++) {
              str += `<li><a class="dropdown-item" href="#" data-c_code="${secondCategoryResult[i].c_code}">${secondCategoryResult[i].c_name}</a></li>`;
            }

            str += "</ul>";
            // console.log(str);
            // 새로 생성된 2차 카테고리 메뉴를 바로 하위에 추가
            // curFirstCategory.next('.dropdown-menu').remove(); // 기존 메뉴가 있으면 제거
            curFirstCategory.parents("ul#category_menu").find("ul#secondCategory").remove();
            curFirstCategory.after(str);
            curFirstCategory.next("ul#secondCategory").addClass("show");
            
          });
        });

        // 2차 카테고리 선택
        $("ul#category_menu").on("click", "ul#secondCategory li a", function (e) {
          e.preventDefault();

          let c_code = $(this).data("c_code");
          let c_name = encodeURIComponent($(this).html()); // 인코딩 작업 -> & 특수문자 때문에

          console.log("2차 카테고리 코드 : " + c_code);
          console.log("2차 카테고리 이름 : " + c_name);

          // return;

          // cat_name=맨투맨&후드티 -> & 특수문자로 인하여 서버에서는 맨투맨 문자열만 인식됨
          location.href = `/user/book/booklist?c_code=${c_code}&c_name=${c_name}`;
        });
      });