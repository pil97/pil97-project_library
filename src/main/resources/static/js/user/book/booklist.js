let actionForm = $("#actionForm");

$(document).ready(function () {
  // 페이지 번호 클릭시, (이전 1 2 3 4 5 다음) -> actionForm을 사용한다.
  $(".movepage").on("click", function (e) {
    e.preventDefault(); // a 태그의 href 기능을 제거

    // 주소변경
    actionForm.attr("action", "/user/book/booklist");

    // 1. 클릭한 페이지 번호 작업
    actionForm.find("input[name='pageNum']").val($(this).attr("href"));

    // 변경된 pageNum 값을 서버로 전송 (Criteria 클래스)
    actionForm.submit();
  });

  // name="btnCartAdd" - 모달
  $("button[name='btnCartAdd']").on("click", function () {
    $("#orderProcessPopUp").modal("show");

    let book_bno = $(this).data("book_bno");

    console.log("도서코드", book_bno);

    $("#popupInfo").load("/user/book/bookinfo?book_bno=" + book_bno);
  });

  // name="btnDirectOrder" - 모달
  $("button[name='btnDirectOrder']").on("click", function () {
    $("#orderProcessPopUp").modal("show");

    let book_bno = $(this).data("book_bno");

    console.log("도서코드", book_bno);

    $("#popupInfo").load("/user/book/bookinfo?book_bno=" + book_bno);
  });

  // 장바구니 추가 작업
  // id="btnCartAdd" -> 이게 동적으로 생성되기 때문에
  $("#orderProcessPopUp").on("click", "#btnCartAdd", function () {
    let book_bno = $(this).data("book_bno");
    let cart_amount = $("#btnCartAmount").val();

    console.log("도서코드 : ", book_bno);
    console.log("수량 : ", cart_amount);

    $.ajax({
      url: "/user/cart/cartadd",
      type: "get",
      data: { book_bno: book_bno, cart_amount: cart_amount },
      dataType: "text",
      success: function (result) {
        if (result == "success") {
          alert("장바구니에 등록되었습니다.");
          if (confirm("장바구니로 이동하시겠습니까?")) {
            location.href = "/user/cart/cartlist";
          } else {
            $("#orderProcessPopUp").modal("hide");
          }
        } else if ((result = "fail")) {
          alert("로그인을 해주세요.");
        }
      },
    });
  });

  // 바로구매 기능
  $("#orderProcessPopUp").on("click", "#btnDirectOrder", function () {
    let book_bno = $(this).data("book_bno");
    let cart_amount = $("#btnCartAmount").val();

    let url = `/user/order/orderlist?book_bno=${book_bno}&cart_amount=${cart_amount}`;
    location.href = url;
  });
}); // read event end
