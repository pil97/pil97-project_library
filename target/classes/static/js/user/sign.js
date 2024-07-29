// 메일수신동의 유무 확인
document.getElementById("usr_receive").addEventListener("change", function () {
  var hiddenInput = document.getElementById("usr_receive_value");
  hiddenInput.value = this.checked ? "Y" : "N";
});
// 유효성검사
$(document).ready(function () {
  let useIDCheck = false; // 아이디 중복체크 기능을 사용했는지 유무를 확인
  let emailAuthSuccess = false; // 이메일 인증 확인을 했는지 유무를 확인
  let pwdCheck = false; // 비밀번호 일치 여부 확인

  // 아이디 중복 확인
  $("#btnIDCheck").on("click", function () {
    if ($("#usr_id").val() == "") {
      alert("아이디를 입력하세요.");
      $("#usr_id").focus();
      return; // 이걸 사용해야 밑에 코드 실행 안됨
    }

    $.ajax({
      url: "/user/idCheck", // 작동되는 주소
      type: "get", // post Or get
      data: {
        usr_id: $("#usr_id").val(),
      }, // mbsp_id -> 스프링 메서드 idCheck(String mbsp_id) 파라미터명과 일치해야함
      dataType: "text", // 스프링에서 메서드 타입에 따라 달라짐 text, html, xml, json 값이 사용 -> 메서드 타입이 String이라 text로 해야함
      success: function (result) {
        // result 값이 스프링에서 entity = new ResponseEntity<String>(idUse, HttpStatus.OK); idUse 값이 사용됨 yes or no
        if (result == "yes") {
          alert("아이디 사용 가능");
          // 모달창이 아니라 화면에 출력하고 싶은 경우
          $("#idCheckMsg").html("사용 가능");
          useIDCheck = true;
        } else {
          alert("아이디 사용 불가능");
          $("#idCheckMsg").html("사용 불가능");
          $("#usr_id").val(""); // val() 안에 데이터를 입력시 값이 변경됨(setter기능) -> "" 입력시 입력한 공백이 됨 -> 입력한 아이디가 삭제
          $("#usr_id").focus();
        }
      },
    });
  });

  // 메일 인증 버튼
  $("#btnMailAuthCode").on("click", function () {
    // 메일 입력 값 확인
    if ($("#usr_email").val() == "") {
      alert("메일을 입력하세요.");
      $("#usr_email").focus();
      return;
    }

    $.ajax({
      url: "/email/authcode",
      type: "get",
      data: {
        receiverMail: $("#usr_email").val(),
        type: "emailJoin",
      },
      dataType: "text",
      success: function (result) {
        if (result == "success") {
          alert("메일로 인증코드가 발급되었습니다.");
        }
      },
    });
  });

  // 인증 확인
  $("#btnConfirmAuth").on("click", function () {
    if ($("#u_authcode").val() == "") {
      alert("인증코드를 입력하세요.");
      $("#u_authcode").focus();
      return;
    }

    $.ajax({
      url: "/email/confirm_authcode",
      type: "get",
      data: {
        authCode: $("#u_authcode").val(),
      },
      dataType: "text",
      success: function (result) {
        if (result == "success") {
          alert("인증이 확인 되었습니다.");
          emailAuthSuccess = true;
        } else if (result == "fail") {
          alert("인증코드 값을 확인해주세요.");
          console.log(u_authcode);
          $("#u_authcode").focus();
        } else if (result == "request") {
          alert("인증코드가 소멸되었습니다. \n 인증 요청 메일을 다시 진행해주세요.");
        }
      },
      error: function () {},
    });
  });

  // 회원가입 클릭
  // 아래 코드는 양립할 수가 없다.
  // 1. <button type="submit" class="btn btn-info" id="btnJoin">Sign</button> -> type이 submit이면, click 이벤트 사용안됨. -> form submit 이벤트 사용
  /*
  $("#joinForm").on("submit", function (e) {
	e.preventDefault();
	console.log("submit event");
	return;
  });
*/

  // 2. <button type="button" class="btn btn-info" id="btnJoin">Sign</button> -> type이 button이면, click 이벤트 사용가능.
  // 비밀번호 확인
  function checkPwd() {
    if ($("#usr_password").val() == $("#u_confirm_pwd").val()) {
      return (pwdCheck = true);
    }
  }

  $("#btnJoin").on("click", function () {
    // console.log("click event");

    // 회원가입 유효성 검사 : 정규식 활용

    if (!useIDCheck) {
      alert("아이디 중복을 확인해주세요.");
      $("#btnIDCheck").focus();
      return;
    }

    checkPwd();

    if (!pwdCheck) {
      alert("비밀번호가 일치하지 않습니다.");
      $("#u_confirm_pwd").focus();
      $("#u_confirm_pwd").val("");
      return;
    }

    if (!emailAuthSuccess) {
      alert("이메일 인증을 확인해주세요.");
      $("#btnConfirmAuth").focus();
      return;
    }

    // 버틀 클릭시 form 태그 작동
    $("#joinForm").submit();
  });

  // ready event end
});
